package com.yahoo.dtf.actions.flowcontrol;

import com.yahoo.dtf.actions.flowcontrol.Sequence;

/**
 * @dtf.tag else
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc The else clause of an if tag.
 * 
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
 */
public class Else extends Sequence {
    public Else() {}
}
