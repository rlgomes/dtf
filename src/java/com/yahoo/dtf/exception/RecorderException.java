package com.yahoo.dtf.exception;

import com.yahoo.dtf.exception.DTFException;

/**
 * 
 * @author Rodney Gomes
 *
 */
public class RecorderException extends DTFException {

    public RecorderException(String msg) {
        super(msg);
    }
    
    public RecorderException(String msg, Throwable t) {
        super(msg,t);
    }
}
