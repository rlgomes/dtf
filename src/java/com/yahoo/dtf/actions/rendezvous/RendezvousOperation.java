package com.yahoo.dtf.actions.rendezvous;

import java.util.HashMap;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.component.Component;
import com.yahoo.dtf.actions.protocol.ReleaseAgent;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.rendezvous.RendezvousComponentHook;
import com.yahoo.dtf.rendezvous.RendezvousPoint;

public abstract class RendezvousOperation extends Action {

    public static final String RENDEZVOUS_CTX = "dtf.rendezvous.ctx";
   
    /*
     * This way we'll only register this hook if you ever use any of the sync 
     * tags, for the other tests we don't even have to waste time looking for 
     * any sync points if they were never used.
     */
    static {
        RendezvousComponentHook hook = new RendezvousComponentHook();
        Component.registerComponentHook(hook);
        ReleaseAgent.addCleanUpHook(hook);
    }
    
    public final static Object _rendevousLock = new Object();
    /**
     * @dtf.attr id 
     * @dtf.attr.desc the unique identifier used to reference this particular
     *                rendezvous point within a DTF testcase.
     */
    private String id = null;
    
    public static HashMap<String, RendezvousPoint> getRendezvousPoints() {
        HashMap<String, RendezvousPoint> rs = null;
        synchronized (_rendevousLock) { 
            rs = (HashMap<String, RendezvousPoint>)getGlobalContext(RENDEZVOUS_CTX);
            
            if (rs == null) { 
                rs = new HashMap<String, RendezvousPoint>();
                registerGlobalContext(RENDEZVOUS_CTX, rs);
            }
        }
        return rs;
    }
    
    public static void cleanUp() { 
        unRegisterGlobalContext(RENDEZVOUS_CTX);
    }

    public String getId() throws ParseException { return replaceProperties(id); }
    public void setId(String id) { this.id = id; }
}
