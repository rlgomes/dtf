package com.yahoo.dtf.components;

import java.util.ArrayList;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;

/**
 * LockHook can be used to execute at lock time for each component locked during
 * a test. After implementing this interface you should register using the
 * LockComponent.registerLockHook() method to do so. At that point your init()
 * method will be called after every successfully locked component.
 *  
 * @author rlgomes
 */
public interface LockHook {

    /**
     * Initialization code executed right after locking the specified component 
     * with the LockCompnent object you can easily use the Component object to 
     * talk to that component and send any requests over or just keep track of 
     * some data on the runner side. What you must return with this method is 
     * the actions to execute on that component immediately after locking to 
     * validate it is ready to be used or to push some data to the component
     * that is necessary for the test run.
     * 
     * @throws DTFException
     */
    public ArrayList<Action> init(String id) throws DTFException;
}