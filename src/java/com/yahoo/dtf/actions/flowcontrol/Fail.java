package com.yahoo.dtf.actions.flowcontrol;

import com.yahoo.dtf.actions.util.CDATA;
import com.yahoo.dtf.exception.FailException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag fail
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Fail tag allows us to throw a failure at any point in the test 
 *               case where we feel that by reaching this point in the test we 
 *               have in fact hit an issue that should be noted as a failure of 
 *               the test case.
 * 
 * @dtf.tag.example 
 * <fail message="There was failure."/>
 *
 * @dtf.tag.example 
 * <fail>
 *     Failed to meet some limit in the test
 * </fail>
 */
public class Fail extends CDATA {

    /**
     * @dtf.attr message
     * @dtf.attr.desc The message to be used when throwing the FailException.
     */
    private String message = null;
    
    public Fail() {}
    
    public void execute() throws FailException, ParseException {
        String message = getMessage();
       
        if ( message == null ) 
            message = getCDATA();
        
        if ( message == null )
            throw new ParseException("Set the message attribute or provide a text node to the fail tag.");
        
        throw new FailException(message);
    }

    public String getMessage() throws ParseException { return replaceProperties(message); }
    public void setMessage(String message) { this.message = message; }
}
