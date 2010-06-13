package com.yahoo.dtf.exception;

import com.yahoo.dtf.exception.DTFException;

/**
 * This exception is used to stop execution in a nice way by being able to break
 * the execution of a for,while,parallelloop,etc. without having any logical 
 * condition to do so just by executing the break tag.
 * 
 * @author Rodney Gomes
 */
public class BreakException extends DTFException {

    public BreakException(String msg) {
        super(msg);
    }
    
    public BreakException(String msg, Throwable t) {
        super(msg,t);
    }

}
