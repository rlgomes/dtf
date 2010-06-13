package com.yahoo.dtf.components;

import java.util.ArrayList;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;

/**
 * Hook used to send certain actions to the agent side at communication time 
 * with the component. This code will be executed on every single component
 * tag execution, so be aware that you can effect the performance of calling to 
 * these components quite significantly.
 * 
 * @author rlgomes
 */
public interface ComponentHook {

    /**
     * For a specified component return the necessary actions you'd like to 
     * execute on the agent before proceeding with normal execution.
     * 
     * @param id
     * @return
     */
    public ArrayList<Action> handleComponent(String id) throws DTFException;
}
