package com.yahoo.dtf.actions.arithmetic;

import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag abs
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Calculates the absolute value of a number, converting all numbers
 *               to a positive value and placing that in the property identified
 *               by the attribute result.
 * 
 * @dtf.tag.example 
 * <abs op1="${prop1}" result="result"/> 
 *
 * @dtf.tag.example 
 * <abs op1="${prop1}" result="prop1"/> 
 */
public class Abs extends ArithmeticOperator { 
   
    public void execute() throws DTFException {
        String op1 = getOp1();
        Double d = Math.abs(Double.valueOf(getOp1()));
        String result = null;
        
        op1 = ( op1 == null ? "" : op1 );
        
        if ( isDouble(op1) ) { 
            result = "" + d;
        } else { 
            result =  "" + d.longValue();
        }
        getConfig().setProperty(getResult(), result);
    }
}
