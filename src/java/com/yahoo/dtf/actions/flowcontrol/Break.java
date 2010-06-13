package com.yahoo.dtf.actions.flowcontrol;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.BreakException;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag break
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc The break tag can be used to end an iterative (for, while,
 *               etc.) execution and exit from it at any point other than the 
 *               logical end of execution. The break tag only works within the 
 *               execution tags:
 *               <br/>
 *               <br/>
 *               <b>Sequential Tags</b>
 *               <br/>
 *               Within the context of the following sequential tags the break 
 *               tag results in stopping the looping and returning to the point
 *               of execution immediately after the parent tag of the break tag
 *               that is one of the following:
 *               <br/>
 *               <ul>
 *                  <li>{@dtf.link for}</li>
 *                  <li>{@dtf.link while}</li>
 *                  <li>{@dtf.link timer}</li>
 *               </ul>
 *               <br/>
 *               <b>Concurrent Tags</b>
 *               <br/>
 *               Within the context of the following concurrent tags you'll find
 *               that the break will only break the execution of that one thread
 *               that hits the break point. All other threads will continue to 
 *               execute till they hit the break or end execution through a 
 *               different condition.
 *               <br/>
 *               <ul>
 *                  <li>{@dtf.link parallelloop}</li>
 *                  <li>{@dtf.link parallel}</li>
 *               </ul>
 *               <br/>
 *               <br/>
 *               <b>NOTE</b><br/>
 *               Using a break point outside of the context of the previous tags
 *               will result in breaking execution all the way to the root of
 *               your test. The test will still execute any try/finally clauses
 *               but will end execution immediately after that.
 *               
 * @dtf.tag.example 
 * 
 */
public class Break extends Action {

    public Break() {}
    
    public void execute() throws DTFException {
        throw new BreakException("Executing a break.");
    }
}
