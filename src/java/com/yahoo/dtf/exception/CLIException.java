package com.yahoo.dtf.exception;

import com.yahoo.dtf.exception.DTFException;

/**
 * 
 * @author Rodney Gomes
 *
 */
public class CLIException extends DTFException {

    public CLIException(String msg) {
        super(msg);
    }
    
    public CLIException(String msg, Throwable t) {
        super(msg,t);
    }
}
