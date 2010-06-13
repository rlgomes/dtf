package com.yahoo.dtf.exception;

import com.yahoo.dtf.exception.DTFException;

/**
 * 
 * @author Rodney Gomes
 *
 */
public class ActionException extends DTFException {

    public ActionException(String msg) {
        super(msg);
    }
    
    public ActionException(String msg, Throwable t) {
        super(msg,t);
    }

}
