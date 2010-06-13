package com.yahoo.dtf.exception;

import com.yahoo.dtf.exception.DTFException;

/**
 * 
 * @author Rodney Gomes
 *
 */
public class ShareException extends DTFException {

    public ShareException(String msg) {
        super(msg);
    }
    
    public ShareException(String msg, Throwable t) {
        super(msg,t);
    }
}
