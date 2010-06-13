package com.yahoo.dtf.plugin.init;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.init.InitClass;

public class InitHook implements InitClass {

    public void init() throws DTFException {
    	Action.getLogger().info("Example InitHook got called.");
    }
}
