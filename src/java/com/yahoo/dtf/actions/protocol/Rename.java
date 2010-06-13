package com.yahoo.dtf.actions.protocol;

import com.yahoo.dtf.DTFConstants;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;

public class Rename extends Action {
    
    static {
        ReleaseAgent.addCleanUpHook(new CleanUpHook() {
            public void cleanup() throws DTFException {
                unRegisterGlobalContext(DTFConstants.DTF_NODE_NAME);
            }
        });
    }
    
    public String name = null;
    
    public Rename() {}

    public void execute() {
        getLogger().info("Node name set to: " + getName());
        registerGlobalContext(DTFConstants.DTF_NODE_NAME, getName());
    }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; } 
}
