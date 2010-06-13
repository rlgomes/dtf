package com.yahoo.dtf.share;

import java.util.concurrent.Semaphore;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.share.Share_set;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.logger.DTFLogger;

public abstract class Share {

    private static DTFLogger _logger = DTFLogger.getLogger(Share.class);
    
    private String _id = null;
    private String _type = null;
    private String _cid = null;
    
    private Semaphore _semaphore = null;
    
    public Share(String type, String id) throws DTFException {
        _id = id;
        _type = type;
        _cid = Action.getLocalID();
        _semaphore = new Semaphore(0);
    }
    
    public void setCid(String cid) { _cid = cid; }
    
    public boolean isLocal() throws ParseException { 
        // localid is null when you haven't connected to the controller
        return (Action.getLocalID() == null || Action.getLocalID().equals(_cid));
    }
   
    /**
     * Set the action to the share and keep it till the getAction has been 
     * called.
     * 
     * @param action
     * @throws DTFException
     */
    public abstract void setAction(Action action) throws DTFException;
    
    /**
     * 
     * @return
     * @throws DTFException
     */
    public abstract Action getAction() throws DTFException;
   
    /**
     * 
     * @param action
     * @throws DTFException
     */
    public void set(Action action) throws DTFException {
        /*
         * This duplication allows us to replace any properties that are 
         * necessary right now at set time where those properties would exist.
         * 
         * Its costly to do this but there's really no other easy way of making
         * sure that when we set some Actions for someone else to get that we 
         * have resolved all of the properties of those actions and have a copy
         * of them that is thread safe.
         */
        action = action.duplicate();
        
        if ( !isLocal() ) {
            if ( _logger.isDebugEnabled() )
	            _logger.debug("Setting share [" + getId() + "]");
            
            Share_set ss = new Share_set();
            ss.setId(getId());
            ss.addAction(action);
            Action.getComm().sendActionToCaller(_cid, ss).execute();
        } else { 
            setAction(action);
            _semaphore.release();
        }
    }
   
    /**
     *  
     * @return
     * @throws DTFException
     */
    public Action get(boolean blocking) throws DTFException { 
        if ( !isLocal() ) {
            if ( _logger.isDebugEnabled() )
	            _logger.debug("Getting data from share [" +  getId() + 
	                          "] on [" + _cid + "] ");
           
            SharePointGet spw = new SharePointGet();
            spw.setId(getId());
            spw.setBlocking(""+blocking);
           
            Action.getState().enableReplace();
            return Action.getComm().sendActionToCaller(_cid, spw);
        } else { 
            if ( blocking ) {
                try {
                    _semaphore.acquire();
                } catch (InterruptedException e) {
                    throw new DTFException("Unable to wait for action.");
                }
		        return getAction();
	        } else { 
	            return getAction();
	        }
        }
    }
    
    public void setType(String type) { _type = type; }
    public String getType() { return _type; }
    
    public void setId(String id) { _id = id; }
    public String getId() { return _id; } 
}
