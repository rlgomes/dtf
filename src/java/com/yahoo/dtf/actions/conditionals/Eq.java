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
        String op1 = getOp1();
        String op2 = getOp2();
        
        if (StringUtil.naturalCompare(op1, op2) == 0) {
            return true;
        } else { 
            String msg = op1 + " equal to " + op2;
            registerContext(ASSERT_EXP_CTX, msg);
            return false;
        }
    }
}
