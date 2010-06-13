package com.yahoo.dtf.exception;

import com.yahoo.dtf.exception.DTFException;

/**
 * 
 * @author Rodney Gomes
 *
 */
public class DistributionException extends DTFException {

    public DistributionException(String msg) {
        super(msg);
    }
    
    public DistributionException(String msg, Throwable t) {
        super(msg,t);
    }
}
