package com.yahoo.dtf.exception;

import com.yahoo.dtf.exception.DTFException;

public class RangeException extends DTFException {

    public RangeException(String msg) {
        super(msg);
    }
    
    public RangeException(String msg, Throwable t) {
        super(msg,t);
    }

}
