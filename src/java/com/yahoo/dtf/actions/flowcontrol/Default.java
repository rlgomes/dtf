package com.yahoo.dtf.actions.flowcontrol;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag default
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc default execution path to take when none of the cases in the
 *               switch tag match the property from the switch tag. 
 *                
 * @dtf.tag.example 
 * <switch property="${test}">
 *     <case value="1">
 *          <log>Switch case #1</log>
 *     </case> 
 *     <case value="2">
 *         <log>Switch case #2</log>
 *     </case>
 *     <default>
 *         <log>Default case called</log>
 *     </default>         
 * </switch> 
 */
public class Default extends Action {

    public Default() {}
    
    public void execute() throws DTFException {
        executeChildren();
    }
}
