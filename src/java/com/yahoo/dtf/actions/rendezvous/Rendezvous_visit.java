package com.yahoo.dtf.actions.rendezvous;

import java.util.HashMap;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.rendezvous.RendezvousPoint;
import com.yahoo.dtf.util.TimeUtil;

/**
 * @dtf.tag rendezvous_visit
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Visit the rendezvous point and waiting till all of the parties 
 *               that were designated to synchronize on this point have arrived.
 *               This tag also includes a timeout so you can timeout and abort
 *               the waiting because you have a limit on how long any of the 
 *               other parties can take to show up.
 * 
 * @dtf.tag.example 
 * <rendezvous_visit id="rendezvous" />
 *
 * @dtf.tag.example 
 * <rendezvous_visit id="rendezvous" timeout="2h"/>
 *
 */
public class Rendezvous_visit extends RendezvousOperation {
   
    /**
     * @dtf.attr timeout
     * @dtf.attr.desc the maximum amount of time to wait for all parties to 
     *                visit the rendezvous point. If this time limit is exceeded
     *                then a RendezvousException is thrown to notify that the
     *                timeout was hit.
     */
    private String timeout = null;

    public void execute() throws DTFException {
        HashMap<String, RendezvousPoint> rps = RendezvousOperation.getRendezvousPoints();
        String id = getId();
        long timeout = getTimeout();

        RendezvousPoint rp = rps.get(id);
        if ( rp != null ) { 
            rp.visit(timeout);
        } else { 
            throw new ParseException("[" + id + "] does not exist.");
        }
    }

    public long getTimeout() throws ParseException { 
        return TimeUtil.parseTime("timeout",timeout);
    }
    public void setTimeout(String timeout) { this.timeout = timeout; }
}
