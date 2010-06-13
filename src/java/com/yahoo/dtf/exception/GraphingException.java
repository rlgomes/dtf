package com.yahoo.dtf.exception;

import com.yahoo.dtf.exception.DTFException;

/**
 * 
 * @author Rodney Gomes
 *
 */
public class GraphingException extends DTFException {

    public GraphingException(String msg) {
        super(msg);
    }
    
    public GraphingException(String msg, Throwable t) {
        super(msg,t);
    }
}
