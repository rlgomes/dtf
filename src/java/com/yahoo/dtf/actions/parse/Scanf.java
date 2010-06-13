package com.yahoo.dtf.actions.parse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.config.Config;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.util.StringUtil;

/**
 * @dtf.tag scanf
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc The scanf tag can be used to process data from the input 
 *               attribute and parse/extract different elements from this 
 *               property while following the syntax of the scanf function found
 *               in the C programming language.
 *               </br>
 *               </br>
 *               <b>
 *               One thing to note that is important with scanf is that it will
 *               read exactly what you tell it, so if you say read back "%2.2f"
 *               it will try to read back a float that has 2 digits in the whole
 *               part of the number and two fractional digits. So be aware
 *               because it wont' consume any extra digits or do any type of 
 *               conversion.</b>
 *               </br>
 *
 * @dtf.tag.example 
 * <scanf args="float,int,hex"
 *        format="%.2f some text %5d %04x"
 *        input="2.12 some text 00001 000a"/>
 *
 * @dtf.tag.example 
 * <scanf args="float,hex"
 *        format="%.2f %x"
 *        input="12.3456789 0x"/>
 *        
 */
public class Scanf extends Action {

    private static int ARGS_INDEX = 1;
   
    private static int PERC_INDEX = 2;
    private static int FLAG_INDEX = 3;
   
    private static int WIDT_INDEX = 4;
    private static int PREC_INDEX = 5;
    private static int TYPE_INDEX = 6;

    private static int STRI_INDEX = 7;

    private static Pattern ELEM = Pattern.compile(
     //          flags        width       precision         conversion type
     "((\\%)([\\-\\+\\#0]*)([0-9]*)(\\.?[0-9]*)([slLcdiubxXoeEfgG\\%]{1}))|([^%]*)");
    
    /**
     * @dtf.attr format
     * @dtf.attr.desc The scanf format string that follows a syntax compliant 
     *                with the C land implementation of scanf. Note that the 
     *                format you specify has to be the format the input is in 
     *                otherwise you won't get the right values out of your data.
     *                Right now here are the available options:
     *               
     *                <pre>
     *                %[flags][width][.precision]type
     *                </pre>
     *                
     *                <b>flags</b>
     *                <ul>
     *                  <li>0 - will pad the numerical values using 0's</li>
     *                </ul>
     *
     *                <b>width</b>
     *                <ul>
     *                  <li>[0-9]* - integer value representing the amount of
     *                               digits to read for the numerical value 
     *                               being processed by the scanf tag</li>
     *                </ul>
     *
     *                <b>precision</b>
     *                <ul>
     *                  <li>.[0-9]* - a dot followed by an integer value 
     *                                representing the amount of digits to read 
     *                                for the fractional part of the number 
     *                                being processed by the scanf tag</li>
     *                </ul>
     *              
     *                <b>type</b>
     *                <ul>
     *                  <li>% - matches the literal character %.</li>
     *                  <li>d,D,i matches with an integer which can be in
     *                            hexa-decimal format.</li>
     *                  <li>x,X matches a hexa-decimal number.</li>
     *                  <li>f,e,E,g matches an optionally signed floating-point
     *                              number.</li>
     *                  <li>s matches a sequence of non-white-space characters;
     *                        The input string stops at white space or at the
     *                        maximum field width, whichever occurs first.<li>
     *                  <li>c matches a sequence of characters whose length is 
     *                        specified by the maximum field width (default 1);
     *                  <li>[ matches a non-empty sequence of characters from 
     *                        the specified set of accepted characters.</li>
     *                </ul>
     */
    private String format = null;
    
    /**
     * @dtf.attr input
     * @dtf.attr.desc The input string to process using the format specified by 
     *                the format attribute.
     */
    private String input = null;
    
    /**
     * @dtf.attr args
     * @dtf.attr.desc A comma separated list of the property names to use when
     *                storing the various elements specified in the format string
     *                so that the test writer can access those values after the
     *                tag has completed execution.
     */
    private String args = null;
    
    @Override
    public void execute() throws DTFException {
        String format = getFormat();
        String[] args = getArgs().split(",");
        Matcher matcher = ELEM.matcher(format);
 
        byte[] buffer = getInput().getBytes();
        ByteArrayInputStream is = new ByteArrayInputStream(buffer);
        PushbackInputStream pis = new PushbackInputStream(is);
        Config config = getConfig();
        
        try { 
	        int index = 0;
	        while ( matcher.find() ) { 
	            String elem = matcher.group(ARGS_INDEX);
	            
	            if ( elem != null && elem.length() != 0 ) {
	                String prop = args[index++];

	                String flagStr = matcher.group(FLAG_INDEX);
	                char flag = (flagStr.length() != 0 ? flagStr.charAt(0) : '0');
	                
	                String widthStr = matcher.group(WIDT_INDEX);
	                    
	                int width = 
                       (widthStr.length() != 0 ? Integer.decode(widthStr) : -1);
	                
	                String preciStr = 
	                                 matcher.group(PREC_INDEX).replace(".", "");
	                int preci = 
                       (preciStr.length() != 0 ? Integer.decode(preciStr) : -1);
	               
	                char type  = matcher.group(TYPE_INDEX).charAt(0);
	                StringBuffer num = new StringBuffer("");
	                String aux = null;
	                int read = 0, cnt = 0;

	                switch (type) { 
	                    case 'd':
	                    case 'i':
	                        while ( (width == -1 || cnt++ < width) &&
	                                (read = pis.read()) != -1 &&
	                                 Character.isDigit(read) ) {
	                            num.append((char)read);
	                        }
	                        
	                        if ( read != -1 && !Character.isDigit(read) ) 
	                            pis.unread(read);
	   
	                        aux = "" + Long.valueOf(num.toString());
	                        aux = StringUtil.padString(aux, width, flag);
	                        config.setProperty(prop, aux);
	                        break;
	                    case 'e':
	                    case 'E':
	                    case 'f':
	                        // read the whole part
	                        while ( (width == -1 || cnt++ < width) &&
	                                (read = pis.read()) != -1 && 
	                                Character.isDigit(read) &&
	                                ((char)read) != '.' ) {
	                            num.append((char)read);
	                        }
	                        // read the fractional part
	                        if ( read == '.' ) { 
	                            num.append('.');
		                        while ( (preci == -1 || cnt++ < preci)  &&
		                                (read = pis.read()) != -1 &&
		                                Character.isDigit(read) ) {
		                           num.append((char)read);
		                        }
	                        }

	                        if ( read != -1 && !Character.isDigit(read) ) 
	                            pis.unread(read);
	                       
	                        aux = "" + num;
	                        aux = StringUtil.padString(aux, width, flag);
	                        config.setProperty(prop, aux);
	                        break;
	                    case 'x':
	                    case 'X':
	                        String HEXDIGIT = "0123456789abcdefABCDEF";
	                        while ( (width == -1 || cnt++ < width) &&
	                                 (read = pis.read()) != -1 &&
	                                 HEXDIGIT.indexOf(read) != -1 ) {
	                            num.append((char)read);
	                        }
	                        
	                        if ( read != -1 && HEXDIGIT.indexOf(read) == -1 ) 
	                            pis.unread(read);
	                        
	                        aux = "" + num;
	                        aux = StringUtil.padString(aux, width, flag);
	                        config.setProperty(prop, aux);
	                        break;
	                    case 's':
	                        StringBuffer s = new StringBuffer();
	                        while ( (width == -1 || cnt++ < width) &&
	                                (read = pis.read()) != -1 ) {
	                          
	                            // if there is no width attribute then read 
	                            // everything up to the next whitespace character
	                            if ( width == -1 && !Character.isWhitespace(read) )
	                                break;
	                            
	                            s.append((char)read);
	                        }
	                        
	                        if ( read != -1 ) pis.unread(read);

	                        config.setProperty(prop, s.toString());
	                        break;
	                    case 'c':
                            s = new StringBuffer();
                            
                            if ( width == -1 ) 
                                width = 1;
                            
                            while ( cnt++ < width && 
                                   (read = pis.read()) != -1 ) {
                                s.append((char)read);
                            }
                            
                            if ( cnt < width ) 
                                throw new ParseException("Expected more characters.");
	                       
	                        config.setProperty(prop, s.toString());
	                        break;
	                    case '%':
	                        if ( (read = pis.read()) != '%' ) { 
	                            throw new ParseException("Expected [%] got [" + 
	                                                     (char)read + "]");
	                        }
	                }
	            } else { 
	                // last one should have matched any other string
	                elem = matcher.group(STRI_INDEX);
	                
	                if ( elem.length() != 0 ) {
                        int cnt = 0;
                        int read = 0;
                        while ( cnt < elem.length() && 
                               (read = pis.read()) != -1) {

                            if ( ((char)read) != elem.charAt(cnt) ) { 
                                throw new ParseException("Expected [" + 
                                                         elem.charAt(cnt) + 
                                                         "] got [" + 
                                                         ((char)read) + "]");
                            }
                            cnt++;
                        }
	                }
	            }
	           
	            /* 
	             * If there are unresolved properties we can dynamically resolve
	             * them to a value that was previously instantiated by this 
	             * scanf tag.
	             * 
	             * This allows you to create scanf format argument that is 
	             * dynamic and saves CPU cycles by not having to run scanf twice
	             * on the same data, here's an example:
	             *
	             * Lets say you wanted to scan the string "000cHelloWorldXXXXX"
	             * knowing that the first thing in the string were 4 hex bytes 
	             * that dictated how long the following string was.
	             * 
	             */
	            if ( format.contains("${") ) { 
		            int start = matcher.start();
		            format = replaceProperties(format, true);
		            matcher = ELEM.matcher(format);
		            matcher.find(start);
	            }
	        }
        } catch (IOException e) { 
            throw new DTFException("Error processing scanf.",e);
        }
    }

    public String getFormat() throws ParseException { return replaceProperties(format,true); }
    public void setFormat(String format) { this.format = format; }

    public String getInput() throws ParseException { return replaceProperties(input); }
    public void setInput(String input) { this.input = input; }

    public String getArgs() throws ParseException { return replaceProperties(args); }
    public void setArgs(String args) { this.args = args; }
}
