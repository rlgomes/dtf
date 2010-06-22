package com.yahoo.dtf.rendezvous;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.protocol.CleanUpHook;
import com.yahoo.dtf.actions.protocol.Lock;
import com.yahoo.dtf.actions.rendezvous.RendezvousOperation;
import com.yahoo.dtf.actions.rendezvous.Rendezvous_destroy;
import com.yahoo.dtf.comm.Comm;
import com.yahoo.dtf.components.ComponentHook;
import com.yahoo.dtf.components.Components;
import com.yahoo.dtf.exception.DTFException;

public class RendezvousComponentHook implements ComponentHook,
                                                CleanUpHook {

    private static Object _rplock = new Object();
    
    public ArrayList<Action> handleComponent(String id) throws DTFException {
        synchronized (_rplock) { 
            ArrayList<Action> result = new ArrayList<Action>();
            HashMap<String, RendezvousPoint> rps = 
                                      RendezvousOperation.getRendezvousPoints();
           
            synchronized (rps) { 
	            ArrayList<String> sent = getSentRendezvous(id);
	            Iterator<Entry<String,RendezvousPoint>> entries = 
	                                                      rps.entrySet().iterator();
	           
	            ArrayList<String> addme = new ArrayList<String>();
	            while ( entries.hasNext() ) { 
	                Entry<String,RendezvousPoint> entry = entries.next();
	                String key = entry.getKey();
	                
	                if ( !sent.contains(key) ) {
	                    RendezvousPoint rp = entry.getValue();
	                    RendezvousCreate rc = new RendezvousCreate();
	                    if (Action.getLogger().isDebugEnabled()) {
	                        Action.getLogger().debug("Sending [" + rp.getId() + 
	                                                 "] to [" + id + "]");
	                    }
	                    rc.setId(rp.getId());
	                    rc.setParties(""+rp.getParties());
	                    rc.setCid(Action.getLocalID());
	                    result.add(rc);
	                    
	                    addme.add(rp.getId());
	                }
	            }
	            sent.addAll(addme);
	            
	            return result;
	        }
        }
    }

    public static synchronized void createOnAll(String id,
                                                int parties,
                                                String cid)
           throws DTFException {
        synchronized (_rplock) { 
            Components components = Action.getComponents();
            Comm comm = Action.getComm();
    
            if (components == null)
                return;
            
            Iterator<String> cids = components._elems.keySet().iterator();
    
            while (cids.hasNext()) {
                String component = cids.next();
                ArrayList<String> sent = getSentRendezvous(component);
    
                if ( !sent.contains(id) ) {
                    Lock lock = components.getComponent(component);
	                if (Action.getLogger().isDebugEnabled()) {
	                    Action.getLogger().debug("Sending [" + id + "] to [" 
	                                             + component + "]");
	                }
	                
	                RendezvousCreate rc = new RendezvousCreate();
	                rc.setId(id);
	                rc.setParties("" + parties);
	                rc.setCid(cid);
	                comm.sendAction(lock.getId(), rc).execute();
	                // every component has to be labeled as owning this rendezvous
	                // point now
	                sent.add(id);
                 }
            }
        }
    }
    
    public static synchronized void removeRendezvous(String id)
           throws DTFException {
        if ( !DTFNode.getType().equals("dtfx") ) 
            return;

        synchronized (_rplock) { 
	        Components components = Action.getComponents();
	        Comm comm = Action.getComm();
	       
	        Iterator<String> cids = components._elems.keySet().iterator();
	    
	        while ( cids.hasNext() ) { 
	            String cid = cids.next();
	            ArrayList<String> sent = getSentRendezvous(cid);
	            if ( sent.contains(id) ) { 
	                Lock lock = components.getComponent(cid);
	                Rendezvous_destroy rd = new Rendezvous_destroy();
	                rd.setId(id);
	                comm.sendAction(lock.getId(), rd).execute();
	                sent.remove(id);
	            }
	        }
        }
    }
    
    public void cleanup() throws DTFException {
        RendezvousOperation.cleanUp();
    }
    
    /*
     * Keep track of the sent rendezvous points per component
     */
    private static Object _mapLock = new Object();
    
    private static ArrayList<String> getSentRendezvous(String id) { 
        synchronized(_mapLock) { 
            HashMap<String,ArrayList<String>> map = 
                (HashMap<String,ArrayList<String>>)
                           Action.getGlobalContext("getrendezvouspoints." + id);
            
            if (map == null) { 
                map = new HashMap<String, ArrayList<String>>();
                Action.registerGlobalContext("getrendezvouspoints." + id, map);
            }
           
            ArrayList<String> sent = map.get(id);
            if ( sent == null) {
                sent = new ArrayList<String>();
                map.put(id,sent);
            }

            return sent;
        }
    }
}
