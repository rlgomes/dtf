package com.yahoo.dtf.rendezvous;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.rendezvous.Rendezvous_reset;
import com.yahoo.dtf.actions.rendezvous.Rendezvous_visit;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.exception.RendezvousException;
import com.yahoo.dtf.logger.DTFLogger;

public class RendezvousPoint {

    private static DTFLogger _logger = DTFLogger.getLogger(RendezvousPoint.class);
    
    private String _id = null;
    private String _cid = null;
    
    private int _parties = 0;
    private int _current = 0;
    
    public RendezvousPoint(String id,
                           int parties,
                           String cid) {
        _cid = cid;
        _id = id;
        _parties = parties;
    }
    
    public boolean isLocal() throws ParseException { 
        // localid is null when you haven't connected to the controller
        return (Action.getLocalID() == null || Action.getLocalID().equals(_cid) || _cid == null );
    }

    public void visit() throws RendezvousException { visit(-1); }
    
    public String getId() { return _id; }
    public int getParties() { return _parties; }
    
    public void visit(long timeout) throws RendezvousException {
        try {
	        if ( !isLocal() )  {
	            if ( _logger.isDebugEnabled() )
	                _logger.debug("Remote rendezvous visit [" +  getId() + 
	                              "] on [" + _cid + "] ");
	
	            Rendezvous_visit visit = new Rendezvous_visit();
	            visit.setId(getId());
	            visit.setTimeout(""+timeout);
	            
	            Action.getComm().sendActionToCaller(_cid, visit).execute();
	        } else { 
	            if ( _logger.isDebugEnabled() )
	                _logger.debug("Local rendezvous visit [" +  getId() + 
	                              "] on [" + _cid + "]");
	            synchronized (this) { 
			        _current++;
			        
			        if ( _current == _parties ) 
			            notifyAll();

			        if ( _current < _parties ) {
			            try {
			                if ( timeout == -1 )
			                    wait();
			                else
			                    wait(timeout);
			            } catch (InterruptedException ignore) { }
			            
	                    if ( _current < _parties ) 
	                        throw new RendezvousException("Timedout on SyncPoint.");
			        }
	            }
	        }
        } catch (DTFException e) {
            throw new RendezvousException("Unable to access remote rendezvous point.",e);
        }
    }

    /**
     * Check if the current SyncPoint has enough visitors with this visitor that
     * is currently checking.
     * 
     * @return
     * @throws RendezvousException 
     */
    public boolean check() throws RendezvousException {
        try {
            if (!isLocal()) {
                if (_logger.isDebugEnabled()) {
                    _logger.debug("Remote rendezvous check [" + getId()
                            + "] on [" + _cid + "] ");
                }

                RendezvousCheck check = new RendezvousCheck();
                check.setId(getId());

                Action result = Action.getComm().sendActionToCaller(_cid, check);
                RemoteResult rr = (RemoteResult) result
                        .findFirstAction(RemoteResult.class);
                assert (rr != null) : "Fatal error no RemoteResults.";
                return rr.getBool();
            } else {
                synchronized (this) {
                    return (_parties <= (_current + 1));
                }
            }
        } catch (DTFException e) {
            throw new RendezvousException(
                    "Unable to access remote rendezvous point.", e);
        }
    }

    /**
     * Reset the _current parties counter and make sure that all currently
     * waiting parties are notified.
     * @throws RendezvousException 
     */
    public void reset() throws RendezvousException {
        try {
            if (!isLocal()) {
                if (_logger.isDebugEnabled()) {
                    _logger.debug("Remote rendezvous reset [" + getId()
                            + "] on [" + _cid + "] ");
                }

                Rendezvous_reset reset = new Rendezvous_reset();
                reset.setId(getId());

                Action.getComm().sendActionToCaller(_cid, reset).execute();
            } else {
                synchronized (this) {
                    _current = 0;
                    notifyAll();
                }
            }
        } catch (DTFException e) {
            throw new RendezvousException(
                    "Unable to access remote rendezvous point.", e);
        }
    }
    
    public String toString() {
        return getClass().getSimpleName() + " [id: " + _id + ", parties: " + 
                                      _parties + ", current: " + _current + "]";
    }
}
