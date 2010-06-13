package com.yahoo.dtf.actions.parse;

import java.util.IllegalFormatException;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.util.NumberUtil;

/**
 * @dtf.tag printf
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc The printf tag will mimic the behavior of the well known C land
 *               function but will output your results to the name of the 
 *               property identified by the attribute property. In the future we
 *               may allow for direct outputting to files but for now processing
 *               of the data to another property is what is needed.
 *
 * @dtf.tag.example 
 * <sequence>
 *      <property name="float" value="1.23456789"/> 
 *      <property name="list" value="1,2,3,4"/> 
 *      <printf format="INTEGER[%d] STRING[%s] FLOAT [%.2f] LIST [${list}]" 
 *              args="23,Hello There!,${float}"
 *              property="result"/>
 * </sequence>
 *         
 * @dtf.tag.example 
 * <sequence>
 *     <printf format="%04x" args="10" property="result"/>
 *     <log>10 -- convert to hex and pad to 4 digits -> ${result}</log>
 *     <assert><eq op1="${result}" op2="000a"/></assert>
 * </sequence>
 */
public class Printf extends Action {

    /**
     * @dtf.attr format
     * @dtf.attr.desc The printf format string that follows a syntax compliant 
     *                with the C land implementation of printf. For all of the 
     *                details on the format attribute just follow the existing 
     *                Java documentation on the String.format() function, 
     *                <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/Formatter.html#syntax">here</a>.
     */
    private String format = null;
    
    /**
     * @dtf.attr property
     * @dtf.attr.desc This proprety will contain the formatted output.
     *                
     */
    private String property = null;

    /**
     * @dtf.attr args
     * @dtf.attr.desc The args contains a comma separated list of the content 
     *                to push to apply in order to the format string. Be aware
     *                that additional commas in the arguments themselves can 
     *                break the formatting, but if you want to output a simple
     *                list of elements to another property then just put that 
     *                property directly into the format string (see the 1st 
     *                example given for this tag above).
     */
    private String args = null;

    @Override
    public void execute() throws DTFException {
        String[] arguments = getArgs().split(",");
        Object[] array = new Object[arguments.length];
        
        for (int i = 0; i < arguments.length; i++) { 
            String arg = arguments[i];
            
            if (NumberUtil.isLong(arg)) { 
                array[i] = new Long(arg);
            } else if ( NumberUtil.isDouble(arg) ) { 
                array[i] = new Double(arg);
            } else { 
                array[i] = arg;
            }
        }
       
        try { 
            String result = String.format(getFormat(),array);
            getConfig().setProperty(getProperty(), result);
        } catch (IllegalFormatException e) { 
            throw new ParseException("Syntax error.",e);
        }
    }

    public String getFormat() throws ParseException { return replaceProperties(format); }
    public void setFormat(String format) { this.format = format; }

    public String getProperty() throws ParseException { return replaceProperties(property); }
    public void setProperty(String property) { this.property = property; }

    public String getArgs() throws ParseException { return replaceProperties(args); }
    public void setArgs(String args) { this.args = args; }
}
