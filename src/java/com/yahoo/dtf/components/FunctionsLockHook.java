package com.yahoo.dtf.components;

import java.util.ArrayList;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.component.Lockcomponent;
import com.yahoo.dtf.actions.function.ExportFuncs;
import com.yahoo.dtf.actions.function.Function;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.logger.DTFLogger;

public class FunctionsLockHook implements LockHook {
    
    public ArrayList<Action> init(String id) throws DTFException {
        DTFLogger log = Action.getLogger();
        ArrayList<Action> result = new ArrayList<Action>();
        /*
         * Export necessary functions to the locked component.
         */
        ArrayList<Function> functions = Function.getExportableFunctions();
        if (functions.size() == 0) {
            if (log.isDebugEnabled())
                log.debug("No exported functions.");
        } else {
            ExportFuncs export = new ExportFuncs();
            for (int i = 0; i < functions.size(); i++) {
                Function function = functions.get(i);
                export.addAction(function);
                if (log.isDebugEnabled())
                    log.debug("Sending function [" + function.getName()
                              + "]");
            }
          
            result.add(export);
        }
        return result;
    }
    
}
