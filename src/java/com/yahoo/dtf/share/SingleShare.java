package com.yahoo.dtf.share;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.flowcontrol.Sequence;
import com.yahoo.dtf.exception.DTFException;

/**
 * Single share type can only hold the last set request and therefore acts more
 * like a variable that is updated with the latest and greatest data on set. 
 * Any getter can easily come in and have their local data updated with the 
 * actions stored at this share point. Very useful for keeping some state in 
 * sync between multiple components in DTF.
 * 
 * @author rlgomes
 */
public class SingleShare extends Share {

    public final static String NAME = "single";
    
    // start with empty sequence that does nothing
    private Action _action = null;
    
    public SingleShare(String id) throws DTFException {
        super(NAME,id);
        _action = new Sequence();
    }
  
    @Override
    public void setAction(Action action) throws DTFException {
        if ( action != null )
            _action = action;
    }
   
    @Override
    public Action getAction() throws DTFException { 
        return _action;
    }
}
