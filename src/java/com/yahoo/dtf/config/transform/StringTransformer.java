package com.yahoo.dtf.config.transform;

import com.yahoo.dtf.config.transform.Transformer;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.feature String Manipulation
 *
 * @dtf.feature.group Transformers
 * 
 * @dtf.feature.desc <p>
 *                   This is the DTF Transformer and it can be used to apply 
 *                   string functions to the properties to apply certain types 
 *                   of encoding to the property values.
 *                   </p>
 *                   <p>
 *                   <b>length</b><br/>
 *                   This function will calculate the length of the properties 
 *                   value.
 *                   </p>
 *                   <p>
 *                   <b>sub-string(a,b)</b><br/>
 *                   This function can extract a sub string from an existing 
 *                   property value.
 *                   </p>
 *                   <p>
 *                   <b>index-of(str)</b><br/>
 *                   This function will find the index of an existing string 
 *                   within the property's value.
 *                   </p>
 *                   
 * @dtf.example
 * <sequence>
 *     <property name="string1" value="Hello World!"/>
 *     <log>Length of [${string1}] is ${string1:string:length}</log> 
 * </sequence>
 *  
 * @dtf.example
 * <sequence>
 *     <property name="string2" value="rodney lopes gomes"/>
 *     <log>middle name is "${string2:string:sub-string(7,12)}"</log> 
 * </sequence>
 * 
 * @dtf.example
 * <sequence>
 *     <property name="string1" value="Hello World!"/>
 *     <log>index of 'W' in "${string1}" is ${string1:string:index-of(W)}</log> 
 * </sequence>
 */
public class StringTransformer implements Transformer {

    public String apply(String data, String expression) throws ParseException {
        int bracketIndex = expression.indexOf('(');
        String operator = null;
        String arguments = null;
        String[] args = null;
        
        if ( bracketIndex != -1 ) {
            operator = expression.substring(0, bracketIndex);
            arguments = expression.substring(bracketIndex + 1,
                                             expression.length() - 1);
            
            args = arguments.split(",");
        } else { 
            operator = expression;
        }

        if ( operator.equals("length") ) {
            return "" + data.length();
        }

        if ( operator.equals("sub-string") ) {
            int start = Integer.valueOf(args[0]); 
            int stop  = Integer.valueOf(args[1]); 
            return "" + data.substring(start, stop);
        }
        
        if ( operator.equals("index-of") ) {
            if ( args.length != 1 ) { 
                throw new ParseException("index-of has only 1 argument not [" + 
                                         arguments + "]");
            }
            return "" + data.indexOf(args[0]);
        }
        
        throw new ParseException("Unkown string expression [" + expression + "]");
    }
}
