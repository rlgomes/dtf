package com.yahoo.dtf.actions.rendezvous;

import java.util.HashMap;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.RendezvousException;
import com.yahoo.dtf.rendezvous.RendezvousComponentHook;
import com.yahoo.dtf.rendezvous.RendezvousPoint;

/**
 * @dtf.tag rendezvous_destroy
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc destroy the specified rendezvous cleaning up any resources.
 * 
 * @dtf.tag.example 
 * <local>
 *      <rendezvous_destroy id="myrendezvous1"/>
 * </local>
 *
 */
public class Rendezvous_destroy extends RendezvousOperation {

    public void execute() throws DTFException {
        HashMap<String, RendezvousPoint> rs = getRendezvousPoints();
        
        if ( !rs.containsKey(getId()) ) 
            throw new RendezvousException("[" + getId() + "] does not exist.");
       
        rs.remove(getId());
        RendezvousComponentHook.removeRendezvous(getId());
    }
}
