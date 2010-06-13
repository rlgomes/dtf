package com.yahoo.dtf.distribution;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.components.StateComponentHook;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.state.ActionState;
import com.yahoo.dtf.state.DTFState;

public class Worker extends Thread {

    private Action _action = null;
    private DTFState _state = null;
    private DTFException _exception = null;
    
    private String _id = null;
    
    /**
     * 
     * @param action
     * @param dist
     * @param state
     * @param id
     */
    public Worker(Action action,
                  DTFState state,
                  String id) {
        _action = action;
        _state = state;
        _id = id;
    }
    
    public String getWorkerId() { return _id; }
    
    public void run() {
        // set my state nicely
        ActionState.getInstance().setState(getName(), _state);
        ThreadMgr.registerThread(this);
        try {
            _action.execute();
        } catch (Throwable t) {
            if (t instanceof DTFException) { 
                _exception = (DTFException) t;
            } else { 
                _exception = new DTFException("Error executing action.",t);
            }
        } finally { 
        	ActionState.getInstance().delState(getName());
        	StateComponentHook.threadDead(getName());
        	ThreadMgr.unregisterThread(this);
        } 
    }
    
    public void waitFor() throws DTFException {
        try {
            join();
        } catch (InterruptedException e) {
            throw new DTFException("Interrupted.",e);
        } 
        
        if (_exception != null)
            throw _exception;
    }
}
