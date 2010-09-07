package com.yahoo.dtf.actions.conditionals;

import com.yahoo.dtf.actions.conditionals.Condition;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.util.StringUtil;

/**
 * @dtf.tag lt
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Evaluates if op1 is less than op2.
 * 
 * @dtf.tag.example 
 * <if>
 *     <lt op1="${iteration}" op2="100"/>
 *     <then>
 *         <log>less than 100</log>
 *     </then>
 * </if>
 */
public class Lt extends Condition {
    public Lt() { }

    public boolean evaluate() throws DTFException {
        String op1 = getOp1();
        String op2 = getOp2();
        
        if (StringUtil.naturalCompare(op1,op2) < 0) {
            return true;
        } else { 
            String msg = op1 + " less than " + op2;
            registerContext(ASSERT_EXP_CTX, msg);
            return false;
        }
    }
}
