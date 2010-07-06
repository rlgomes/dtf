package com.yahoo.dtf.actions.conditionals;

import com.yahoo.dtf.actions.conditionals.Condition;
import com.yahoo.dtf.exception.DTFException;

/**
 * Used internally to represent the condition of always true.
 * 
 * @author Rodney Gomes
 *
 */
public class True extends Condition {
    public True() { }
    public boolean evaluate() throws DTFException { return true; } 
}
