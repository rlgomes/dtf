package com.yahoo.dtf.actions.rendezvous;

import java.util.HashMap;

import com.yahoo.dtf.actions.conditionals.Conditional;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.rendezvous.RendezvousPoint;

/**
 * @dtf.tag rendezvous_check
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc a conditional tag that can be used in the existing conditional 
 *               situations such as in if, while, switch tags to make a decision
 *               of taking a certain path based on a more complex condition than
 *               equality. This tag validates that the currently specified 
 *               rendezvous point has been visited by all the desired parties, 
 *               including myself as the current visitor.
 *               <br/>
 *               <br/>
 *               Always important to note that if you use this tag to check if
 *               the rendezvous has been visited by all of the parties and it
 *               returns true then you need to use the {@dtf.link rendezvous_visit}
 *               tag to visit the actual rendezvous point and release the 
 *               currently waiting parties. 
 * 
 * @dtf.tag.example 
 * <while>
 *     <not>
 *          <rendezvous_check id="rendezvous" />
 *     </not>
 *     <log>Thread 1 doing its thing...</log>
 * </while>
 *
 * @dtf.tag.example 
 * <if>
 *     <rendezvous_check id="rendezvous" />
 *     <then>
 *          <log>Rendezvous point has been visited by all the parties...</log>
 *          <rendezvous_visit id="rendezvous"/>
 *     </then>
 * </if>
 *
 */
public class Rendezvous_check extends RendezvousOperation implements Conditional {

    public void execute() throws DTFException { }
    
    public boolean evaluate() throws DTFException {
        HashMap<String, RendezvousPoint> sps = RendezvousOperation.getRendezvousPoints();
        RendezvousPoint rp = sps.get(getId());
        
        if ( rp != null ) { 
            return rp.check();
        } else { 
            throw new ParseException("[" + getId() + "] does not exist.");
        }
    }
    
    public String explanation() throws DTFException {
        return "rendezvous point " + getId() + " is ready";
    }
}
