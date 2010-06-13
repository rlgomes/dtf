package com.yahoo.dtf.actions.protocol;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;

/*
 * XXX: should keep the stacktrace as well, just need to create stacktracelement 
 *      actions and append them to eachother. 
 *
 */
public class SerializationException extends Action {

    private String message = null;
    
    @Override
    public void execute() throws DTFException {
        throw new DTFException(message);
    }
    
    public void setMessage(String message) { this.message = message; }
    public String getMessage() { return message; }
}