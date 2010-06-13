package com.yahoo.dtf.exception;

import com.yahoo.dtf.exception.DTFException;

/**
 * 
 * @author Rodney Gomes
 *
 */
public class ParseException extends DTFException {

    public ParseException(String msg) {
        super(msg);
    }
    
    public ParseException(String msg, Throwable t) {
        super(msg,t);
    }
}
