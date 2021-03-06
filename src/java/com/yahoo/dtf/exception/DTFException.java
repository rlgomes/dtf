package com.yahoo.dtf.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.state.ActionState;

/**
 * 
 * @author Rodney Gomes
 *
 */
public class DTFException extends Exception {

    private String id = null;
    
    private boolean alreadyReported = false;

    public DTFException() { }

    public DTFException(String msg) {
        super(msg + 
              (ActionState.getInstance().getState().getAction() != null ? 
               ActionState.getInstance().getState().getAction().getXMLLocation() : ""));
    }
    
    public DTFException(String msg, Throwable t) { 
        super(msg + 
              (ActionState.getInstance().getState().getAction() != null ? 
               ActionState.getInstance().getState().getAction().getXMLLocation() : ""), 
               t);
    }
    
    @Override
    public String getMessage() {
        if ( id != null ) { 
            /*
             * Its very useful to see when an exception originated on a 
             * component as well as knowning exactly which component that is 
             * in case we happen to have a more complex DTF test in which the
             * component id is changing at runtime for a specific line in the 
             * test script.
             */
            return super.getMessage() + " on component [" + id + "]";
        } else { 
            return super.getMessage();
        }
    }
    
    public void setComponent(String id) { this.id = id; }
    
    public boolean wasLogged() { return alreadyReported; }
    public void logged() { alreadyReported = true; }
}
