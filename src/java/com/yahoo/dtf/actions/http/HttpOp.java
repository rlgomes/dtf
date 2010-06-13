package com.yahoo.dtf.actions.http;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.recorder.Event;

/**
 * Currently this represents what a class needs to implement in order to 
 * process the http request through a different http library. The ability to 
 * change http clients at runtime is not yet in place but can be added quite 
 * easily and this class was created as a first step for the eventual scenario
 * of different http client libraries in use at the same time within DTF.
 * 
 * @dtf.feature HTTP Client
 * @dtf.feature.group HTTP
 * 
 * @dtf.feature.desc <p>
 *                   The HTTP tags available in DTF will always respond with
 *                   HTTP codes from the server side that the server has 
 *                   sent back. These tags will also generate certain client
 *                   side codes using the 9XX range of codes to signal 
 *                   common errors on the client side. Here is the list of
 *                   codes and their description:
 *                   </p>
 *                   <ul>
 *                      <li>900 - Connection error (connection refused)</li>
 *                      <li>901 - Socket error (broken pipe, socket reset)</li>
 *                      <li>902 - Unknown host (dns resolution error, bad hostname)</li>
 *                      <li>903 - Connection timeout error</li>
 *                      <li>999 - Unknown client error</li>
 *                   </ul>
 */
public abstract class HttpOp {

  
    public final static String CONNECT_ERROR        = "900";
    public final static String SOCKET_ERROR         = "901";
    public final static String UNKNOWHOST_ERROR     = "902";
    public final static String CONTIMEOUT_ERROR     = "903";
    
    public final static String UNKNOWN_ERROR        = "999";
    
    /**
     * 
     * Initialize the HTTP libraries.
     */
    public abstract void init();
   
    /**
     * 
     * Shutdown the HTTP libraries, this is used by the ReleaseAgent mechanism 
     * and makes sure that on the agent side things are cleaned up and restarted
     * between test executions.
     */
    public abstract void shutdown();
    
    /**
     * Execute the POST method and return the Event that contains the information
     * described in the class Http_post. Be sure that you are returning all the
     * necessary attributes.
     * 
     * @param op
     * @return
     * @throws DTFException
     */
    public abstract Event executePost(HttpBase op) throws DTFException;

    /**
     * Execute the GET method and return the Event that contains the information
     * described in the class Http_get. Be sure that you are returning all the
     * necessary attributes.
     * 
     * @param op
     * @return
     * @throws DTFException
     */
    public abstract Event executeGet(HttpBase op) throws DTFException;

    /**
     * Execute the PUT method and return the Event that contains the information
     * described in the class Http_put. Be sure that you are returning all the
     * necessary attributes.
     * 
     * @param op
     * @return
     * @throws DTFException
     */
    public abstract Event executePut(HttpBase op) throws DTFException;

    /**
     * Execute the DELETE method and return the Event that contains the information
     * described in the class Http_delete. Be sure that you are returning all the
     * necessary attributes.
     * 
     * @param op
     * @return
     * @throws DTFException
     */
    public abstract Event executeDelete(HttpBase op) throws DTFException;

    /**
     * Execute the HEAD method and return the Event that contains the information
     * described in the class Http_head. Be sure that you are returning all the
     * necessary attributes.
     * 
     * @param op
     * @return
     * @throws DTFException
     */
    public abstract Event executeHead(HttpBase op) throws DTFException;

    /**
     * Execute the method specified by name and return the Event that contains 
     * the information described in the class Http_request. Be sure that you are 
     * returning all the necessary attributes.
     * 
     * @param op
     * @return
     * @throws DTFException
     */
    public abstract Event executeRequest(HttpBase op,
                                         String name) throws DTFException;
    
}
