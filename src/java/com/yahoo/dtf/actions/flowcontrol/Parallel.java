package com.yahoo.dtf.actions.flowcontrol;

import java.util.ArrayList;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.distribution.Worker;
import com.yahoo.dtf.exception.BreakException;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.InterruptionException;
import com.yahoo.dtf.state.DTFState;

/**
 * @dtf.tag parallel
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc All of the direct children of this tag are executed in 
 *               parallel, this tag only terminates its execution once all of 
 *               the children have completed their executions.
 * 
 * @dtf.tag.example 
 * <parallel>
 *     <component id="DTFA1">
 *         <echo>Echo a</echo>
 *     </component>
 *     <component id="DTFA2">
 *         <echo>Echo b</echo>
 *     </component>
 * </parallel>
 * 
 * @dtf.tag.example 
 * <parallel>
 *     <for property="i" range="1..100">
 *         <echo>Echo first thread ${i}</echo>
 *     </for>
 *     <for property="i" range="1..100">
 *         <echo>Echo second thread ${i}</echo>
 *     </for>
 * </parallel>
 *
 */
public class Parallel extends Action {

    public Parallel() { }

    public void execute() throws DTFException {
        ArrayList<Action> children = children();
        Worker[] actions = new Worker[children.size()];

        for (int index = 0; index < actions.length; index++) {
            DTFState state = getState().duplicate();
            actions[index] = new Worker((Action) children.get(index),
                                        state,
                                        ""+index);
        }
        
        for (int index = 0; index < actions.length; index++) 
            actions[index].start();
      
        /*
         * Wait for all underlying threads to terminate and log all of them 
         * except the last exception which we can throw to any parent tag to 
         * handle the error.
         */
        DTFException lastexception = null;
        boolean interrupted = false;
        for (int index = 0; index < actions.length; index++) {
            try { 
                actions[index].waitFor();
            } catch (InterruptionException e) { 
                if ( getLogger().isDebugEnabled() ) {
                    getLogger().debug("Thread interrupted [" + 
                                      actions[index].getName() + "]",e);
                }
                interrupted = true;
            } catch (BreakException e) { 
                // break point
                if ( getLogger().isDebugEnabled() )
                    getLogger().debug("break point hit",e);
            } catch (DTFException e) { 
                if ( lastexception != null ) 
                    getLogger().error("Child failed.", lastexception);
                
                lastexception = e;
            }
        }

        if ( interrupted && getLogger().isDebugEnabled() ) 
            getLogger().debug("Execution interrupted.");
        
        if ( lastexception != null ) 
            throw lastexception;
    }
}
