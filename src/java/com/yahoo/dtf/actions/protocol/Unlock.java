package com.yahoo.dtf.actions.protocol;

import com.yahoo.dtf.actions.protocol.Lock;
import com.yahoo.dtf.NodeInfo;
import com.yahoo.dtf.NodeState;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;

public class Unlock extends Lock {

    public Unlock() { }

    public Unlock(String id, String owner)
           throws DTFException{
        this(id);
        setOwner(owner);
    }
    
    public Unlock(String id)
           throws DTFException{
        setId(id);
        setOwner(Action.getLocalID());
    }
    
    public void execute() throws DTFException {
        if (getLogger().isDebugEnabled())
            getLogger().debug("Attemtping to unlock: " + this);
        NodeState ns = NodeState.getInstance();
        NodeInfo ni = ns.unlockNode(this);
        getLogger().info("Unlocked node " + ni);
    }
   
}
