package com.yahoo.dtf.rendezvous;

import java.util.HashMap;

import com.yahoo.dtf.actions.rendezvous.Rendezvous_create;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.RendezvousException;

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
public class RendezvousCreate extends Rendezvous_create {

    private String cid = null;
    
    @Override
    public void execute() throws DTFException {
        HashMap<String, RendezvousPoint> rs = getRendezvousPoints();
        
        if (rs.containsKey(getId())) 
            throw new RendezvousException("[" + getId() + "] already exists.");
     
        /*
         * Create and store the new rendez-vous point
         */
        rs.put(getId(),new RendezvousPoint(getId(),getParties(),getCid()));
    }
    
    public void setCid(String cid) { this.cid = cid; } 
    public String getCid() { return cid; }

}
