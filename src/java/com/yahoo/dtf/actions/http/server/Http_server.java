package com.yahoo.dtf.actions.http.server;

import java.util.ArrayList;
import java.util.HashMap;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag http_server
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc This tag will startup an HTTP server and bind it to the 
 *               host and port you specify. It will then create HTTP listeners
 *               for the path and methods you specify and allow you to receive
 *               each of the requests and do something with it. Now currently 
 *               each listener has as many threads as requests can come in, so 
 *               there is no throttling or pooling of request handling threads. 
 *               We will add this in the near future when it becomes a problem.
 *               <br/>
 *               The other thing to note bout the http_server tag is that it 
 *               doe snot currently allow you to define any HTTP config values
 *               but can easily be adapted to do so in the near future. 
 *               
 * @dtf.tag.example
 * <http_server port="8080">
 *     <http_listener path="/oddtest" method="PUT">
 *          <mod op1="${http.put.headers.number}" op2="2" result="result"/>
 *          <if>
 *              <eq op1="${result}" op2="0"/>
 *              <then>
 *                  <http_response status="200"
 *                                 message="${http.put.headers.number} is even"/>
 *              </then> 
 *              <else>
 *                  <http_response status="200" 
 *                                 message="${http.put.headers.number} is odd"/>
 *              </else>
 *         </if>
 *     </http_listener>
 * </http_server>
 * 
 * @dtf.tag.example 
 * <http_server port="8082">
 *      <http_listener path="/echo-data" method="PUT">
 *           <log>received [${http.put.body}] in the HTTP body</log>
 *           <log>received headers [${http.headers}]</log>
 *      </http_listener>
 * </http_server>
 *
 * @dtf.tag.example 
 * <http_server port="8082" command="stop"/>
 * 
 */
public class Http_server extends Action {
    
    public final static String HTTP_SERVER_CTX = "dtf.http.server.ctx";

    /**
     * @dtf.attr host
     * @dtf.attr.desc The hostname to bind to, incase your machine has multiple
     *                interfaces or you would rather bind to localhost to not 
     *                allow external systems to use/attack your HTTP server.
     */
    private String host = null;
    
    /**
     * @dtf.attr port
     * @dtf.attr.desc The port number to bind to, by default set to 80.
     */
    private String port = null;
    
    /**
     * @dtf.attr command
     * @dtf.attr.desc What operation to do on the HTTP server identified by the
     *                key hostname,port. The two existing options are 'start' 
     *                and 'stop' being that by default command is set to try
     *                and start up the HTTP server described.
     */
    private String command = null;
   
    /**
     * @dtf.attr threads
     * @dtf.attr.desc this property specifies the number of maximum threads to 
     *                use at any given time to handle incoming requests to this
     *                HTTP server.
     */
    private String threads = null;
 
    private static Object _lock = new Object();
    
    protected static HashMap<String, RequestListener> getHttpServers() { 
        synchronized (_lock) { 
	        HashMap<String, RequestListener> listeners = 
	                          (HashMap<String, RequestListener>) 
	                                          getGlobalContext(HTTP_SERVER_CTX);
	       
	        if ( listeners == null ) { 
	            listeners = new HashMap<String, RequestListener>();
	            registerGlobalContext(HTTP_SERVER_CTX, listeners);
	        }
	       
	        return listeners;
        }
    }
    
    @Override
    public void execute() throws DTFException {
        HashMap<String, RequestListener> listeners = getHttpServers();
        String key = getHost() + ":" + getPort();
        RequestListener rl = listeners.get(key);         
        
        if ( command.equals("stop") ) {  
            if ( rl == null ) 
                throw new DTFException("No server started at [" + key + "]");

	        getLogger().info("Stopping [" + key + "]");
	        rl.shutdown();
            listeners.remove(key);
        } else if ( command.equals("start") ){ 
	        if ( rl != null )
	            throw new DTFException("Already have a server at [" + key + "]");
	        
	        rl = new RequestListener(getPort(),getThreads());
	       
	        ArrayList<Http_listener> httplisteners = findActions(Http_listener.class);
	        for (int i = 0; i < httplisteners.size(); i++) { 
	            Http_listener listener = httplisteners.get(i);
	         
	            DTFHttpHandler handler = new DTFHttpHandler(listener.getMethod(),
	                                                        listener);
	            
	            rl.addHttpListener(listener.getPath(), handler);
	        }
	        
	        rl.start();
	        try {
	            listeners.put(key,rl);
	            getLogger().info("Starting [" + key + "]");
	            rl.join();
	        } catch (InterruptedException e) {
	            throw new DTFException("Server thread interrupted.",e);
	        }
        } else {
            throw new DTFException("Uknown HTTP server command [" + 
                                   getCommand() + "]");
        }
    }

    public String getHost() throws ParseException { return replaceProperties(host); }
    public void setHost(String host) { this.host = host; }

    public int getPort() throws ParseException { return toInt("port", port); }
    public void setPort(String port) { this.port = port; }

    public String getCommand() throws ParseException { return replaceProperties(command); }
    public void setCommand(String command) { this.command = command; }

    public int getThreads() throws ParseException { return toInt("threads",threads); }
    public void setThreads(String threads) { this.threads = threads; }
}
