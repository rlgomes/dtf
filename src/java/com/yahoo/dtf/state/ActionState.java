package com.yahoo.dtf.state;

import java.util.Enumeration;
import java.util.Hashtable;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.logger.DTFLogger;
import com.yahoo.dtf.state.ActionState;
import com.yahoo.dtf.state.DTFState;
import com.yahoo.dtf.DTFConstants;

public class ActionState {

    private static ActionState _instance = new ActionState();
    
    private Hashtable<String,DTFState> _states = null;
   
    private ActionState() {
        _states = new Hashtable<String, DTFState>();
    }
    
    public static ActionState getInstance() {
        return _instance;
    }
   
    public String getCurrentID() { 
        return Thread.currentThread().getName();
    }
        
    public DTFState getState() {
       return getState(getCurrentID());
    }
    
    public Enumeration<String> getStates() { 
        return _states.keys();
    }
    
    public boolean hasState(String id) { 
        return _states.containsKey(id);
    }

    public DTFState getState(String id) {
       DTFState state = (DTFState)_states.get(id); 

       if (state == null) 
           return (DTFState)_states.get(DTFConstants.MAIN_THREAD_NAME);
       else 
           return state;
    }
    
    public void setState(DTFState state) { setState(getCurrentID(),state); }
    public void setState(String key, DTFState state) {
        _states.put(key, state);
    }
    
    public void delState() { delState(getCurrentID()); }
    public void delState(String key) {
        DTFState state = ((DTFState)_states.remove(key));
        if ( state == null ) { 
            Action.getLogger().warn("Attempted to delete a non existent state.",
                                    new Throwable());
        }
    }
}
