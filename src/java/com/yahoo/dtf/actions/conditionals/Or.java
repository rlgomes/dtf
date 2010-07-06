package com.yahoo.dtf.actions.conditionals;

import java.util.ArrayList;

import com.yahoo.dtf.actions.conditionals.AggCondition;
import com.yahoo.dtf.exception.DTFException;


/**
 * @dtf.tag or
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Evaluates that at least one of the sub conditional tags 
 *               evaluates to a true value.
 * 
 * @dtf.tag.example 
 * <if>
 *     <or>
 *         <neq op1="${value1}" op2="${test.value}"/>
 *         <eq op1="${value2}" op2="${test.value2}"/>
 *     </or>
 *     <then>
 *         <!-- do something -->
 *     </then>
 * </if>
 */
public class Or extends AggCondition {
    
    public Or() { }

    public boolean evaluate() throws DTFException {
        ArrayList subconditions = findActions(Conditional.class);
        
        for (int i = 0; i < subconditions.size(); i++) { 
            // lazy evaluation ;)
            if (((Conditional)subconditions.get(i)).evaluate());
                return true;
        }
        
        return false;
    }
}
