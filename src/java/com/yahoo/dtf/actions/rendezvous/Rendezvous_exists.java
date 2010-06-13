package com.yahoo.dtf.actions.rendezvous;

import java.util.HashMap;

import com.yahoo.dtf.actions.conditionals.Conditional;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.rendezvous.RendezvousPoint;

/**
 * @dtf.tag rendezvous_exists
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc a conditional tag that can be used in the existing conditional 
 *               situations such as in if, while, switch tags to make a decision
 *               of taking a certain path based on a more complex condition than
 *               equality. This tag only validates that the rendezvous point 
 *               with the specified id exists.
 * 
 * @dtf.tag.example 
 * <while>
 *     <not>
 *          <rendezvous_exists id="rendezvous" />
 *     </not>
 *     <sleep time="10ms"/>
 * </while>
 *
 */
public class Rendezvous_exists extends RendezvousOperation implements Conditional {

    public void execute() throws DTFException { }
    
    public boolean evaluate() throws DTFException {
        HashMap<String, RendezvousPoint> sps = RendezvousOperation.getRendezvousPoints();
        RendezvousPoint rp = sps.get(getId());
        
        return rp != null;
    }
    
    public String explanation() throws DTFException {
        return "rendezvous point " + getId() + " exits";
    }
}
