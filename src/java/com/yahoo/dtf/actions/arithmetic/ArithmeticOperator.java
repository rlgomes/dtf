package com.yahoo.dtf.actions.arithmetic;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.ParseException;

public abstract class ArithmeticOperator extends Action {

    /**
     * @dtf.attr op1
     * @dtf.attr.desc first operand of the arithmetic operation.
     */
    private String op1 = null;
   
    /**
     * @dtf.attr op2
     * @dtf.attr.desc second operand of the arithmetic operation.
     */
    private String op2 = null;
    
    /**
     * @dtf.attr result
     * @dtf.attr.desc property name of where to store the result of the 
     *                arithmetic operation.
     *                 
     */
    private String result  = null;

    protected boolean isDouble(String aux) { 
        return aux.contains(".") || aux.contains("e") || aux.contains("E");
    }

    public String getOp1() throws ParseException { return replaceProperties(op1); }
    public void setOp1(String op1) { this.op1 = op1; }

    public String getOp2() throws ParseException { return replaceProperties(op2); }
    public void setOp2(String op2) { this.op2 = op2; }

    public String getResult() throws ParseException { return replaceProperties(result); }
    public void setResult(String result) { this.result = result; }
}
