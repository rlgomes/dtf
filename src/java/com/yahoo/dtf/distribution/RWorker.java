package com.yahoo.dtf.distribution;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.state.ActionState;
import com.yahoo.dtf.state.DTFState;

/**
 * Worker that is used by the testsuite tag when doing concurrent testscript
 * execution and having a fixed pool size to be used.
 * 
 * @author rlgomes
 */
public class RWorker implements Runnable {

    private static ActionState _as = ActionState.getInstance();
    
    private Action _action = null;
    private String _id = null;
    private DTFState _state = null;
    private DTFException _exception = null;
    
    /**
     * 
     * @param action
     * @param dist
     * @param state
     * @param id
     */
    public RWorker(Action action, String id, DTFState state) {
        _action = action;
        _id = id;
        _state = state;
    }
    
    public void run() {
        try {
            Thread.currentThread().setName(_id);
            _as.setState(_state);
            _action.executeSelf();
        } catch (Throwable t) {
            if (t instanceof DTFException) { 
                _exception = (DTFException) t;
            } else { 
                _exception = new DTFException("Error executing action.",t);
            }
        }
    }
    
    public void checkForException() throws DTFException {
        if (_exception != null) throw _exception;
    }
}
