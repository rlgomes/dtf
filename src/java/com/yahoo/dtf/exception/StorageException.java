package com.yahoo.dtf.exception;

import com.yahoo.dtf.exception.DTFException;

/**
 * 
 * @author Rodney Gomes
 *
 */
public class StorageException extends DTFException {

    public StorageException(String msg) {
        super(msg);
    }
    
    public StorageException(String msg, Throwable t) {
        super(msg,t);
    }
}
