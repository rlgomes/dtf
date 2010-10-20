package com.yahoo.dtf.debug;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.DTFProperties;
import com.yahoo.dtf.logger.DTFLogger;
import com.yahoo.dtf.util.HostUtils;
import com.yahoo.dtf.util.SocketUtil;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.protocol.deploy.Dtfa;
import com.yahoo.dtf.comm.Comm;
import com.yahoo.dtf.debug.xmltrace.XMLTrace;
import com.yahoo.dtf.deploy.DeployDTF;
import com.yahoo.dtf.deploy.SSHUtil;
import com.yahoo.dtf.exception.CLIException;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.feature DTF Debug Server
 * @dtf.feature.group Debugging
 * @dtf.feature.desc 
 * <p>
 * Debugging a running component in DTF is done by connecting to the DebugServer
 * that is started on each and every component at startup. You'll see a log line
 * like so at the startup that will give you the information you need to debug 
 * what is going on inside that component:
 * </p>
 * 
 * <pre>
 * DebugServer     - Listening at localhost:40000 
 * </pre>
 * 
 * <p>
 * With the above port you can now "telnet localhost 40000" and you'll be 
 * greeted with the following prompt:
 * </p>
 * 
 * <pre> 
 * > telnet localhost 40000 
 * Trying 127.0.0.1... 
 * Connected to localhost. 
 * Escape character is '^]'.
 * DTF DebugServer, type ? for help 
 * </pre>
 * 
 * <p>
 * Some of the commands include, the <i>xmltrace</> command that will give you 
 * the following output that basically lets you know where in the XML you're 
 * currently executing Actions:
 * </p>
 * 
 * <pre>
 * --------------------------------------------------------------------------------
 *  Thread ID   |              XML Trace              |      Action State      
 * --------------------------------------------------------------------------------
 *    main      |      tests/ut/mytest.xml:23,55      | Component  {id=DTFA1}
 * --------------------------------------------------------------------------------
 * </pre>
 * 
 * <p>
 * This trace as expected shows that the runner is currently waiting on the 
 * action component which is talking to the component you previously locked 
 * called DTFA1. Now we can connect the DebugServer on that component and check 
 * out what the xmltrace looks like on that side:
 * </p>
 * 
 * <pre>
 * --------------------------------------------------------------------------------
 *   Thread ID   |              XML Trace              |      Action State      
 * --------------------------------------------------------------------------------
 *   Thread-35   |      tests/ut/mytest.xml:24,55      | Parallel  {}
 *   Thread-36   |      tests/ut/mytest.xml:25,55      | Sleep  {duration=5m}
 *   Thread-37   |      tests/ut/mytest.xml:26,55      | Sleep  {duration=5m}
 * --------------------------------------------------------------------------------
 * </pre>
 * 
 * <p>
 * So on the component side we can easily see that the component is waiting on 
 * two sleeps that were in a parallel on line 24 of our test. With this any DTF 
 * user can easily identify at what point the their test is running and which 
 * threads are doing what. This ability makes it possibly to identify which 
 * actions are actually stuck and easily identify if there is a certain pattern 
 * as to the actions that are getting stuck to better understand if the problem
 * at hand is in the client libraries being tested or in the server side of the 
 * product being tested.
 * </p>
 * 
 * <p>
 * You can also control the logging level for the whole JVM or set the logging 
 * level per class name, this is extremely useful since you can change the 
 * logging level to see what is going on in case of issues and easily change it 
 * back at runtime.
 * </p>
 */
public class DebugCLI {

    private Socket _socket = null;
    
    private BufferedReader _reader = null;
    private PrintWriter _writer = null;
    
    private DebugLogger _logger = null;
    
    public DebugCLI(Socket socket) throws IOException {
        _socket = socket;
        
        InputStreamReader isr = new InputStreamReader(_socket.getInputStream());
        _reader = new BufferedReader(isr);
      
        OutputStream os = _socket.getOutputStream();
        _writer = new PrintWriter(os);
        
        _logger = new DebugLogger(_writer, Logger.getLogger(DebugCLI.class));
    }
    
    public void shutdown() { 
        // close up all existing sessions.
        for (Entry<String,Session> entry : _sessions.entrySet())
            entry.getValue().disconnect();
        
        writeLine("\nDebugServer shutting down.");
        close();
    }
    
    public void close() { 
        try { 
            _socket.close();
        } catch (IOException e) {
            if ( Action.getLogger().isDebugEnabled() ) 
                Action.getLogger().debug("Error closing DebugCLI.",e);
        }
    }
    
    private static int rport = 30000;
   
    private static HashMap<String, Session> _sessions = new HashMap<String, Session>();
    public static void cleanUpSession(String host) { 
        Session session = _sessions.remove(host);
        if ( session != null ) session.disconnect();
    }
    
    private static ArrayList<String> _tunneledback = new ArrayList<String>();
    
    public void parse() throws DTFException { 
        writeLine("DTF DebugServer, type ? for help");
       
        String line = null;
        while ( (line = readLine()) != null ) { 
            String[] args = line.split(" ");
            String command = args[0];
           
            if ( command.equals("?") ) { 
                help();
                continue;
            }
            
            if ( command.equals("xmltrace") ) {
                XMLTrace.writeXMLTrace(_writer);
                continue;
            }
            
            if ( command.equals("loglevel") ) {
                if ( args.length < 2 ) {
                    writeLine("need to specify at least the log level.");
                } else { 
	                String level = args[1];
	                int l = -1;
	                if ( level.equalsIgnoreCase("info") ) { 
	                    l = DTFLogger.INFO;
	                } else if ( level.equalsIgnoreCase("error") ) { 
	                    l = DTFLogger.ERROR;
	                } else if ( level.equalsIgnoreCase("warn") ) { 
	                    l = DTFLogger.WARN;
	                } else if ( level.equalsIgnoreCase("debug") ) { 
	                    l = DTFLogger.DEBUG;
	                } else { 
	                    writeLine("Unknown log level [" + level + "]");
	                }
	               
	                if ( args.length == 3 ) {
	                    String cname = args[2];
		                DTFLogger.setLoggingLevel(cname, l);
		                writeLine("Log level set to [" + level + 
		                          "] for class [" + cname + "]");
	                } else { 
		                DTFLogger.setLoggingLevel(l);
		                writeLine("Log level set to [" + level + "]");
	                }
                }
                continue;
            }
            
            if ( command.equals("shutdown") ) { 
                writeLine("Shutting down DTF node...");
                DTFNode.stop();
                continue;
            }

            if ( command.equals("status") ) { 
                DTFNode.status(_writer);
                continue;
            }

            if ( command.equals("start") ) { 
                // use the same deploy code used by deploy dtf to deploy the 
                // agent and runner's from here but be careful that we pass
                // tunnel information correctly

                if ( args.length < 3 ) {
                    writeLine("Not enough arguments.");
                } else { 
	                // start component host user path x=y,z=a,etc.
                    DeployDTF.initJar();
                    
	                String comp = args[1];
	                String host = args[2];
	                String user = args[3];
	                String logn = args[4];
	                String path = (args.length > 5 ? args[5] : null);
	                String rsakey = (args.length > 6 ? args[6] : null);
	                String passphrase = (args.length > 7 ? args[7] : null);
	                String wrapcmd = (args.length > 8 ? args[8] : null);
	               
	                // stupid trick to allow the path to be set to null
	                path = ( path == null || path.equals("null") ? null : path );
	                rsakey = ( rsakey == null || rsakey.equals("null") ? null : rsakey );
	                passphrase = ( passphrase == null || passphrase.equals("null") ? null : passphrase );

	                try { 
	                    // stupid trick to allow the path to be set to null
	                    wrapcmd = ( wrapcmd == null || wrapcmd.equals("null") ? null : URLDecoder.decode(wrapcmd,"UTF8"));
	                    
	                    String propstring = ( args.length > 9 ? URLDecoder.decode(args[9],"UTF8") : null);
	                    String[] props = (propstring == null ? new String[0] : propstring.split(","));
	                    
	                    HashMap<String, String> properties = 
	                                              new HashMap<String, String>();
	                    for (String part : props) { 
	                        String[] parts = part.split("=");
	                        properties.put(parts[0], parts[1]);
	                    }

	                    int lport = Action.getConfig().getPropertyAsInt(DTFProperties.DTF_LISTEN_PORT);
	                  
	                    if ( !HostUtils.isLocal(host) ) { 
	                        _logger.info("Tunneling [" + host + "] over ssh.");
	                        // find next available port
	                        while ( SocketUtil.isPortOpen(++rport));
	                        
		                    Session session = SSHUtil.connectToHost(host,
		                                                            user,
		                                                            rsakey,
		                                                            passphrase);
		                    _sessions.put(host + rport, session);
		                    
		                    // SSH Tunnel on local rport for dtfc to connect to 
		                    // component
                            session.setPortForwardingL(rport,"127.0.0.1",rport);
	                        _logger.info("localhost:" + rport + " to " + 
	                                     host + ":" + rport);
	                    
	                        
		                    if ( !_tunneledback.contains(host) ) { 
		                        _logger.info("Setting up tunnel back to the dtfc on " +
		                                     host + ":" + lport);
			                    // SSH Tunnel for other component to connect to DTFC
		                        session.setPortForwardingR(lport,
		                                                   "127.0.0.1",
		                                                   lport);
			                    _tunneledback.add(host);
		                    } else { 
		                        _logger.info("Already tunneled back to the dtfc.");
		                    }
	                        
		                    Comm.addTunnel(host, rport, rport);
		                    
		                    properties.put("dtf.connect.addr","127.0.0.1");
	
		                    properties.put("dtf.tunneled","true");
		                    properties.put("dtf.listen.port",""+rport);
	                    } 
	                    properties.put("dtf.listen.addr",""+host);
	                    properties.put("dtf.connect.port","" + lport);

	                    Dtfa dtfa = new Dtfa();
	                    dtfa.setHost(host);
	                    dtfa.setUser(user);
	                    dtfa.setPath(path);
	                    dtfa.setRsakey(rsakey);
	                    dtfa.setWrapcmd(wrapcmd);
	                    
	                    DeployDTF.startup(comp, dtfa, properties, logn, _logger);
	                } catch (JSchException e) { 
	                    writeLine(e.getMessage());
	                    throw new DTFException("Authentication issues can be solved by " + 
	                              "running the ant setup-ssh -Dsetup.host=xxx -Dsetup.user=yyy, "
	                              + "before trying to a dtf component on the " +
	                              "other system.",e);
	                } catch (IOException e) {
	                    e.printStackTrace();
	                    writeLine("ERROR: " + e.getMessage());
	                    return;
                    } catch (SftpException e) {
	                    e.printStackTrace();
	                    writeLine("ERROR: " + e.getMessage());
	                    return;
                    }
                }
                    
                continue;
            }
            
            if ( command.equals("quit") ) { 
                writeLine("Bye");
                try {
                    _socket.close();
                } catch (IOException e) {
                    throw new CLIException("Unable to close CLI session.",e);
                }
                break;
            }
            
            writeLine("Unkown command [" + command + "]");
        }
    }
   
    public void writeLine(String line) {
        _writer.write(line + "\n");
        _writer.flush();
    }
    
    public String readLine() throws CLIException {
        _writer.write("# ");
        _writer.flush();
        try {
            return _reader.readLine();
        } catch (IOException e) {
            throw new CLIException("Unable to read from CLI.",e);
        }
    }
    
    private void help() {
        writeLine("Available commands:");
        writeLine("    ?     - displays this help menu.\n");
        writeLine("   quit   - quits this session.\n");
        writeLine(" xmltrace - displays the DTF XML trace for all currently executing DTF Actions.\n");
        writeLine(" loglevel info|error|debug|warn [x.y.z.Class]");
        writeLine("          - set the current loglevel, levels are info, debug, warn, error.");
        writeLine("            ex: loglevel info\n");
        writeLine("  status  - the status of this DTF node (dtfc shows all node statuses).\n");
    }
}
