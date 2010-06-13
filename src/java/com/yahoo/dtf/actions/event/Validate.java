package com.yahoo.dtf.actions.event;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag validate 
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc 
 *               
 * @dtf.tag.example 
 * 
 */
public class Validate extends Action {

    public Validate() { }
    
    public void execute() throws DTFException {
        executeChildren();
    }
}
