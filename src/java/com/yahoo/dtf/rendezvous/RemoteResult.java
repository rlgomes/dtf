package com.yahoo.dtf.rendezvous;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;

/**
 * Utility class used to transport responses from remote executions. This is 
 * mainly used by DTF internal activities that need to transport information
 * between remote places and the current point of execution.
 * 
 * @author rlgomes
 */
public class RemoteResult extends Action {

    private String string = null;
    
    private String integer = null;
    
    private String bool = null;
    
    @Override
    public void execute() throws DTFException {
        // no execution just a means of moving data around.
    }

    public String getString() { return string; }
    public void setString(String string) { this.string = string; }

    public int getInteger() { return Integer.valueOf(integer); }
    public void setInteger(String integer) { this.integer = integer; }

    public boolean getBool() { return Boolean.valueOf(bool); }
    public void setBool(String bool) { this.bool = bool; }
    public void setBool(Boolean bool) { this.bool = ""+bool; }
}
