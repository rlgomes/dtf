package com.yahoo.dtf.debug;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.DebugServerException;
import com.yahoo.dtf.logger.DTFLogger;

public class DebugServer extends Thread {
    
    private static DTFLogger _log = DTFLogger.getLogger(DebugServer.class);
    private final static int MAX_RETRY = 50;
    
    private static DebugServer _instance = null;
    
    private ServerSocket _serversocket = null;
    private boolean _running = true;
    private DebugCLI cli = null;
    
    private int port = 40000;
    
    private DebugServer() throws DebugServerException { 
        int retry = 0;
        while ( retry < MAX_RETRY ) { 
	        try {
	            _serversocket = new ServerSocket(port,5);
	            break;
	        } catch (IOException e) {
	            port++;
	            retry++;
	        }
        }
        
        if ( _serversocket == null )
            throw new DebugServerException("Errors binding to server socket.");
   
        _log.info("Listening at 127.0.0.1:" + port);
    }
    
    public int getPort() { return port; } 
    
    public synchronized static DebugServer getInstance() throws DebugServerException {  
        if ( _instance == null ) { 
            _instance = new DebugServer();
            _instance.start();
        }
        return _instance;
    }
    
    public static DebugCLI getCLI() { return _instance.cli; }
    
    @Override
    public void run() {
        while ( _running ) { 
            Socket clientSocket = null;
            try {
                clientSocket = _serversocket.accept();
                cli = new DebugCLI(clientSocket);
                cli.parse();
            } catch (IOException e) {
                if ( _running ) 
                    _log.error("Client connection error.",e);
            } catch (DTFException e) {
                if ( _running ) {
                    e.printStackTrace();
                    cli.writeLine("ERROR: " + e.getMessage());
                }
            } catch (Throwable t) { 
                if ( _running ) {
                    t.printStackTrace();
                    cli.writeLine("ERROR: " + t.getMessage());
                }
            } finally { 
                if ( cli != null )
                    cli.close();
            }
        }
    }
   
    /**
     * Only a single DebugServer running in a given JVM
     * 
     * @throws DebugServerException
     */
    public synchronized static void init() throws DebugServerException { 
        if ( _instance == null ) { 
            _instance = getInstance();
        } else { 
            throw new DebugServerException("Trying to start DebugServer twice.");
        }
    }
    
    public static void shutdown() { 
        if ( _instance != null ) { 
	        _instance._running = false;
	        try {
	            if ( _instance.cli != null )
	                _instance.cli.shutdown();
	            
	            _instance._serversocket.close();
	        } catch (IOException e) {
	            _log.error("Issues shutting down server socket.",e);
	        }
	        try {
	            _instance.join();
	        } catch (InterruptedException ignore) { }
        }
    }
}
