package com.yahoo.dtf.exception;

import com.yahoo.dtf.exception.DTFException;

/**
 * 
 * @author Rodney Gomes
 *
 */
public class LockException extends DTFException {

    public LockException(String msg) {
        super(msg);
    }
    
    public LockException(String msg, Throwable t) {
        super(msg,t);
    }

}
