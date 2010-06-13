package com.yahoo.dtf.exception;

import com.yahoo.dtf.exception.DTFException;

/**
 * 
 * @author Rodney Gomes
 *
 */
public class ReturnException extends DTFException {

    private String returnValue = null;
    
    public void setReturnValue(String value) { 
        returnValue = value;
    }
    
    public String getReturnValue() { 
        return returnValue;
    }
    
    public ReturnException(String msg) {
        super(msg);
    }
    
    public ReturnException(String msg, Throwable t) {
        super(msg,t);
    }
}
