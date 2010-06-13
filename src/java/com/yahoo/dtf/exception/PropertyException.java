package com.yahoo.dtf.exception;

import com.yahoo.dtf.exception.DTFException;

/**
 * 
 * @author Rodney Gomes
 *
 */
public class PropertyException extends DTFException {

    public PropertyException(String msg) {
        super(msg);
    }
    
    public PropertyException(String msg, Throwable t) {
        super(msg,t);
    }

}
