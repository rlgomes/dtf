package com.yahoo.dtf.actions.arithmetic;

import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag add
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Calculates the remainder of division between op1 and op2 and
 *               places the value in the property identified by the result 
 *               attribute.
 * 
 * @dtf.tag.example 
 * <mod op1="${prop1}" op2="10000" result="result"/> 
 *
 * @dtf.tag.example
 * <mod op1="${a}" op2="2" result="odd"/> 
 */
public class Mod extends ArithmeticOperator { 
   
    public void execute() throws DTFException {
        String op1 = getOp1();
        String op2 = getOp2();
        String result = null;
        
        op1 = ( op1 == null ? "" : op1 );
        op2 = ( op2 == null ? "" : op2 );
        
        Double d1 = Double.valueOf(op1);
        Double d2 = Double.valueOf(op2);
        
        if ( isDouble(op1) || isDouble(op2) ) { 
            result = "" + (d1%d2);
        } else { 
            result = "" + new Double(d1%d2).longValue();
        }
        
        getConfig().setProperty(getResult(), result);
    }
}
