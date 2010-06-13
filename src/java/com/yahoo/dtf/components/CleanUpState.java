package com.yahoo.dtf.components;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.comm.rpc.Node;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.state.ActionState;

/**
 * Internally used action that is used to clean up remote state on the agents
 * when a thread on the runner no longer exists and its remote state can be 
 * cleared up.
 * 
 * @author rlgomes
 */
public class CleanUpState extends Action {

    private String id = null;
    
    @Override
    public void execute() throws DTFException {
        String id = genThreadName(getId());
        ActionState.getInstance().delState(id);
        Node.cleanedup(id);
    }
    
    public static String genThreadName(String id ) { 
        return "R" + id;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
}
