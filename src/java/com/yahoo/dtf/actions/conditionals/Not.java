package com.yahoo.dtf.actions.conditionals;

import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag not
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Evaluates the underlying child and returns the inverse value.
 * 
 * @dtf.tag.example 
 * <if>
 *     <not>
 *         <eq op1="${ready}" op2="false"/>
 *     </not>
 *     <then>
 *         <log>not ready yet...</log>
 *     </then>
 * </if>
 * 
 * @dtf.tag.example 
 * <if>
 *     <not>
 *         <and>
 *             <eq op1="${value2}" op2="${test.value2}"/>
 *             <eq op1="${value3}" op2="${test.value3}"/>
 *         </and>
 *     </not>
 *     <then>
 *         <log>Ready to go!</log>
 *     </then>
 * </if>
 */
public class Not extends AggCondition {
    public Not() { }
    
    public boolean evaluate() throws DTFException {
        Conditional condition = (Conditional) findFirstAction(Conditional.class);
        return !condition.evaluate();
    }
}
