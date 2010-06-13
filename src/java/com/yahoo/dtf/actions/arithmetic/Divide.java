package com.yahoo.dtf.actions.arithmetic;

import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag divide
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Divide op1 by op2 and store the result in the property
 *               identified by the attribute result. Dividing by zero will 
 *               generate an ArithmeticException that can easily be caught in 
 *               your DTF test using the {@dtf.link try} and ${@dtf.link catch}
 *               tags.
 * 
 * @dtf.tag.example 
 * <divide op1="100" op2="${proerty1}" result="result"/> 
 *
 * @dtf.tag.example 
 * <divide op1="${a}" op2="1000" result="a"/> 
 */
public class Divide extends ArithmeticOperator { 
    public void execute() throws DTFException {
        try { 
            String op1 = getOp1();
            String op2 = getOp2();
            String result = null;
            
            op1 = ( op1 == null ? "" : op1 );
            op2 = ( op2 == null ? "" : op2 );
            
            Double d1 = Double.valueOf(op1);
            Double d2 = Double.valueOf(op2);
            
            if ( isDouble(op1) || isDouble(op2) ) { 
                result = "" + (d1/d2);
            } else { 
                result = "" + new Double(d1/d2).longValue();
            }
            
            getConfig().setProperty(getResult(), result);
        } catch (ArithmeticException e) { 
            throw new DTFException("Error dividing " + getOp1() + " by " + getOp2(),e);
        }
    }
}
