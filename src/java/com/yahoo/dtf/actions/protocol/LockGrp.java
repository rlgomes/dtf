package com.yahoo.dtf.actions.protocol;

import java.util.ArrayList;

import com.yahoo.dtf.actions.protocol.Connect;
import com.yahoo.dtf.DTFProperties;
import com.yahoo.dtf.NodeInfo;
import com.yahoo.dtf.NodeState;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.LockException;
import com.yahoo.dtf.util.ThreadUtil;

/**
 * Protocol action used to try and obtain the lockgroup on the DTFC. If one of 
 * the specified Locks can not be obtained then all of the Locks are released
 * and retried within a few seconds.
 * 
 * @author rlgomes
 */
public class LockGrp extends Connect {

    private String owner = null;

    private String name = null;
   
    /*
     * Used for direct connection between executer and agents.
     */
    private String address = null;
    
    private long timeout = -1;
    
    public LockGrp() { }
    
    public LockGrp(String id, String refid, long timeout) 
           throws DTFException{
        setId(id);
        setOwner(Action.getLocalID());
        setAddress(getConfig().getProperty(DTFProperties.DTF_LISTEN_ADDR));
        setPort(getConfig().getPropertyAsInt(DTFProperties.DTF_LISTEN_PORT));
        setName(refid);
        setTimeout(timeout);
    }
    
    public void execute() throws DTFException {
        ArrayList<Lock> locks = findAllActions(Lock.class);
        NodeInfo[] niinfos = null;

        if (getLogger().isDebugEnabled())
            getLogger().debug("Attempting to lock: " + this);

        NodeState ns = NodeState.getInstance();

        LockException excep = null;
        // try to lock each lock in the group and if any fails release all 
        // of the previous ones and wait for 3 seconds...
        int i = 0;
        long timeout = getTimeout();
     
        long start = System.currentTimeMillis();
        while ( timeout == -1 || (System.currentTimeMillis() - start) < timeout ) { 
            try { 
                niinfos = ns.lockNodes(locks.toArray(new Lock[0]));
                break;
            } catch (LockException e) { 
                getLogger().info("Retrying lockgroup [" + this + "]");  
                excep = e; 
                ThreadUtil.pause(1000);
            }
        }
        
        if ( niinfos == null ) 
            throw excep;

        /*
         * Now we can call the necessary SetupAgents and make the lock official
         * by returning the multiple locks to the requesting DTFX.
         */
        try { 
	        for (i = 0; i < locks.size(); i++) 
	            locks.get(i).setupLock(niinfos[i]);
        } catch (DTFException e) { 
            getLogger().error("Unable to setup agent ",e);
            // any failure during the setup of the various locks we should 
            // definitely be certain to release the pre-acquired locks
            for ( ;i >= 0 ; i--) niinfos[i].unlock();
            throw e;
        }
    }
    
    public void setName(String name) { this.name = name; } 
    public String getName() { return name; } 

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public void setTimeout(long timeout) { this.timeout = timeout; } 
    public long getTimeout() { return timeout; } 

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
