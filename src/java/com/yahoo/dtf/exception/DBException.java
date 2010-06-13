package com.yahoo.dtf.exception;

import com.yahoo.dtf.exception.DTFException;

/**
 * 
 * @author Rodney Gomes
 *
 */
public class DBException extends DTFException {

    public DBException(String msg) {
        super(msg);
    }
    
    public DBException(String msg, Throwable t) {
        super(msg,t);
    }
}
