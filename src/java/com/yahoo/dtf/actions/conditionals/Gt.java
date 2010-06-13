package com.yahoo.dtf.actions.conditionals;

import com.yahoo.dtf.actions.conditionals.Condition;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.util.StringUtil;

/**
 * @dtf.tag gt
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Evaluates if op1 is greater than op2.
 * 
 * @dtf.tag.example 
 * <if>
 *     <gt op1="${iteration}" op2="100"/>
 *     <then>
 *         <log>Iterations over 100!</log>
 *     </then>
 * </if>
 */
public class Gt extends Condition {
    public Gt() { }
    
    public boolean evaluate() throws DTFException {
        return (StringUtil.naturalCompare(getOp1(),getOp2()) > 0);
    }
    
    public String explanation() throws DTFException {
        return getOp1() + " greater than " + getOp2();
    }
}
