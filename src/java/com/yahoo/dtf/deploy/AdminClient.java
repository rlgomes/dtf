package com.yahoo.dtf.deploy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.yahoo.dtf.exception.DebugServerException;
import com.yahoo.dtf.logger.DTFLogger;

public class AdminClient {

    private DTFLogger logger = null;

    private Socket socket = null;
    
    private BufferedReader reader = null;
    
    private OutputStream writer = null;
    
    public AdminClient(String host, int port, DTFLogger logger) throws DebugServerException { 
        this.logger = logger;
        
        try {
            socket = new Socket(host, port);
        } catch (UnknownHostException e) {
            throw new DebugServerException("Unable to connect to DebugServer.", e);
        } catch (IOException e) {
            throw new DebugServerException("Unable to connect to DebugServer.", e);
        }
        
        try {
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(isr);
            writer = socket.getOutputStream();
        } catch (UnknownHostException e) {
            throw new DebugServerException("Error talking to DebugServer.", e);
        } catch (IOException e) {
            throw new DebugServerException("Error talking to DebugServer.", e);
        }

        try { 
            int read = -1;
            while ((read = reader.read()) != -1) {
                if ( ((char)read) == '#' ) {
                    reader.read(); // read space
                    break;
                }
            }
        } catch (IOException e) { 
            throw new DebugServerException("Error connecting to DebugServer",e);
        }
    }
    
    public void execute(String command) throws DebugServerException { 
        try { 
            writer.write((command + "\n").getBytes());
            writer.flush();
            
            int read = -1;
            StringBuffer line = new StringBuffer();
            while ((read = reader.read()) != -1) {
                if ( ((char)read) == '#' ) {
                    reader.read(); // read space
                    break;
                }
                
                if ( ((char)read) == '\n' ) {
                    if ( line.toString().startsWith("ERROR:")) { 
                        logger.error(line.toString());
                        throw new DebugServerException("Failure to launch, see more information above.");
                    }
                    logger.info(line.toString());
                    line = new StringBuffer();
                } else { 
                    line.append((char)read);
                }
            }
        } catch (IOException e) { 
            throw new DebugServerException("Error connecting to DebugServer",e);
        }
    }
    
    public void close() throws DebugServerException { 
        try { 
            socket.close();
        } catch (IOException e) { 
            throw new DebugServerException("Error closing connection to DebugServer",e);
        }
    }
}