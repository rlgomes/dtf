package com.yahoo.dtf.exception;

import com.yahoo.dtf.exception.DTFException;

/**
 * 
 * @author Rodney Gomes
 *
 */
public class QueryException extends DTFException {

    public QueryException(String msg) {
        super(msg);
    }
    
    public QueryException(String msg, Throwable t) {
        super(msg,t);
    }
}
