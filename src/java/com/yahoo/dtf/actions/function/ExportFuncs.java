package com.yahoo.dtf.actions.function;

import java.util.ArrayList;

import com.yahoo.dtf.actions.util.DTFProperty;
import com.yahoo.dtf.comm.rpc.Node;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.state.ActionState;
import com.yahoo.dtf.state.DTFState;

/**
 * This class is used to export functions on the component side and should only
 * be used by internal classes such as the export class which is accessible in 
 * the DTF XML.
 */
public class ExportFuncs extends DTFProperty {

    public ExportFuncs() { }

    public void execute() throws DTFException {
        DTFState state = ActionState.getInstance().getState(Node.BASE_CONFIG);
        ArrayList<Function> functions = findActions(Function.class);
        
        for (int i = 0; i < functions.size(); i++) { 
            Function function = functions.get(i);
            state.getFunctions().addFunction(function.getName(),function);
            if ( getLogger().isDebugEnabled() ) {
                getLogger().debug("Accepting function [" + function.getName() + 
                                  "]");
            }
        }
    }
    
    public String getValue() throws ParseException {
        return value;
    }
}
