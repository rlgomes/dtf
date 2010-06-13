package com.yahoo.dtf.config;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.config.DynamicProperty;

/**
 * Just a simple dynamic property that gives the testwriter access to the 
 * node name incase this is somehow useful for the test execution.
 * 
 * @author rlgomes
 */
public class DTFNodeName implements DynamicProperty {
    
    public static final String DTF_NODENAME = "dtf.node.name";

    public String getValue(String args) {
        return Action.getLocalID();
    }
}
