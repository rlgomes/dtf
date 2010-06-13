package com.yahoo.dtf.exception;

import com.yahoo.dtf.exception.DTFException;

/**
 * 
 * @author Rodney Gomes
 *
 */
public class RendezvousException extends DTFException {

    public RendezvousException(String msg) {
        super(msg);
    }
    
    public RendezvousException(String msg, Throwable t) {
        super(msg,t);
    }
}
