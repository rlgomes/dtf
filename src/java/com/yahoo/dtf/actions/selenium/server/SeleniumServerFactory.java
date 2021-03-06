package com.yahoo.dtf.actions.selenium.server;

import java.util.HashMap;
import java.util.Map.Entry;

import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.SeleniumServer;

import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.NodeShutdownHook;
import com.yahoo.dtf.exception.DTFException;

/**
 * Simple factory responsible for starting and stopping selenium remote server
 * instances.
 * 
 * @author rlgomes
 */
public class SeleniumServerFactory {
    
    private static HashMap<String, SeleniumServer> ssmap = 
                                          new HashMap<String, SeleniumServer>();
    
    static { 
        DTFNode.registerShutdownHook(new NodeShutdownHook() {
            @Override
            public void shutdown() {
                for (Entry<String,SeleniumServer> entry : ssmap.entrySet() ) { 
                    entry.getValue().stop();
                }
            } 
        });
    }

    /**
     * Starts a selenium server if none is already running at the specific port
     * requested.
     * 
     * @param host
     * @param port
     * @throws DTFException 
     */
    public synchronized static void startServer(int port) throws DTFException { 
        if ( !ssmap.containsKey(""+port) ) {
	        RemoteControlConfiguration rcc = new RemoteControlConfiguration();
	        rcc.setPort(port);
	        SeleniumServer server;
            try {
                server = new SeleniumServer(rcc);
                server.start();
            } catch (Exception e) {
                throw new DTFException("Issue starting selenium server",e);
            }
	        ssmap.put(""+port, server);
        }
    }
}
