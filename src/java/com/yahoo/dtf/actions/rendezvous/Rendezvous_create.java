package com.yahoo.dtf.actions.rendezvous;

import java.util.HashMap;

import com.yahoo.dtf.DTFConstants;
import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.rendezvous.RendezvousComponentHook;
import com.yahoo.dtf.rendezvous.RendezvousPoint;

/**
 * @dtf.tag rendezvous_create
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc create a rendezvous point for a number of parties to 
 *               synchronize their execution at. Rendezvous points are used to
 *               synchronize behavior between multiple threads. They allow for
 *               a pretty straightforward manner of being able to say that a 
 *               number of predefined parties are to meet at this point and 
 *               continue from it once their all there.
 *               <br/>
 *               <br/>
 *               Rendezvous points work in a local or distributed fashion 
 *               depending on where they were created and where you are acessing
 *               them from. So the rendezvous point is local to the component 
 *               where it was created but it is remote and therefore works in 
 *               a distributed way when accessed from a different component
 *               (runner or agent).
 * 
 * @dtf.tag.example 
 * <rendezvous_create id="myrendezvous1" parties="3"/>
 *
 * @dtf.tag.example 
 * <rendezvous_create id="myrendezvous2" parties="50"/>
 *
 */
public class Rendezvous_create extends RendezvousOperation {
 
    /**
     * @dtf.attr parties
     * @dtf.attr.desc the number of visiting parties to this rendezvous point. 
     */
    private String parties = null;
    
    public void execute() throws DTFException {
        HashMap<String, RendezvousPoint> rs = getRendezvousPoints();
        
        if (rs.containsKey(getId())) 
            throw new ParseException("[" + getId() + "] already exists.");
       

        if ( !DTFNode.getType().equals(DTFConstants.DTFX_ID) ) {
            /*
             * Make sure notification/creation of this rendezvous occurs on all
             * other components.
             */
            if ( getLogger().isDebugEnabled() ) {
                getLogger().debug("Sending rendezvous [" + getId() + 
                                  "] to runner.");
            }

            String owner = DTFNode.getOwner().getOwner();
            getComm().sendActionToCaller(owner, this).execute(); 
        } else { 
	        /*
	         * Create and store the new rendez-vous point
	         */
           
            synchronized(rs) { 
		        rs.put(getId(), new RendezvousPoint(getId(),
		                                            getParties(),
		                                            getLocalID()));
	        }
	        
            /*
             * Any locked components should just receive the creation of this
             * rendezvous point automatically. This just solves a few problems 
             * with accessing the same component from different threads on the
             * runner side in parallel which can sometimes originate one of the
             * threads going through and finding that there is no such 
             * rendezvous point created on that component side.
             */
            RendezvousComponentHook.createOnAll(getId(),
                                                getParties(),
                                                Action.getLocalID());
        }
    }

    public int getParties() throws ParseException { return toInt("parties",parties); }
    public void setParties(String parties) { this.parties = parties; }
}
