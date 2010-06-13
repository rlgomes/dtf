package com.yahoo.dtf.exception;

import com.yahoo.dtf.exception.DTFException;

/**
 * 
 * @author Rodney Gomes
 *
 */
public class FailException extends DTFException {

    public FailException(String msg) {
        super(msg);
    }
    
    public FailException(String msg, Throwable t) {
        super(msg,t);
    }
}
