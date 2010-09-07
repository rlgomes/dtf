package com.yahoo.dtf.actions.flowcontrol;

import com.yahoo.dtf.actions.flowcontrol.Sequence;

/**
 * @dtf.tag finally 
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Used within the try tag. This tag will define a block of XML
 *               that will always be executed independently of there being an 
 *               exception thrown or not.
 * 
 * @dtf.tag.example 
 * <try>
 *     <sequence>
 *        <!-- any sequence of actions -->
 *     </sequence>
 *     <finally>
 *         <echo>
 *         This message will always be printed, no matter what happens
 *         in the previous tags within the Sequence tag.
 *         </echo>
 *     </finally>
 * </try>
 */
public class Finally extends Sequence { 
    public Finally() {}
}
