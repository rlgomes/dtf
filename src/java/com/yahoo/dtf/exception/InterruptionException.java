package com.yahoo.dtf.exception;

import com.yahoo.dtf.exception.DTFException;

/**
 * 
 * @author Rodney Gomes
 *
 */
public class InterruptionException extends DTFException {

    public InterruptionException(String msg) {
        super(msg);
    }
    
    public InterruptionException(String msg, Throwable t) {
        super(msg,t);
    }

}
