package com.yahoo.dtf.actions.protocol;

import java.io.IOException;
import java.util.Hashtable;

import org.apache.http.ConnectionClosedException;

import com.yahoo.dtf.actions.protocol.Lock;
import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.comm.Comm;
import com.yahoo.dtf.comm.CommClient;
import com.yahoo.dtf.comm.CommRMIClient;
import com.yahoo.dtf.comm.rpc.Node;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.state.ActionState;
import com.yahoo.dtf.state.DTFState;

/**
 * Setups necessary information for the DTFA to be able to communicate with the 
 * DTFX in case of asynchronous events.
 * 
 */
public class SetupAgent extends Lock {
    
    public SetupAgent() { }
    
    public SetupAgent(Lock lock) { 
        setId(lock.getName());
        setOwner(lock.getOwner());
        setAddress(lock.getAddress());
        setPort(lock.getPort());
    }
    
    public void execute() throws DTFException {
        /*
         * For the case in which we're tunneling to this agent then we should
         * communicate with the DTFX using the controller. It is slower but its
         * the only guaranteed method.
         */
        if (!getTunneled()) { 
            CommClient client = new CommRMIClient(getAddress(), getPort());
            if ( client.heartbeat().booleanValue() ) { 
                if (getLogger().isDebugEnabled())
                    getLogger().debug("Direction connection to component being used.");
    
                Comm.addClient(getOwner(), client);
            } 
        }
      
        /*
         * Recreate the Agent base config from the main config that the agent
         * has when it starts for the first time and connects to the DTFC. 
         */
        ActionState as = ActionState.getInstance();
        DTFState state = as.getState("main").duplicate();
        state.setGlobalContext((Hashtable)state.getGlobalContext().clone());
        as.setState(Node.BASE_CONFIG, state);
        
        DTFNode.setOwner(this);
        getLogger().info("This agent [" + getId() + "] in use by [" + 
                         getOwner() + "]");
    }
}
