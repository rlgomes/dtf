package com.yahoo.dtf.actions.http;

import java.io.IOException;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.recorder.Event;

/**
 * @dtf.tag http_request
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc This HTTP tag allows you to create your own HTTP request method
 *               which can be used to do requests against non REST APIs such as
 *               WEBDAV which has special HTTP methods for creating directories,
 *               locking files, moving files, etc.
 *
 * @dtf.event http.[method]
 * @dtf.event.attr uri
 * @dtf.event.attr.desc the exact uri passed to the http request.
 *
 * @dtf.event http.[method]
 * @dtf.event.attr headerin
 * @dtf.event.attr.desc each of the input headers for the HTTP request will have
 *                      an event with this attribute name as the prefix to the
 *                      header attribute name. So if you had a header with the
 *                      name X then your resulting event would be named 
 *                      event.headerin.x  
 * 
 * @dtf.event http.[method]
 * @dtf.event.attr status
 * @dtf.event.attr.desc the status code for the HTTP request.
 *
 * @dtf.event http.[method]
 * @dtf.event.attr statusmsg
 * @dtf.event.attr.desc the status code for the HTTP request.
 *
 * @dtf.event http.[method]
 * @dtf.event.attr body
 * @dtf.event.attr.desc the HTTP request data that was received along with the 
 *                      HTTP response.
 *                      
 * @dtf.event http.[method]
 * @dtf.event.attr bodysize
 * @dtf.event.attr.desc the size in bytes of the data that was received in the
 *                      HTTP response.
 *                      
 * @dtf.event http.[method]
 * @dtf.event.attr bodyhash
 * @dtf.event.attr.desc the hash of the http.[method] data that was received along 
 *                      with the HTTP response.
 *                      
 * @dtf.event http.[method]
 * @dtf.event.attr headerout
 * @dtf.event.attr.desc each of the output headers received from the HTTP request
 *                      response will generate an event with this attribute name 
 *                      as the prefix to the header attribute name. So if you 
 *                      had a header with the name X then your resulting event 
 *                      would be named event.headerout.x  
 * 
 * @dtf.tag.example 
 * <http_request uri="http://localhost/webdav/newfolder" method="MKCOL"/>
 * 
 * @dtf.tag.example
 * <http_request uri="http://localhost/webdav/newfolder" method="GET"/>
 */
public class Http_request extends HttpBase {
   
    private String method = null;
    
    public Event executeOp() throws DTFException { 
        return getOp().executeRequest(this, getMethod());
    }
    
    public void setMethod(String method) { this.method = method; } 
    public String getMethod() throws ParseException { return replaceProperties(method); } 
}
