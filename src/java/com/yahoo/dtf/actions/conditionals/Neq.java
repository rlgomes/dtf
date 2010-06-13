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
 * @dtf.tag.desc Evaluates if op1 and op2 are different.
 * 
 * @dtf.tag.example 
 * <if>
 *     <neq op1="${responsetime}" op2="50"/>
 *     <then>
 *         <log>response time still under 50ms</log>
 *     </then>
 * </if>
 */
public class Neq extends Condition {
    public Neq() { }
    
    public boolean evaluate() throws DTFException {
        return (StringUtil.naturalCompare(getOp1(), getOp2()) != 0);
    }
    
    public String explanation() throws DTFException {
        return getOp1() + " not equal to " + getOp2();
    }
}
