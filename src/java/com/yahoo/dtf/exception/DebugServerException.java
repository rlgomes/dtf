package com.yahoo.dtf.exception;

import com.yahoo.dtf.exception.DTFException;

/**
 * 
 * @author Rodney Gomes
 *
 */
public class DebugServerException extends DTFException {

    public DebugServerException(String msg) {
        super(msg);
    }
    
    public DebugServerException(String msg, Throwable t) {
        super(msg,t);
    }
}
