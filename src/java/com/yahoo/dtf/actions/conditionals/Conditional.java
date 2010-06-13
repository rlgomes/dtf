package com.yahoo.dtf.actions.conditionals;

import com.yahoo.dtf.exception.DTFException;

public interface Conditional {
    /**
     * Returns this conditionals logical value for the current state.
     * 
     * @return
     * @throws DTFException
     */
    public boolean evaluate() throws DTFException;
    
    /**
     * Returns a string which represents in plain English what this condition
     * is attempting to validate.
     * 
     * @return
     */
    public String explanation() throws DTFException;
}
