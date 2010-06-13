package com.yahoo.dtf.actions.http;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.recorder.Event;

/**
 * @dtf.tag http_delete
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc 
 *               
 * @dtf.event http.delete
 * @dtf.event.attr uri
 * @dtf.event.attr.desc the exact uri passed to the http request.
 *
 * @dtf.event http.delete
 * @dtf.event.attr headerin
 * @dtf.event.attr.desc each of the input headers for the HTTP request will have
 *                      an event with this attribute name as the prefix to the
 *                      header attribute name. So if you had a header with the
 *                      name X then your resulting event would be named 
 *                      event.headerin.x  
 * 
 * @dtf.event http.delete
 * @dtf.event.attr status
 * @dtf.event.attr.desc the status code for the HTTP request.
 *
 * @dtf.event http.delete
 * @dtf.event.attr statusmsg
 * @dtf.event.attr.desc the status code for the HTTP request.
 *
 * @dtf.event http.delete
 * @dtf.event.attr body
 * @dtf.event.attr.desc the http.delete data that was received along with the 
 *                      HTTP response.
 *
 * @dtf.event http.delete
 * @dtf.event.attr bodysize
 * @dtf.event.attr.desc the size in bytes of the data that was received in the
 *                      HTTP response.
 *                      
 * @dtf.event http.delete
 * @dtf.event.attr bodyhash
 * @dtf.event.attr.desc the hash of the http.delete data that was received along 
 *                      with the HTTP response.
 * 
 * @dtf.event http.delete
 * @dtf.event.attr headerout
 * @dtf.event.attr.desc each of the output headers received from the HTTP request
 *                      response will generate an event with this attribute name 
 *                      as the prefix to the header attribute name. So if you 
 *                      had a header with the name X then your resulting event 
 *                      would be named event.headerout.x 
 *                      
 * @dtf.tag.example 
 * <http_delete uri="${dtf.http.uri}" perfrun="true" onFailure="fail">
 *      <header name="header1" value="XXXXX"/>
 *      <header name="header2" value="YYYY"/>
 * </http_delete>
 * 
 * @dtf.tag.example
 * <http_delete uri="${dtf.http.uri}"/>
 */
public class Http_delete extends HttpBase {
    
    public Event executeOp() throws DTFException {
        return getOp().executeDelete(this);
    }
}
