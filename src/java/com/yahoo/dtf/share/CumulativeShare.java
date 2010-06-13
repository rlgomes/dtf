package com.yahoo.dtf.share;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.flowcontrol.Sequence;
import com.yahoo.dtf.exception.DTFException;

/**
 * Cumulative share type is used when you want all of the sets to be kept and
 * when you do a get you get all of the sets that were done till that moment 
 * in time. This acts more like a white board where multiple setters can save 
 * information that any getter can come in and get the last few sets that were 
 * done.
 * 
 * @author rlgomes
 */
public class CumulativeShare extends Share {

    public final static String NAME = "cumulative";
    
    private Sequence _sequence = new Sequence();
    
    public CumulativeShare(String id) throws DTFException {
        super(NAME,id);
        _sequence = new Sequence();
    }
  
    @Override
    public synchronized void setAction(Action action) throws DTFException {
        if ( action != null ) {
            _sequence.addAction(action);
        }
    }
   
    @Override
    public synchronized Action getAction() throws DTFException { 
        Sequence sequence = _sequence;
        _sequence = new Sequence();
        return sequence;
    }
}
