package com.yahoo.dtf.exception;

import com.yahoo.dtf.exception.DTFException;

/**
 * 
 * @author Rodney Gomes
 *
 */
public class FunctionException extends DTFException {

    public FunctionException(String msg) {
        super(msg);
    }
    
    public FunctionException(String msg, Throwable t) {
        super(msg,t);
    }
}
