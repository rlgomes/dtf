package com.yahoo.dtf.actions.conditionals;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.util.StringUtil;

/**
 * @dtf.tag eq
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Evaluates the equality of the two operands that are identified 
 *               by the attributes op1 and op2.
 * 
 * @dtf.tag.example 
 * <if>
 *     <eq op1="${iteration}" op2="0"/>
 *     <then>
 *         <log>Iterations at 0!</log>
 *     </then>
 * </if>
 */
public class Eq extends Condition {
    public Eq() { }
    
    public boolean evaluate() throws DTFException {
        return (StringUtil.naturalCompare(getOp1(), getOp2()) == 0);
    }
    
    public String explanation() throws DTFException {
        return getOp1() + " equal to " + getOp2();
    }
}