package com.yahoo.dtf.components;

import java.util.ArrayList;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;

/**
 * 
 * @author rlgomes
 */
public interface ComponentReturnHook {

    /**
     * 
     * @param id
     * @return
     */
    public ArrayList<Action> returnToRunner(String id) throws DTFException;
}
