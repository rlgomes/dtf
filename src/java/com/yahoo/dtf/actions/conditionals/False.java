package com.yahoo.dtf.actions.conditionals;

import com.yahoo.dtf.actions.conditionals.Condition;
import com.yahoo.dtf.exception.DTFException;

/**
 * Used internally to represent the condition of always false.
 * 
 * @author Rodney Gomes
 *
 */
public class False extends Condition {
    public False() { }
    public boolean evaluate() throws DTFException { return false; } 
    
    public String explanation() throws DTFException {
        return "always false";
    }
}
