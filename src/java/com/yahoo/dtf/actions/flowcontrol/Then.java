package com.yahoo.dtf.actions.flowcontrol;

import com.yahoo.dtf.actions.flowcontrol.Sequence;

/**
 * @dtf.tag then
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc The then clause of an if tag.
 *
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
 */
public class Then extends Sequence {
    public Then() {}
}
