package com.yahoo.dtf.actions.util;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;

/**
 * EmtpyAction is used when you need to have an action that does absolutely 
 * nothing, including not even executing any underlying children.
 * 
 * @author rlgomes
 */
public class EmptyAction extends Action {
    @Override
    public void execute() throws DTFException { /* NOP */ }
}
