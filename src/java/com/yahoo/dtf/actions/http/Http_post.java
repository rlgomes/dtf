package com.yahoo.dtf.actions.http;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.recorder.Event;

/**
 * @dtf.tag http_post
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc The http_post tag is used to generate HTTP POST requests and
 *               can test webservices quite well. At the moment you can't handle 
 *               cookies, but you do have the ability to add Headers to the
 *               request as well as entities (data that can be added to your
 *               post request to push a file or another piece of data with the
 *               http request).
 *               <br/> 
 *               There are varous attributes to control if we use keepalives and
 *               also to control redirects in http. One important attribute is 
 *               the perfrun attribute which when set to false will only log
 *               the necessary information to calculate performance statistics
 *               for this run but wont' log the data that was sent or recevied
 *               by the request. So for those cases in which you you want to 
 *               measure the performance you should set that value to true so 
 *               that you can get more precise performance numbers.
 *               
 * @dtf.event http.post
 * @dtf.event.attr uri
 * @dtf.event.attr.desc the exact uri passed to the http request.
 *
 * @dtf.event http.post
 * @dtf.event.attr headerin
 * @dtf.event.attr.desc each of the input headers for the HTTP request will have
 *                      an event with this attribute name as the prefix to the
 *                      header attribute name. So if you had a header with the
 *                      name X then your resulting event would be named 
 *                      event.headerin.x  
 * 
 * @dtf.event http.post
 * @dtf.event.attr status
 * @dtf.event.attr.desc the status code for the HTTP request.
 *
 * @dtf.event http.post
 * @dtf.event.attr statusmsg
 * @dtf.event.attr.desc the status code for the HTTP request.
 *
 * @dtf.event http.post
 * @dtf.event.attr data 
 * @dtf.event.attr.desc the HTTP post data that was sent along with this 
 *                      specific request.
 *                      
 * @dtf.event http.post
 * @dtf.event.attr datahash
 * @dtf.event.attr.desc the hash of the HTTP post data that was sent along with
 *                      this specific request.
 *
 * @dtf.event http.post
 * @dtf.event.attr body
 * @dtf.event.attr.desc the HTTP post data that was received along with the 
 *                      HTTP response.
 * 
 * @dtf.event http.post
 * @dtf.event.attr bodysize
 * @dtf.event.attr.desc the size in bytes of the data that was received in the
 *                      HTTP response.
 *                      
 * @dtf.event http.post
 * @dtf.event.attr bodyhash
 * @dtf.event.attr.desc the hash of the HTTP post data that was received along 
 *                      with the HTTP response.
 * 
 * @dtf.event http.post
 * @dtf.event.attr headerout
 * @dtf.event.attr.desc each of the output headers received from the HTTP request
 *                      response will generate an event with this attribute name 
 *                      as the prefix to the header attribute name. So if you 
 *                      had a header with the name X then your resulting event 
 *                      would be named event.headerout.x 
 *                      
 * @dtf.tag.example 
 * <http_post uri="${dtf.http.uri}" perfrun="true" onFailure="fail">
 *      <header name="header1" value="XXXXX"/>
 *      <header name="header2" value="YYYY"/>
 *      <entity value="${dtf.post.data}"/>
 * </http_post>
 * 
 * @dtf.tag.example
 * <http_post uri="${dtf.http.uri}"/>
 */
public class Http_post extends HttpBase {
    
    public Event executeOp() throws DTFException {
        return getOp().executePost(this);
    }
}
