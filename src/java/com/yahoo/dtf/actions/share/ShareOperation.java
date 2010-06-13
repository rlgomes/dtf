package com.yahoo.dtf.actions.share;

import java.util.HashMap;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.component.Component;
import com.yahoo.dtf.actions.protocol.CleanUpHook;
import com.yahoo.dtf.actions.protocol.ReleaseAgent;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.share.Share;
import com.yahoo.dtf.share.ShareComponentHook;

public abstract class ShareOperation extends Action {

    public static final String SHARES = "dtf.share.ctx";
    
    /*
     * This way we'll only register this hook if you ever use any of the sync 
     * tags, for the other tests we don't even have to waste time looking for 
     * any sync points if they were never used.
     */
    static {
        Component.registerComponentHook(new ShareComponentHook());
    }

    /**
     * @dtf.attr id 
     * @dtf.attr.desc Unique id for this share point
     *               
     */
    private String id = null;
    
    public final static Object _synclock = new Object();
    public static HashMap<String, Share> getShares() {
        HashMap<String, Share> rs = null;
        synchronized (_synclock) { 
            rs = (HashMap<String, Share>)getGlobalContext(SHARES);
            
            if (rs == null) { 
                rs = new HashMap<String, Share>();
                registerGlobalContext(SHARES, rs);
               
                // register the clean up code for this global context
                ReleaseAgent.addCleanUpHook(new CleanUpHook() { 
                    public void cleanup() throws DTFException {
                        unRegisterGlobalContext(SHARES);
                    }
                });
            }
        }
        return rs;
    }

    public String getId() throws ParseException { return replaceProperties(id); }
    public void setId(String id) { this.id = id; }
}
