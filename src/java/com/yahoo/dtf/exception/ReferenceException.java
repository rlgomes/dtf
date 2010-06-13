package com.yahoo.dtf.exception;

import com.yahoo.dtf.exception.DTFException;

/**
 * 
 * @author Rodney Gomes
 *
 */
public class ReferenceException extends DTFException {

    public ReferenceException(String msg) {
        super(msg);
    }
    
    public ReferenceException(String msg, Throwable t) {
        super(msg,t);
    }
}
