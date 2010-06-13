package com.yahoo.dtf.junit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import junit.framework.JUnit4TestAdapter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.DTFProperties;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.DebugServerException;
import com.yahoo.dtf.util.ThreadUtil;

/**
 *
 * @author rlgomes
 */
public class DebugServerSuite extends DTFJUnitTest {
   
    @BeforeClass
    public static void startUpNode() throws DTFException { 
        NodeRunner.startupNode("dtfc", "dummy");
    }
    
    @AfterClass
    public static void stopNode() { 
        DTFNode.stop();
    }
    
    private String readTillPrompt(BufferedReader reader, char prompt) throws IOException { 
        int read = -1;
        StringBuffer buffer = new StringBuffer();
        while ( (read = reader.read()) != -1 && read != prompt )
            buffer.append((char)read);
       
        // read the space
        if ( read == prompt )
            reader.read();
       
        return buffer.toString();
    }
   
    @Test(timeout=600000)
    public void simpleConnect() throws DTFException { 
        try {
            Socket socket = new Socket("127.0.0.1", 40000);
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
     
            writer.println("");
            writer.flush();
            readTillPrompt(reader, '#');
            readTillPrompt(reader, '#');
            /*
             * print the help menu
             */
            writer.println("?");
            writer.flush();
            String response = readTillPrompt(reader, '#');
            getLogger().info("Help Menu: ");
            getLogger().info(response);
            
            /*
             * print the current xmltrace
             */
            writer.println("xmltrace");
            writer.flush();
            response = readTillPrompt(reader, '#');
            getLogger().info("Current xmltrace: ");
            getLogger().info(response);
            
            /*
             * turn logging levels on and off 
             */
            writer.println("loglevel debug");
            writer.flush();
            response = readTillPrompt(reader, '#');
            getLogger().info("loglevel response: ");
            getLogger().info(response);

            writer.println("loglevel warn");
            writer.flush();
            response = readTillPrompt(reader, '#');
            getLogger().info("loglevel response: ");
            getLogger().info(response);

            writer.println("loglevel error");
            writer.flush();
            response = readTillPrompt(reader, '#');
            getLogger().info("loglevel response: ");
            getLogger().info(response);

            writer.println("loglevel info");
            writer.flush();
            response = readTillPrompt(reader, '#');
            getLogger().info("loglevel response: ");
            getLogger().info(response);
            
            /*
             * turn on the shell mode and poke at some of the existing
             * functionality.
             */
            getLogger().info("Will now interact with the shell mode and run some commands.");
            String[] commands = new String[]{"shell",
                                             "dtf.help()",
                                             "dtf.threads()",
                                             "main = dtf.thread('main')",
                                             "dtf.methods(main)",
                                             "dtf.dmethods(main,'get.*')",
                                             "dtf.dmethods(main,'set.*')",
                                             "state = dtf.state('main')",
                                             "dtf.dmethods(state,'get.*')"
                                            };

            for (int i = 0; i < commands.length; i++) { 
	            writer.println(commands[i]);
	            writer.flush();
	            response = readTillPrompt(reader, '#');
	            System.out.println("# " + commands[i]);
	           
	            if ( response.charAt(response.length()-1) == '\n' ) 
	                response = response.substring(0,response.length()-1);
	            
	            System.out.println(response);
            }

            /*
             * quit shell mode
             */
            writer.println("//end");
            System.out.println("# //end");
            writer.flush();
            response = readTillPrompt(reader,'#');
            System.out.println(response);
            
            /*
             * quit the debug server session.
             */
            writer.println("quit");
            writer.flush();
            response = readTillPrompt(reader,'#');
            getLogger().info("loglevel response: ");
            getLogger().info(response);
        } catch (UnknownHostException e) {
            throw new DebugServerException("Error talking to DebugServer.", e);
        } catch (IOException e) {
            throw new DebugServerException("Error talking to DebugServer.", e);
        }
    }
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DebugServerSuite.class);
    }
}
