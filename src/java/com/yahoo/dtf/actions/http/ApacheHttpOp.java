package com.yahoo.dtf.actions.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.ProxyHost;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.http.config.Http_config;
import com.yahoo.dtf.actions.http.config.Proxy;
import com.yahoo.dtf.actions.http.cookies.Cookie;
import com.yahoo.dtf.actions.http.cookies.Cookiegroup;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.recorder.Event;
import com.yahoo.dtf.streaming.DTFInputStream;
import com.yahoo.dtf.util.HashUtil;
import com.yahoo.dtf.util.streams.Throttler;

/**
 * @dtf.feature HTTP SSL Certification Chain
 * @dtf.feature.group HTTP
 * 
 * @dtf.feature.desc
 * <p>
 * Currently there is only an Apache Client HTTP implementation for the DTF 
 * HTTP tags. This implementation uses the Java X.509 Certificate Management 
 * layer for all certificates related with HTTPS requests. So in order to add 
 * your own certificate to this chain you'll have to use Java specific tools for
 * this effect. 
 * 
 * The certification chain with Java has its keystore located at:
 * <ul> 
 *     <li>Unix: ${user.home}/.java/deployment/security.</li>
 *     <li>Windows: ${deployment.user.home}\security</li>
 * </ul>
 *
 * <p>
 * Other locations, such as system level certificates and the ability to even 
 * change the keystore location  is documented <a href="http://java.sun.com/j2se/1.5.0/docs/guide/deployment/deployment-guide/jcp.html">here</a>.
 * To manage the certificates int these keystores you must use the keytool tool 
 * supplied with the Java JDK and has some usage instructions <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">here</a>.
 * The usage of the tool is very straightforward if you already have an existing
 * X.509 certificate you want to add to your keystore you just use the 
 * following:
 * </p>
 * 
 * <pre>
 * keytool -import -alias alias_for_your_ca -file newCA.cer
 * </pre>
 * 
 * <p>
 * So on the agent systems where you'd like to issue HTTPS requests you'll have 
 * to add this new certification authority to your chain, either at the user 
 * level (keystore location mentioned above) or you can do it at the system 
 * level for the machine by using the keytool to add this same certificate 
 * authority to the chain that everyone using that JDK would trust in (be aware 
 * this may not be the most secure way of doing things).
 * </p>
 */
public class ApacheHttpOp extends HttpOp {

    protected HttpClient client = null;
    protected final static MultiThreadedHttpConnectionManager connmgr = 
                                       new MultiThreadedHttpConnectionManager(); 
    
    @Override
    public void init() {
        connmgr.getParams().setConnectionTimeout(5000);
        connmgr.getParams().setDefaultMaxConnectionsPerHost(64);
        
        /*
         * Disabled the stale connection checking because it is quite the 
         * overhead on small operations and in the end we release all the 
         * open connections when an agent is released so no reason to worry 
         * about stale connections during runtime.
         */
        connmgr.getParams().setStaleCheckingEnabled(false);
        connmgr.getParams().setTcpNoDelay(true);
        client = new HttpClient(connmgr); 
    }
    
    @Override
    public void shutdown() {
        connmgr.closeIdleConnections(1);
    }
    
    /*
     * Remember that this is called in every execution to make sure the
     * configuration of the current action is correctly applied, so only 
     * configure the actual things that can change dynamically in the test.
     */
    private void config(HttpMethodBase method, HttpBase op) throws DTFException { 
        Http_config config = (Http_config) op.findFirstAction(Http_config.class);
        if ( config != null ) { 
            /*
             * Proxy settings
             */
            Proxy proxy = (Proxy) config.findFirstAction(Proxy.class);
            if ( proxy != null ) { 
                String host = proxy.getHost();
                int port = proxy.getPort();
                String username = proxy.getUsername();
                String password = proxy.getPassword();
               
                ProxyHost proxyhost = new ProxyHost(host,port);
                client.getHostConfiguration().setProxyHost(proxyhost);
                
                if ( username != null ) { 
                    HttpState state = client.getState();
                    UsernamePasswordCredentials upc = 
                             new UsernamePasswordCredentials(username,password);
                   
                    AuthScope as = new AuthScope(host,port);
                    state.setProxyCredentials(as, upc);
                    
                    client.setState(state);
                }
            }
            
            if ( config.getExpectcontinue() ) {
                method.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, true);
            }
        } 

        client.getHttpConnectionManager().getParams().setConnectionTimeout(op.getConnecttimeout());
    }
    
    private static String HTTP_DTFIS_CTX = "dtf.http.dtfis.ctx";
    
    protected void attachEntity(HttpBase op,
                                EntityEnclosingMethod method,
                                Event event)
            throws DTFException {
        Entity entity = (Entity) op.findFirstAction(Entity.class);
        
        if (entity != null) {
            DTFInputStream dtfis = entity.getEntityStream();

            if ( op.isPerfrun() ) {
                dtfis.setSaveData(false);
                if ( !op.getHash().equals("none") ) {
                    dtfis.setCalcHash(true);
                    dtfis.setHashAlgorithm(op.getHash());
                    event.addAttribute(HttpBase.HTTP_EVENT_HASH_ALGO,
                                       op.getHash());
                }
            } else { 
                dtfis.setSaveData(true);
            }
           
            /*
             * Register this context so we can later get the hash value or the 
             * original body from the DTFInputStream in the attachResponse 
             * method.
             */
            Action.registerContext(HTTP_DTFIS_CTX, dtfis);
          
            InputStream is = dtfis;
            
            if ( op.getBandwidth() != null )
                is = Throttler.wrapInputStream(dtfis,op.getBandwidth());
            
            InputStreamRequestEntity isre = 
                                  new InputStreamRequestEntity(is,
                                                               dtfis.getSize());
            
            method.setRequestEntity(isre);
            event.addAttribute(HttpBase.HTTP_EVENT_DATA_SIZE, dtfis.getSize());
        }
    }

    private void attachCookiesIn(HttpBase operation,
                                 HttpMethodBase method,
                                 Event event) throws ParseException {
        attachCookies(operation, method, event);
        
        ArrayList<Cookiegroup> groups = operation.findActions(Cookiegroup.class);
        for (int i = 0; i < groups.size(); i++) { 
            attachCookies(groups.get(i), method, event);
        }
    }
    
    private void attachCookies(Action action, 
                               HttpMethodBase method,
                               Event event) throws ParseException { 
        ArrayList<Cookie> cookies = action.findActions(Cookie.class);
        method.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);

        if ( cookies.size() != 0 ) {
            StringBuffer cookieString = new StringBuffer();
            for (int i = 0; i < cookies.size(); i++) { 
                /*
                 * By using apache's Cookie class we can also take advantage of the
                 * fact that they already have formatting of the cookies for 
                 * different specs like RFC2109, RFC2965 and Netscape. So I don't 
                 * have to maintain that formatting code.
                 */
                Cookie cookie = cookies.get(i);
                org.apache.commons.httpclient.Cookie acookie =
                        new org.apache.commons.httpclient.Cookie(cookie.getDomain(),
                                                                 cookie.getName(),
                                                                 cookie.getValue());
    
                acookie.setPath(cookie.getPath());
                acookie.setExpiryDate(cookie.getExpirydate());
                acookie.setComment(cookie.getComment());
                acookie.setVersion(cookie.getVersion());
                acookie.setSecure(cookie.getSecure());
              
                cookieString.append(acookie.toExternalForm() + "; ");
                event.addAttribute(HttpBase.HTTP_EVENT_COOKIE_IN + "." + cookie.getName(), 
                                   cookie.getValue());
            }

            method.addRequestHeader("Cookie", cookieString.toString());
        }
    }

    private void attachCookiesOut(HttpBase op,
                                  Action group,
                                  HttpMethodBase method,
                                  Event event) throws ParseException {
        org.apache.commons.httpclient.Cookie[] cookies =
                                                 client.getState().getCookies();
       
        for (int i = 0; i < cookies.length; i++) { 
            org.apache.commons.httpclient.Cookie cookie = cookies[i];
            event.addAttribute(HttpBase.HTTP_EVENT_COOKIE_OUT + "." + cookie.getName(),
                               cookie.getValue());
        }
    }

    private void attachHeaders(HttpBase op,
                               Action group,
                               HttpMethodBase method,
                               Event event) throws ParseException {
        ArrayList<com.yahoo.dtf.actions.http.Header> headersIn = 
                     group.findActions(com.yahoo.dtf.actions.http.Header.class);

        for (int i = 0; i < headersIn.size(); i++) {
            Header header = headersIn.get(i);
            String name = header.getName();
            String value = header.getValue();
            org.apache.commons.httpclient.Header reqHeader = 
                           new org.apache.commons.httpclient.Header(name,value);
            method.setRequestHeader(reqHeader);
           
            if ( method instanceof EntityEnclosingMethod )
                if ( name.equalsIgnoreCase("Transfer-Encoding") && 
                     value.equals("chunked") ) 
                    ((EntityEnclosingMethod)method).setContentChunked(true);
           
            // XXX: need to have a more elegant way of setting the virtual hos
            if ( name.equalsIgnoreCase("Host") )
                method.getParams().setVirtualHost(reqHeader.getValue());
           
            event.addAttribute(HttpBase.HTTP_EVENT_HEADER_IN + "." + 
                               header.getName(), header.getValue());
        }
        
        attachCookiesOut(op, group, method, event);
    }
    
    protected void attachHeadersIn(HttpBase op,
                                   HttpMethodBase method,
                                   Event event)
            throws ParseException {
        attachHeaders(op, op, method, event);
       
        ArrayList<com.yahoo.dtf.actions.http.Headergroup> headersgroup = 
                   op.findActions(com.yahoo.dtf.actions.http.Headergroup.class);
        
        
        for (int i = 0; i < headersgroup.size(); i++) {
            Headergroup group = headersgroup.get(i);
            attachHeaders(op, group, method, event);
        }
        
        attachCookiesIn(op, method, event);
    }

    protected void attachHeadersOut(HttpBase op, 
                                    HttpMethodBase method,
                                    Event event)
            throws ParseException {
        org.apache.commons.httpclient.Header[] headers = method
                .getResponseHeaders();
        
        for (int i = 0; i < headers.length; i++) {
            event.addAttribute(HttpBase.HTTP_EVENT_HEADER_OUT + "." + 
                               headers[i].getName(), headers[i].getValue());
        }
    }

    protected void attachResponseBody(HttpBase op, 
                                      HttpMethodBase method,
                                      Event event)
              throws DTFException {
        if ( !op.isPerfrun() ) {
            DTFInputStream dtfis = (DTFInputStream) 
                                              Action.getContext(HTTP_DTFIS_CTX);
            if ( dtfis != null ) { 
                Action.unRegisterContext(HTTP_DTFIS_CTX);
                event.addAttribute(HttpBase.HTTP_EVENT_DATA, dtfis.getData());
            }
            
            /*
             * Not using the getResponseAsString because the HttpClient will
             * generate some warnings in the logs about inefficiency of such
             * a thing.
             */
            try { 
                InputStream is = method.getResponseBodyAsStream();
               
                if ( op.getBandwidth() != null )
                    is = Throttler.wrapInputStream(is, op.getBandwidth());
                
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                // getResponseBodyAsStream returns null when there was an error
                // previously.
                int totalRead = 0;
                if ( is != null ) { 
                    int read = 0;
                    byte[] buffer = new byte[4*1024];
                    while (( read = is.read(buffer)) != -1) { 
                        baos.write(buffer,0,read);
                        totalRead+=read;
                    }
                }
                event.addAttribute(HttpBase.HTTP_EVENT_BODY, baos.toString());
                event.addAttribute(HttpBase.HTTP_EVENT_BODY_SIZE, totalRead);
            } catch (IOException e) { 
                throw new DTFException("Error handling output stream.",e);
            }
        } else { 
            /*
             * get the hash that was calculated previously and attach to http 
             * event.
             */
            DTFInputStream dtfis = (DTFInputStream)
                                              Action.getContext(HTTP_DTFIS_CTX);
            if ( dtfis != null && !op.getHash().equals("none") )  {
                event.addAttribute(HttpBase.HTTP_EVENT_DATA_HASH,
                                   dtfis.getHash());
            }
            Action.unRegisterContext(HTTP_DTFIS_CTX);
            
            // performance run we don't save the data but instead just the 
            // sha1 of the object.
            try { 
                InputStream is = method.getResponseBodyAsStream();

                if ( op.getBandwidth() != null )
                    is = Throttler.wrapInputStream(is,op.getBandwidth());
                
                MessageDigest md = null;
                boolean calchash = false;
                if ( op.isPerfrun() && !op.getHash().equals("none") ) {
                    md = MessageDigest.getInstance(op.getHash());
                    calchash = true;
                }
                
                // getResponseBodyAsStream returns null when there was an error
                // previously.
                int totalRead = 0;
                if ( is != null ) { 
                    int read = 0;
                    byte[] buffer = new byte[4*1024];
                    while (( read = is.read(buffer)) != -1) { 
                        if ( calchash ) 
                            md.update(buffer, 0, read);
                        totalRead+=read;
                    }
                }
                event.addAttribute(HttpBase.HTTP_EVENT_BODY_SIZE,
                                   totalRead);
                if ( calchash ) {
                    event.addAttribute(HttpBase.HTTP_EVENT_BODY_HASH, 
                                       HashUtil.convertToHex(md.digest()));
                    event.addAttribute(HttpBase.HTTP_EVENT_HASH_ALGO,
                                       op.getHash());
                }
            } catch (IOException e) { 
                throw new DTFException("Error handling output stream.",e);
            } catch (NoSuchAlgorithmException e) {
                throw new DTFException("Error handling output stream.",e);
            }
            
        }
    }
    
    protected void checkFailure(HttpBase op, 
                                HttpMethodBase method) throws DTFException {
        int status = method.getStatusCode();
        if (!op.continueOnFailure() && (status < 200 || status >= 400)) {
            throw new DTFException("Non successful HTTP Status, code "
                    + method.getStatusCode() + " cause: "
                    + method.getStatusText());
        }
    }

    protected void attachDefaultEvents(HttpBase op, 
                                       HttpMethodBase method,
                                       Event event)
            throws DTFException {
        event.addAttribute(HttpBase.HTTP_EVENT_URI, op.getUri());
        event.addAttribute(HttpBase.HTTP_EVENT_STATUS, method.getStatusCode());
        event.addAttribute(HttpBase.HTTP_EVENT_STATUSMSG, method.getStatusText());
    }


    protected void doOnFailure(HttpBase op, 
                               IOException e,
                               Event event) throws DTFException { 
        if (event.getStop() == -1)
            event.stop();
        
        if ( !op.continueOnFailure() ) {
            throw new DTFException("Error connecting to [" + op.getUri() + "]",e);
        } else { 
            event.addAttribute(HttpBase.HTTP_EVENT_URI, op.getUri());
           
            if ( e instanceof ConnectException ) { 
                event.addAttribute(HttpBase.HTTP_EVENT_STATUS, CONNECT_ERROR);
            } else if ( e instanceof SocketException ) { 
                event.addAttribute(HttpBase.HTTP_EVENT_STATUS, SOCKET_ERROR);
            } else if ( e instanceof UnknownHostException ) { 
                event.addAttribute(HttpBase.HTTP_EVENT_STATUS, UNKNOWHOST_ERROR);
            } else if ( e instanceof ConnectTimeoutException) {
                event.addAttribute(HttpBase.HTTP_EVENT_STATUS, CONTIMEOUT_ERROR);
            }  else { 
                // default
                event.addAttribute(HttpBase.HTTP_EVENT_STATUS, UNKNOWN_ERROR);
            }

            event.addAttribute(HttpBase.HTTP_EVENT_STATUSMSG, e.getMessage());
        }
    }
    
    @Override
    public Event executePost(HttpBase op) throws DTFException {
        PostMethod httppost = new PostMethod(op.getUri());
        config(httppost, op);
        
        Event event = new Event(HttpBase.HTTP_POST_EVENT);
        attachEntity(op, httppost, event);
        attachHeadersIn(op, httppost, event);

        try { 
            event.start();
            client.executeMethod(httppost);
            checkFailure(op, httppost);
            attachResponseBody(op, httppost, event);
            event.stop();
            
            attachDefaultEvents(op, httppost,event);
            attachHeadersOut(op, httppost, event);
        } catch (IOException e) { 
            doOnFailure(op, e, event);
        } finally { 
            httppost.releaseConnection();
        }
       
        return event;
    }
    
    @Override
    public Event executeDelete(HttpBase op) throws DTFException {
        DeleteMethod httpdelete = new DeleteMethod(op.getUri());
        config(httpdelete, op);

        Event event = new Event(HttpBase.HTTP_DELETE_EVENT);
        attachHeadersIn(op, httpdelete, event);
        
        try { 
            event.start();
            client.executeMethod(httpdelete);
            checkFailure(op, httpdelete);
            attachResponseBody(op, httpdelete, event);
            event.stop();
            
            attachDefaultEvents(op, httpdelete, event);
            attachHeadersOut(op, httpdelete, event);
        } catch (IOException e) { 
            doOnFailure(op, e, event);
        } finally { 
            httpdelete.releaseConnection();
        }
       
        return event;
    }
    
    @Override
    public Event executeHead(HttpBase op) throws DTFException {
        HeadMethod httphead = new HeadMethod(op.getUri());
        config(httphead, op);
        
        Event event = new Event(HttpBase.HTTP_HEAD_EVENT);
        attachHeadersIn(op, httphead, event);
        
        try { 
            event.start();
            client.executeMethod(httphead);
            checkFailure(op, httphead);
            attachResponseBody(op, httphead, event);
            event.stop();
            
            attachDefaultEvents(op, httphead, event);
            attachHeadersOut(op, httphead, event);
        } catch (IOException e) { 
            doOnFailure(op, e, event);
        } finally { 
            httphead.releaseConnection();
        }

        return event;
    }
    
    @Override
    public Event executeGet(HttpBase op) throws DTFException {
        GetMethod httpget = new GetMethod(op.getUri());
        config(httpget, op);
        
        Event event = new Event(HttpBase.HTTP_GET_EVENT);
        attachHeadersIn(op, httpget, event);
       
        try { 
            event.start();
            client.executeMethod(httpget);
            checkFailure(op, httpget);
            attachResponseBody(op, httpget, event);
            event.stop();
            
            attachDefaultEvents(op, httpget, event);
            attachHeadersOut(op, httpget, event);
        } catch (IOException e) { 
            doOnFailure(op, e, event);
        } finally { 
            httpget.releaseConnection();
        }
        
        return event;
    }
    
    @Override
    public Event executePut(HttpBase op) throws DTFException {
        PutMethod httpput = new PutMethod(op.getUri());
        config(httpput, op);
        
        Event event = new Event(HttpBase.HTTP_PUT_EVENT);
        attachEntity(op, httpput, event);
        attachHeadersIn(op, httpput, event);
        
        try { 
            event.start();
            client.executeMethod(httpput);
            checkFailure(op, httpput);
            attachResponseBody(op, httpput, event);
            event.stop();

            attachDefaultEvents(op, httpput, event);
            attachHeadersOut(op, httpput, event);
        } catch (IOException e) { 
            doOnFailure(op, e, event);
        } finally { 
            httpput.releaseConnection();
        }
       
        return event;
    }

    @Override
    public Event executeRequest(HttpBase op,
                                String method) 
           throws DTFException {
        HttpDTFMethod httpmethod = new HttpDTFMethod(op.getUri(), method);
        config(httpmethod, op);
        
        Event event = new Event("http." + method);
        attachEntity(op, httpmethod, event);
        attachHeadersIn(op, httpmethod, event);
        try { 
            event.start();
            client.executeMethod(httpmethod);
            checkFailure(op, httpmethod);
            attachResponseBody(op, httpmethod, event);
            event.stop();
            
            attachDefaultEvents(op, httpmethod, event);
            attachHeadersOut(op, httpmethod, event);
        } catch (IOException e) { 
            doOnFailure(op, e,event);
        } finally { 
            httpmethod.releaseConnection();
        }
        
        return event;
    }
}
