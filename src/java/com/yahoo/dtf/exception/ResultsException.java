package com.yahoo.dtf.exception;

import com.yahoo.dtf.exception.DTFException;

public class ResultsException extends DTFException {

    public ResultsException(String msg) {
        super(msg);
    }
    
    public ResultsException(String msg, Throwable t) {
        super(msg,t);
    }

}
