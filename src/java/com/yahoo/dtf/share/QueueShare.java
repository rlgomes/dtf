package com.yahoo.dtf.share;

import java.util.ArrayList;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.util.EmptyAction;
import com.yahoo.dtf.exception.DTFException;

/**
 * Queue share type is used when you want to keep all of the sets ever done to a
 * share and be able to retrieve them all in order from that same queue. The
 * queue is useful when you have a bunch of setters that want to give small 
 * work loads for other getters to pick up and work on. 
 * 
 * @author rlgomes
 */
public class QueueShare extends Share {

    public final static String NAME = "queue";

    private ArrayList<Action> _actions = null;
    
    public QueueShare(String id) throws DTFException {
        super(NAME,id);
        _actions = new ArrayList<Action>();
    }
  
    @Override
    public void setAction(Action action) throws DTFException {
        synchronized (_actions) { 
            _actions.add(action);
        }
    }
   
    @Override
    public Action getAction() throws DTFException { 
        synchronized (_actions) { 
	        if ( _actions.size() == 0 ) { 
	            return new EmptyAction();
	        } else { 
	            return _actions.remove(0);
	        }
        }
    }
}
