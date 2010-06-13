package com.yahoo.dtf.actions.flowcontrol;

import com.yahoo.dtf.actions.flowcontrol.Else;
import com.yahoo.dtf.actions.flowcontrol.Then;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.conditionals.Conditional;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag then
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc The if tag will execute the then tag if the condition is true
 *               and if the condition evaluates to false then the else sub tag
 *               will evaluate to true. 
 *               
 * @dtf.tag.example
 * <if>
 *     <eq op1="${test1}" op2="${test2}"/>
 *     <then>
 *         <local>
 *             <echo>${test1} equals ${test2}</echo>
 *         </local>
 *     </then> 
 *     <else>
 *         <local>
 *             <fail message="CRAP!"/>
 *         </local>
 *     </else>
 * </if> 
 * 
 * @dtf.tag.example
 * <if>
 *     <neq op1="${test1}" op2="${test2}"/>
 *     <then>
 *         <local>
 *             <echo>${test1} is not equals ${test2}</echo>
 *         </local>
 *     </then> 
 * </if> 
 * 
 */
public class If extends Action {
    
    public If() {}
    
    public void execute() throws DTFException {
        Conditional condition = (Conditional)findFirstAction(Conditional.class);
        
        if (condition.evaluate()) { 
            Then then = ((Then)findFirstAction(Then.class));
            
            if (then != null)
                then.execute();
        } else { 
            Else elseChild = ((Else)findFirstAction(Else.class));
            if (elseChild != null)
                elseChild.execute();
        }
    }
}
