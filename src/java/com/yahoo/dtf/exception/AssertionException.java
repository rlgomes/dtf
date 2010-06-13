package com.yahoo.dtf.exception;

import com.yahoo.dtf.exception.DTFException;

/**
 * 
 * @author Rodney Gomes
 *
 */
public class AssertionException extends DTFException {

    public AssertionException(String msg) {
        super(msg);
    }
    
    public AssertionException(String msg, Throwable t) {
        super(msg,t);
    }

}
