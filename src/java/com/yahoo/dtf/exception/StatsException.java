package com.yahoo.dtf.exception;

import com.yahoo.dtf.exception.DTFException;

/**
 * 
 * @author Rodney Gomes
 *
 */
public class StatsException extends DTFException {

    public StatsException(String msg) {
        super(msg);
    }
    
    public StatsException(String msg, Throwable t) {
        super(msg,t);
    }
}
