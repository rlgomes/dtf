package com.yahoo.dtf.distribution;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.flowcontrol.Sequence;
import com.yahoo.dtf.components.StateComponentHook;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.InterruptionException;
import com.yahoo.dtf.state.ActionState;
import com.yahoo.dtf.state.DTFState;

public class DistWorker extends Thread {

    private Action _action = null;
    private DTFState _state = null;
    private DistWorkState _dist = null;
    private DTFException _exception = null;
    
    /**
     * 
     * @param action
     * @param dist
     * @param state
     * @param id
     */
    public DistWorker(Action action,
                      DistWorkState dist,
                      DTFState state) { 
        _action = action;
        _state = state;
        _dist = dist;
    }
    
    public void run() {
    	 // set my state nicely
        ActionState.getInstance().setState(getName(), _state);
        ThreadMgr.registerThread(this);
        try { 
            Sequence sequence = null;
            do { 
	            try {
	                sequence = _dist.waitForWork();
	                if ( sequence != null ) { 
	                    sequence.execute();
	                    _action.execute();
	                }
	            } catch (InterruptionException e) {
	                _exception = e;
	                // terminate the distribution right now
	                _dist.allDone();
	                break;
		        } catch (DTFException e) {
	                _exception = (DTFException) e;
		        }
            } while (sequence != null);
        } catch (Throwable t) { 
            _exception = new DTFException("Unable to execution action.",t);
            // terminate the distribution right now
            _dist.allDone();
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
