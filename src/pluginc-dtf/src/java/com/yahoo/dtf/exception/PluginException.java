package com.yahoo.dtf.exception;

import com.yahoo.dtf.exception.DTFException;

public class PluginException extends DTFException {

    public PluginException(String msg) {
        super(msg);
    }
    
    public PluginException(String msg, Throwable t) {
        super(msg,t);
    }

}
