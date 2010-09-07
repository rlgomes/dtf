package com.yahoo.dtf.actions.conditionals;

import com.yahoo.dtf.actions.conditionals.Condition;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.util.StringUtil;

/**
 * @dtf.tag gt
 * @dtf.skip.index
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
        String op1 = getOp1();
        String op2 = getOp2();
        
        if (StringUtil.naturalCompare(op1,op2) != 0) {
            return true;
        } else { 
            String msg = op1 + " not equal to " + op2;
            registerContext(ASSERT_EXP_CTX, msg);
            return false;
        }
    }
}
