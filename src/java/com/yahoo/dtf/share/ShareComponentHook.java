package com.yahoo.dtf.share;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.protocol.Lock;
import com.yahoo.dtf.actions.share.ShareOperation;
import com.yahoo.dtf.actions.share.Share_destroy;
import com.yahoo.dtf.comm.Comm;
import com.yahoo.dtf.components.ComponentHook;
import com.yahoo.dtf.components.Components;
import com.yahoo.dtf.exception.DTFException;

public class ShareComponentHook implements ComponentHook {

    public synchronized ArrayList<Action> handleComponent(String id) throws DTFException {
        ArrayList<Action> result = new ArrayList<Action>();
        HashMap<String, Share> sps = ShareOperation.getShares();
        ArrayList<String> sent = getSentShares(id);
       
        Iterator<Entry<String, Share>> entries = sps.entrySet().iterator();

        ArrayList<String> addme = new ArrayList<String>();
        while ( entries.hasNext() ) { 
            Entry<String,Share> entry = entries.next();
            String key = entry.getKey();
            if ( !sent.contains(key) ) {
                Share share = entry.getValue();
                ShareCreate sc = new ShareCreate();
                sc.setId(key);
                sc.setType(share.getType());
                sc.setCid(Action.getLocalID());
                result.add(sc);
                addme.add(key);
            }
        }
        sent.addAll(addme);

        return result;
    }
    
    public static void removeShare(String id) throws DTFException {
        Components components = Action.getComponents();
        Comm comm = Action.getComm();
       
        if ( components == null ) return;
        
        Iterator<String> cids = components._elems.keySet().iterator();
    
        while ( cids.hasNext() ) { 
            String cid = cids.next();
            ArrayList<String> sent = getSentShares(cid);
            if ( sent.contains(id) ) { 
                Lock lock = components.getComponent(cid);
                Share_destroy sd = new Share_destroy();
                sd.setId(id);
                comm.sendActionToCaller(lock.getId(), sd).execute();
                sent.remove(id);
            }
        }
    }
    
    public static void createOnAll(String id, String type, String cid) throws DTFException {
        Components components = Action.getComponents();
        Comm comm = Action.getComm();
       
        if ( components == null ) return;
        
        Iterator<String> cids = components._elems.keySet().iterator();
    
        while ( cids.hasNext() ) { 
            String component = cids.next();
            ArrayList<String> sent = getSentShares(component);
            Lock lock = components.getComponent(component);
          
            // don't recreate the share on the same component it came from
            if ( !lock.getId().equals(cid) ) { 
                if ( Action.getLogger().isDebugEnabled() ) { 
	                Action.getLogger().debug("Sending share [" + id + "] to [" 
	                                         + lock.getId() + "] from [" + 
	                                         cid + "]");
                }
                ShareCreate sc = new ShareCreate();
                sc.setId(id);
                sc.setType(type);
                sc.setCid(cid);
                comm.executeOnComponent(component, sc);
            }
            
            // make sure all components are known to have this share
            sent.add(id);
        }
    }
    
    /*
     * Keep track of the sent sync points per component
     */
    private static Object _mapLock = new Object();
    
    private static ArrayList<String> getSentShares(String id) { 
        synchronized(_mapLock) { 
            HashMap<String,ArrayList<String>> map = 
                (HashMap<String,ArrayList<String>>)
                                Action.getGlobalContext("sentsyncpoints." + id);
            
            if (map == null) { 
                map = new HashMap<String, ArrayList<String>>();
                Action.registerGlobalContext("sentsyncpoints." + id, map);
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
