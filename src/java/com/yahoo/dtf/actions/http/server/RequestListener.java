package com.yahoo.dtf.actions.http.server;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.logger.DTFLogger;

public class RequestListener extends Thread {

    private static DTFLogger _logger = DTFLogger.getLogger(RequestListener.class);
    
    private ServerSocket serversocket = null;
    private HttpParams params = null;
    private HttpService httpService = null;
    private HttpRequestHandlerRegistry registry = null; 
    private ExecutorService executors = null;
    
    private boolean _down = false;

    public RequestListener(int port, int threads) throws DTFException {
        try { 
	        serversocket = new ServerSocket(port);
	        params = new BasicHttpParams();
	        
	        params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
            params.setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 4 * 1024);
            params.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false);
            params.setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true);
            params.setParameter(CoreProtocolPNames.ORIGIN_SERVER, "HttpComponents/1.1");
	        
	        BasicHttpProcessor httpproc = new BasicHttpProcessor();
	
	        // Set up request handlers
	        registry = new HttpRequestHandlerRegistry();
	
	        // Set up the HTTP service
	        httpService = new HttpService(httpproc,
	                                      new DefaultConnectionReuseStrategy(),
	                                      new DefaultHttpResponseFactory());
	        httpService.setParams(params);
	        httpService.setHandlerResolver(registry);
	       
	        executors = Executors.newFixedThreadPool(threads);
        } catch (IOException e) { 
            throw new DTFException("Unable to start up listener.",e);
        }
    }
    
    public void addHttpListener(String path, DTFHttpHandler handler) { 
        registry.register(path, handler);
    }

    public void run() {
        while (!Thread.interrupted()) {
            try {
                // Set up HTTP connection
                Socket socket = serversocket.accept();
                DefaultHttpServerConnection conn = 
                                              new DefaultHttpServerConnection();
                conn.bind(socket, params);
                
                // Start worker thread
                executors.execute(new HTTPRunnable(httpService, conn, this));
            } catch (InterruptedIOException e) {
                break;
            } catch (IOException e) {
                if ( !Thread.interrupted() ) 
                    _logger.error("I/O error initialising connection thread.",e);
                break;
            }
        }
    }
    
    public boolean isDown() { return _down; }
    
    public void shutdown() { 
        interrupt();
        _down = true;
        try {
            serversocket.close();
            executors.shutdown();
        } catch (IOException ignore) { }
    }
}