package com.yahoo.dtf.actions.flowcontrol;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.conditionals.Conditional;
import com.yahoo.dtf.exception.BreakException;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.InterruptionException;

/**
 * @dtf.tag while
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc The while tag is just another aggregation tag that allows you 
 *               to run the underlying children tags for as long as the first
 *               conditional tag evaluates to true. Conditional tags include eq,
 *               not,and,or,rendezvous_check,etc.
 * 
 * @dtf.tag.example 
 * <while>
 *      <not>
 *          <rendezvous_check id="myrendezvous1"/>
 *      </not>
 *      <!-- do whatever we want... -->
 * </while>
 *
 * @dtf.tag.example 
 * <while>
 *      <neq op1="${flag}" op2="false"/>
 *      <!-- check the state of flag and upate it -->     
 * </while>
 *
 */
public class While extends Action {

    public void execute() throws DTFException {
        Conditional condition = (Conditional)findFirstAction(Conditional.class);
       
        try { 
	        while (condition.evaluate()) { 
	            executeChildren();
	            checkInterruption();
	        }
        } catch (InterruptionException e) { 
            if ( getLogger().isDebugEnabled() )
                getLogger().debug("execution interrupted.");
        } catch (BreakException e) { 
            // break point
            if ( getLogger().isDebugEnabled() )
                getLogger().debug("break point hit",e);
        }
    }
}
