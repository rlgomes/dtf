package com.yahoo.dtf.share;

import java.util.HashMap;

import com.yahoo.dtf.actions.share.Share_create;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ShareException;

/**
 * Creates the rendezvous on another agent, this is mainly used by the runner
 * to make sure all of the agents no where any of the rendezvous points are. 
 * The RendezvousPoint code is then smart enough to check if the current 
 * RendezvousPoint is local and if it is not then the agent will talk to the 
 * agent or runner who has this RendezvousPoint.
 * 
 * @author rlgomes
 *
 */
public class ShareCreate extends Share_create {

    private String cid = null;
    
    @Override
    public void execute() throws DTFException {
        HashMap<String, Share> shares = getShares();
        Share sp = shares.get(getId());
        
        if ( sp != null ) {
            throw new ShareException("Share with name [" + getId() + 
                                     "] already exists.");
        }
        
        sp = ShareFactory.getShare(getType(), getId());
        sp.setCid(getCid());
        
        synchronized(shares) { 
            shares.put(getId(),sp);
            shares.notifyAll();
        }
    }
    
    public void setCid(String cid) { this.cid = cid; } 
    public String getCid() { return cid; }

}
