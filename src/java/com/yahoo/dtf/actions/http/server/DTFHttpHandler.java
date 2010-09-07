package com.yahoo.dtf.actions.http.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.http.Entity;
import com.yahoo.dtf.actions.http.HttpBase;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.recorder.Event;
import com.yahoo.dtf.state.ActionState;
import com.yahoo.dtf.state.DTFState;
import com.yahoo.dtf.streaming.DTFInputStream;
import com.yahoo.dtf.util.HashUtil;
import com.yahoo.dtf.util.streams.Throttler;

/**
 * @dtf.feature HTTP Server
 * @dtf.feature.group HTTP
 * 
 * @dtf.feature.desc
 * <p>
 * Within DTF it is possible to start your own HTTP server that will respond 
 * to any type of HTTP method and be able to construct an HTTP response to your
 * required needs. You'll find that you have to be a bit careful when starting
 * and shutting down the httpserver because otherwise you may create a test that
 * gets stuck waiting for a parallel executed http_server that doesn't end 
 * because you haven't issued the shutdown correctly. A simple receip to follow
 * is in the next few lines of XML code:
 * </p>
 * 
 * {@dtf.xml
 * <parallel>    
 *     <http_server port="28080">
 *         <http_listener path="/testpath" method="PUT"/>
 *     </http_server>
 *        
 *     <try>
 *         <log>Do something...</log>
 *         <finally>
 *             <http_server command="stop" port="28080"/>
 *         </finally>
 *     </try>
 * </parallel>}
 * 
 * <p>
 * The previous template works well because after staring the HTTP server we're 
 * very careful to protect the stop command within a try/finally statement
 * that guarantees we'll always stop the server before proceeding from whatever 
 * client activity we have tried to execute.
 * </p>
 */

public class DTFHttpHandler implements HttpRequestHandler {

    private Http_listener _listener;
    private String _method;
    private DTFState  _state;
    
    public DTFHttpHandler(String method,
                          Http_listener listener) {
        super();
        _listener = listener;
        _method = method.toUpperCase();
        // duplicate the state so that multiple threads won't run into any 
        // issues
        _state = Action.getState().duplicate();
    }

    public void handle(HttpRequest request,
                       HttpResponse response,
                       HttpContext context)
           throws HttpException, IOException {
        String method = request.getRequestLine().getMethod().toUpperCase();
        String target = request.getRequestLine().getUri();
        
        StringBuffer headerlist = new StringBuffer();
        
        Event event = new Event("http." + method.toLowerCase());
        event.start();
        event.stop();
        Header[] headers = request.getAllHeaders();
        for (int i = 0; i < headers.length; i++) { 
            Header header = headers[i];
            event.addAttribute(HttpBase.HTTP_EVENT_HEADER_IN + "." + 
                               header.getName(), header.getValue());
            headerlist.append(header.getName() + (i+1 < headers.length ? "," : ""));
        }
        event.addAttribute(HttpBase.HTTP_EVENT_HEADER_IN, 
                           headerlist.toString());
        
        event.addAttribute("path", request.getRequestLine().getUri());
       
        if (request instanceof HttpEntityEnclosingRequest) {
            HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
            try {
                String bw = _listener.getBandwidth();
                
                if ( !_listener.isPerfrun() ) {
                    OutputStream os = new ByteArrayOutputStream();
                   
                    if ( bw != null )
                        os = Throttler.wrapOutputStream(os, bw);
                    
                    entity.writeTo(os);
                    event.addAttribute(HttpBase.HTTP_EVENT_BODY, 
                                       os.toString());
                } else {
                    // sha1 of the object.
                    InputStream is = entity.getContent();
                   
                    if ( bw != null )
                        is = Throttler.wrapInputStream(is, bw);
                    
                    MessageDigest md = null;
                    boolean calchash = false;
                    if (!_listener.getHash().equals("none")) {
                        md = MessageDigest.getInstance(_listener.getHash());
                        calchash = true;
                    }

                    int totalRead = 0;
                    int read = 0;
                    byte[] buffer = new byte[4 * 1024];
                    while ((read = is.read(buffer)) != -1) {
                        if (calchash)
                            md.update(buffer, 0, read);
                        totalRead += read;
                    }

                    event.addAttribute(HttpBase.HTTP_EVENT_BODY_SIZE, totalRead);
                    if (calchash) {
                        event.addAttribute(HttpBase.HTTP_EVENT_BODY_HASH,
                                           HashUtil.convertToHex(md.digest()));
                        event.addAttribute(HttpBase.HTTP_EVENT_HASH_ALGO,
                                           _listener.getHash());
                    }
                }
            } catch (IOException e) {
                throw new HttpException("Error handling  stream.", e);
            } catch (NoSuchAlgorithmException e) {
                throw new HttpException("Error handling  stream.", e);
            } catch (ParseException e) {
                throw new HttpException("Error handling  stream.", e);
            }
        }
        
        if ( method.equals(_method) || _method.equals("*") ) {
            ActionState as = ActionState.getInstance();
            as.setState(_state);
            try {
                _state.getRecorder().record(event);
                _listener.execute();

                Integer rc = (Integer) 
                            Action.getContext(Http_response.HTTP_RESPONSE_CODE);
                String msg = (String) 
                             Action.getContext(Http_response.HTTP_RESPONSE_MSG);

                if (rc != null)
                    response.setStatusCode(rc);
                else
                    response.setStatusCode(HttpStatus.SC_OK);

                if ( msg != null )
                    response.setReasonPhrase(msg);
                
                ArrayList<com.yahoo.dtf.actions.http.Header> rheaders = 
                    (ArrayList<com.yahoo.dtf.actions.http.Header>)
                         Action.getContext(Http_response.HTTP_RESPONSE_HEADERS);
           
                if ( rheaders != null ) { 
	                for (int i = 0; i < rheaders.size(); i++) { 
	                    com.yahoo.dtf.actions.http.Header header = rheaders.get(i);
	                    response.setHeader(header.getName(),header.getValue());
	                }
                }

                ArrayList<com.yahoo.dtf.actions.http.cookies.Cookie> rcookies = 
                    (ArrayList<com.yahoo.dtf.actions.http.cookies.Cookie>)
                         Action.getContext(Http_response.HTTP_RESPONSE_COOKIES);
                
                if ( rcookies != null ) { 
	                for (int i = 0; i < rcookies.size(); i++) { 
	                    com.yahoo.dtf.actions.http.cookies.Cookie cookie = 
	                                                                rcookies.get(i);
	                }
                }
              
                long size = 0;
                
                Http_response cresponse = (Http_response)
                                 _listener.findFirstAction(Http_response.class);
                
                if ( cresponse != null ) { 
	                Entity entity = (Entity) 
	                          Action.getContext(Http_response.HTTP_RESPONSE_ENTITY);
	                
	                if ( entity != null ) { 
	                    DTFInputStream dtfis = entity.getEntityStream();
	                    size = dtfis.getSize();
	                    
	                    InputStream is = dtfis;
	                    
	                    String bw = cresponse.getBandwidth();
	                    if ( bw != null )
	                        is = Throttler.wrapInputStream(is, bw);
	                    
	                    HttpEntity rentity = new InputStreamEntity(is, size);
	                    response.setEntity(rentity);
	                }
                }

                response.setHeader("Content-Length", "" + size);
            } catch (DTFException e) {
                response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
                response.setReasonPhrase(e.getMessage());
                response.setHeader("Content-Length", "0");
            } finally {
                as.delState();
            }
        } else { 
            response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
            response.setReasonPhrase("Invalid request there isn't a " + method + 
                                     " listener at " + target);
            response.setHeader("Content-Length", "0");
        }
    }
}
