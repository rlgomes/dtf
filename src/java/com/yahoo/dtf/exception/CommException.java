package com.yahoo.dtf.exception;

import com.yahoo.dtf.exception.DTFException;

/**
 * 
 * @author Rodney Gomes
 *
 */
public class CommException extends DTFException {

    public CommException(String msg) {
        super(msg);
    }
    
    public CommException(String msg, Throwable t) {
        super(msg,t);
    }
}
