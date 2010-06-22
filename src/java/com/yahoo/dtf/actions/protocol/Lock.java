package com.yahoo.dtf.actions.protocol;

import com.yahoo.dtf.actions.protocol.Connect;
import com.yahoo.dtf.actions.protocol.SetupAgent;
import com.yahoo.dtf.DTFProperties;
import com.yahoo.dtf.NodeInfo;
import com.yahoo.dtf.NodeState;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.comm.Comm;
import com.yahoo.dtf.comm.rpc.ActionResult;
import com.yahoo.dtf.comm.rpc.Node;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.LockException;
import com.yahoo.dtf.util.ThreadUtil;

public class Lock extends Connect {

    private String owner = null;

    private String name = null;
   
    /*
     * Used for direct connection between executer and agents.
     */
    private String address = null;
    
    private long timeout = 0;
    
    private boolean tunneled = false;
    
    public Lock() { }
    
    public Lock(String id, String refid, long timeout) 
           throws DTFException{
        setId(id);
        setOwner(Action.getLocalID());
        setAddress(getConfig().getProperty(DTFProperties.DTF_LISTEN_ADDR));
        setPort(getConfig().getPropertyAsInt(DTFProperties.DTF_LISTEN_PORT));
        setName(refid);
        setTimeout(timeout);
    }
    
    public void execute() throws DTFException {
        if (getLogger().isDebugEnabled())
            getLogger().debug("Attemtping to lock: " + this);

        NodeState ns = NodeState.getInstance();
        NodeInfo[] nis = null;
       
        long start = System.currentTimeMillis();
        LockException excep = null;
        do { 
            try { 
                /*
	             * If the node is no longer here then give up on the lock and 
	             * don't bother locking anything.
	             */
	            if ( !ns.isNodeRegistered(getOwner()) ) {
	                if ( getLogger().isDebugEnabled() ) 
	                    getLogger().debug("Lock owner disconnected, throwing lock request away.");
	                return;
	            }
	            nis = ns.lockNodes(new Lock[]{this});
                break;
            } catch (LockException e) { 
                //if (getLogger().isDebugEnabled())
                getLogger().info("Retrying lock: " + this + " for " + getOwner());
                excep = e; 
                ThreadUtil.pause(1000);
            }
        } while (System.currentTimeMillis() - start < getTimeout());
        
        if (excep != null && nis == null) 
            throw excep;
              
        for (NodeInfo ni : nis) 
            setupLock(ni);
    }
    
    protected void setupLock(NodeInfo ni) throws DTFException { 
        /*
         * Communicate with locked component to register the owner information
         * that can be later used to identify who locked this component as well 
         * as allow communication between a locked component and the component
         * that holds this lock.
         */
        SetupAgent sa = new SetupAgent(this);
       
        // set the tunneled attribute.
        sa.setTunneled(Comm.isTunneled());
        setTunneled(Comm.isTunneled());
       
        ActionResult ar = ni.getClient().sendAction(ni.getId(), sa); 
        // must check that we did in fact succeed to send the action.
        ar.execute();
        
        /*
         * Now that we have a NodeInfo that means we've locked a 
         * component and we can return with this NodeInfo object
         * the direct connection information for the node requested.
         */
        setAddress(ni.getAddress());
        setPort(ni.getPort());
        
        copy(ni);
        getLogger().info("Locked node " + ni + " for " + ni.getOwner());
        
        ar = (ActionResult) getContext(Node.ACTION_RESULT_CONTEXT);
        ar.addAction(this);
    }
    
    public void setName(String name) { this.name = name; } 
    public String getName() { return name; } 

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public void setTimeout(long timeout) { this.timeout = timeout; } 
    public long getTimeout() { return timeout; } 

    public void setTunneled(boolean tunneled) { this.tunneled = tunneled; } 
    public boolean getTunneled() { return tunneled; } 
    
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
