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
   
    public boolean evaluate() throws DTFException {
        registerContext(ASSERT_EXP_CTX, "always false");
        return false;
    } 
}
