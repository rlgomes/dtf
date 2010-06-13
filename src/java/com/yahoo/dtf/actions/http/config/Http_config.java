package com.yahoo.dtf.actions.http.config;

import com.yahoo.dtf.actions.reference.Referencable;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag http_config
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc 
 * 
 * @dtf.tag.example 
 * 
 */
public class Http_config extends Referencable {
 
    /**
     * @dtf.attr expectcontinue
     * @dtf.attr.desc  Activates 'Expect: 100-Continue' handshake for the entity 
     *                 enclosing methods. The purpose of the handshake is to 
     *                 allow a client that is sending a request message with a 
     *                 request body to determine if the origin server is willing
     *                 to accept the request (based on the request headers) 
     *                 before the client sends the request body. In other words
     *                 it allows the server to respond before the client has 
     *                 written out the request body and therefore can detect an 
     *                 error or a redirect to another server earlier.
     *                 <br/> 
     *                 Use with caution because it may or may not result in a 
     *                 performance hit depending on the type of response the 
     *                 server is sending back based on the requests header 
     *                 information.
     */
    public String expectcontinue = null;
    
    public void execute() throws DTFException {
        
    }

    public boolean getExpectcontinue() throws ParseException { return toBoolean("expectcontinue", expectcontinue); }
    public void setExpectcontinue(String expectcontinue) { this.expectcontinue = expectcontinue; }
}
