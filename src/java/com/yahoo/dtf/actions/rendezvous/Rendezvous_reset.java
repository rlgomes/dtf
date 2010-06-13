package com.yahoo.dtf.actions.rendezvous;

import java.util.HashMap;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.rendezvous.RendezvousPoint;

/**
 * @dtf.tag rendezvous_reset
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc reset the currently specified rendezvous point back to 0 so 
 *               that there are no longer any parties waiting on this rendezvous
 *               point. Any parties that were waiting should be released and 
 *               continue executing immediately.
 * 
 * @dtf.tag.example 
 * <rendezvous_reset id="rendezvous" />
 *
 */
public class Rendezvous_reset extends RendezvousOperation {
    
    public void execute() throws DTFException {
        HashMap<String, RendezvousPoint> sps = RendezvousOperation.getRendezvousPoints();
        RendezvousPoint sp = sps.get(getId());
        
        if ( sp != null ) { 
            if ( getLogger().isDebugEnabled() )
                getLogger().debug("Resetting [" + getId() + "]");
            
            sp.reset();
        } else { 
            throw new ParseException("[" + getId() + "] does not exist.");
        }
    }

}
