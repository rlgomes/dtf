package com.yahoo.dtf.comm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

import com.yahoo.dtf.comm.Comm;
import com.yahoo.dtf.comm.CommClient;
import com.yahoo.dtf.comm.CommServer;
import com.yahoo.dtf.DTFConstants;
import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.DTFProperties;
import com.yahoo.dtf.NodeInfo;
import com.yahoo.dtf.NodeShutdownHook;
import com.yahoo.dtf.NodeState;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.component.Component;
import com.yahoo.dtf.actions.flowcontrol.Sequence;
import com.yahoo.dtf.comm.rpc.Node;
import com.yahoo.dtf.config.Config;
import com.yahoo.dtf.debug.DebugServer;
import com.yahoo.dtf.exception.CommException;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.PropertyException;
import com.yahoo.dtf.logger.DTFLogger;
import com.yahoo.dtf.util.HostUtils;
import com.yahoo.dtf.util.SystemUtil;
import com.yahoo.dtf.util.ThreadUtil;

public class Comm extends Thread implements NodeShutdownHook {
   
    private static DTFLogger _logger = DTFLogger.getLogger(Comm.class);
  
    private static final String  TUNNEL_CONF_FILE = "tunnel.conf";
    /*
     * 5s  heart beat all registered components every 5s, now if a heart beat
     * takes 50ms then in 5s we are able to heart beat at least 1000 active 
     * components and for now that "limitation" is acceptable and well 
     * understood.
     */
    private long HEARTBEAT_INTERVAL       = 5000; 
    
    /*
     * Maximum amount of time that we'll tolerate without heart beating a 
     * component is 20s
     */
    private long HEARTBEAT_TIMEOUT        = 20000; 
    
    private CommServer _server = null;

    private boolean _running = true;
    
    private static HashMap<String, Integer> _tunnels = null;
    public static HashMap<String, Integer> getTunnels() { return _tunnels; } 
    public static void addTunnel(String host, int rport, int lport) { 
        _logger.info("Tunnel added for " + host + ":" + rport + ":" + lport);
        _tunnels.put(host+rport,lport);
    }
    public static boolean tunnelExists(String host, int rport) { 
        return _tunnels.containsKey(host + rport);
    }
    
    private static boolean _tunneled = false;
    public static boolean isTunneled() { return _tunneled; }

    static { 
        /*
         * Start off by loading any tunneling information into memory. This 
         * information will be passed around to all nodes connected to this
         * controller and therefore everyone will know about all existing
         * tunnels.
         */
        _tunnels = new HashMap<String, Integer>();
        if (new File(TUNNEL_CONF_FILE).exists()) { 
            FileInputStream fis = null;
            Properties props = null;
            _tunneled = true;
            
            try { 
                fis = new FileInputStream(TUNNEL_CONF_FILE);
                props = new Properties();
                props.load(fis);
               
                Enumeration hosts = props.keys();
                while (hosts.hasMoreElements()) { 
                    String host = (String) hosts.nextElement();
                    Integer port = null;
                    String portstring = props.getProperty(host);
     
                    if ( portstring.indexOf('=') != -1 ) {
                        String[] ports = portstring.split("=");
                        port = new Integer(ports[0]);
                    } else {
                        port = new Integer(portstring);
                    }
                        
                    host = InetAddress.getByName(host).getHostAddress();
                    addTunnel(host, port, port);
                }
            } catch (IOException e) { 
                throw new RuntimeException("Unable to open/parse " + 
                                           TUNNEL_CONF_FILE + " file.",e);
            } finally { 
                if ( fis != null ) { 
                    try { 
                        fis.close();
		            } catch (IOException e) { 
		                throw new RuntimeException("Unable to close tunnel file.",e);
		            }
                }
            }
        } 
    }
    
    public Comm(Config config) throws DTFException {
        String heartbeat = config.getProperty("dtf.heartbeat.timeout");
        if (heartbeat != null) { 
            _logger.info("Changing default hearbeat timeout to " + heartbeat + "ms");
            HEARTBEAT_TIMEOUT = new Long(heartbeat).longValue();
        }
        
        String laddr = config.getProperty(DTFProperties.DTF_LISTEN_ADDR);
        int lport = -1;
        try {
            lport = config.getPropertyAsInt(DTFProperties.DTF_LISTEN_PORT,-1);
        } catch (PropertyException e) {
            throw new CommException("Port number bad format.",e);
        }
        
        if (laddr == null) { 
            laddr = HostUtils.getHostAddress();
            config.setProperty(DTFProperties.DTF_LISTEN_ADDR, laddr);
        }
        _logger.info("Host address [" + laddr + "]");
        
        String type = config.getProperty(DTFProperties.DTF_NODE_TYPE);
        
        if (type == null) 
            throw new CommException(DTFProperties.DTF_NODE_TYPE + 
                                    " can not be null.");
       
        _server = new CommRMIServer(laddr,lport);
       
        try {
            _server.start();
        } catch (CommException e) {
            throw new CommException("Unable to start CommServer.", e);
        }
        
        // Because the port can be selected by the RPCServer when the chosen 
        // one is not available
        config.setProperty(DTFProperties.DTF_LISTEN_PORT,""+_server.getPort());
        
        if (type.equalsIgnoreCase("dtfc")) {
            // DTFC node
            _logger.info("DTFC Setup."); 
            // Controller node with Controller handler available
            _server.addHandler("node", new Node()); 
            Action.getState().disableReplace();
        } else if (type.equalsIgnoreCase("dtfa")) { 
            // DTFA node
            _logger.info("DTFA Setup."); 
            // heart beat handler for DTFC to be able to check up on each agent
            _server.addHandler("node", new Node()); 
        } else { 
            // DTFX node
            _logger.info("DTFX Setup."); 
            // Any other DTF node has the basic heart beat handler up 
            _server.addHandler("node", new Node()); 
        }

        CommClient.addAgentAttribute(DTFProperties.DTF_NODE_TYPE,
                                     DTFNode.getType());
        CommClient.addAgentAttribute(DTFProperties.DTF_NODE_OS, 
                                     System.getProperty("os.name"));
        CommClient.addAgentAttribute(DTFProperties.DTF_NODE_OS_ARCH,
                                     System.getProperty("os.arch"));
        CommClient.addAgentAttribute(DTFProperties.DTF_NODE_OS_VER,
                                     System.getProperty("os.version"));
        CommClient.addAgentAttribute(DTFProperties.DTF_DEBUG_PORT,
                                     DebugServer.getInstance().getPort());
        CommClient.addAgentAttribute("dtf.node.host", laddr); 
        CommClient.addAgentAttribute("dtf.node.user", 
                                     System.getProperty("user.name")); 
        CommClient.addAgentAttribute("dtf.node.home", SystemUtil.getCWD());
        
        if (!type.equalsIgnoreCase("dtfc")) {
            /*
             * only DTFA's will register automatically to the DTFC 
             */
            if (type.equalsIgnoreCase("dtfa")) { 
                checkAndConnectToDTFC();
            }
        }
        
        DTFNode.registerShutdownHook(this);
    }
   
    private boolean _connected = false;
    private Object _lockComm = new Object();
    public boolean isConnected() { return _connected; }
    public synchronized void checkAndConnectToDTFC() throws DTFException { 
        synchronized(_lockComm) { 
	        if (!_connected) { 
	            Config config = Action.getConfig();
		        String caddr = config.getProperty(DTFProperties.DTF_CONNECT_ADDR,
		                                              DTFProperties.DTF_CONNECT_ADDR_DEFAULT);
		        int cport = -1;
		        try {
		            cport = config.getPropertyAsInt(DTFProperties.DTF_CONNECT_PORT,
		                                            DTFProperties.DTF_CONNECT_PORT_DEFAULT);
		        } catch (PropertyException e) {
		            throw new CommException("Port number bad format.",e);
		        }
		
		        CommClient client  = new CommRMIClient(caddr, cport, _server);
		        _clients.put("dtfc", client);
		        _connected = true;
	        }

	        if ( Action.getLocalID() == null ) {
		         getCommClient("dtfc").register();
	        }

	        Action.getLogger().info("connected [" + Action.getLocalID() + "]");
        }
    }
    
    public CommServer getCommServer() { 
        return _server;
    }
    
    private static long lastHeartbeat = System.currentTimeMillis();
    public static void heartbeat() { 
        lastHeartbeat = System.currentTimeMillis();
    }
   
    private boolean keepAlive() {
        long heartbeat = (System.currentTimeMillis() - lastHeartbeat);
        if (heartbeat > HEARTBEAT_TIMEOUT) {
            return false;
        }
        return true;
    }
    
    private static long lastmessage = 0;
    
    public void run() {
        long lastUpdate = System.currentTimeMillis();
       
        while (_running) { 
            if (_logger.isDebugEnabled()) { 
                if (System.currentTimeMillis() - lastUpdate > 10000) { 
                    lastUpdate = System.currentTimeMillis();
                    Runtime rt = Runtime.getRuntime();
                    int MB = 1048567;
                    _logger.debug("JVM MEMORY MAX(MB): " + (rt.maxMemory()/MB) +
                                  " FREE(MB): " + (rt.freeMemory()/MB) +
                                  " TOTAL(MB): " + (rt.totalMemory()/MB));
                }
            }
            
            if (DTFNode.getType().equals("dtfc")) { 
                /*
                 * Heart-beating all registered components.
                 */
                NodeState ns = NodeState.getInstance();
                //ns.checkForOrphans();
               
                ArrayList nodes = ns.getRegisteredNodes();
                for (int i = 0; i < nodes.size(); i++) { 
                    NodeInfo node = (NodeInfo)nodes.get(i);
                   
                    long start = System.currentTimeMillis();
                    Boolean result = node.getClient().heartbeat();
                    long stop = System.currentTimeMillis();
                    
                    if (_logger.isDebugEnabled()) 
                        _logger.debug("Time to heartbeat " + node + " " + 
                                      (stop-start) + "ms.");

                    if ( !result.booleanValue() ) {
                        
                        /*
                         * Avoid disconnecting a node that is executing a very 
                         * CPU/IO intensive task that is leading to having 
                         * issues heart beating this component. Use this to 
                         * identify to the end user that he/she may be over 
                         * stressing the node and that increasing the number of
                         * agents used may be necessary to correctly execute the
                         * currently running test.
                         */
                        if ( node.getClient().isSendingAction() ) { 
                        	if ( System.currentTimeMillis() - lastmessage > 5000 ) {
	                            lastmessage = System.currentTimeMillis();
	                            
	                            _logger.warn("Node " + node + 
	                                         " is executing an action but not " + 
	                                         "heartbeating well. That agent may" + 
	                                         " be overwhelmed with work and you " + 
	                                         " may need to review your currently " + 
	                                         "running test case.");
                        	}
                            continue;
                        }
                        
                        /*
                         * heart beat missed the component must have died lets 
                         * remove the node from the registeredNodes, which in turn 
                         * will unlock any components locked by this agent.
                         */
                        try {
                            // make sure that the component didn't just go away
                            if (ns.getNodeInfo(node.getId()) != null) { 
                                _logger.info("Heartbeat missed for node: " + 
                                             node + ", releasing locked components.");
                                ns.removeNode(node);
                            } else { 
                                _logger.info("Just avoided confusion.");
                            }
                        } catch (DTFException e) {
                            _logger.error("Unable to unregister component " + 
                                          node,e);
                        }
                    }
                }
            } else { 
                /*
                 * Keep Alive check for all nodes except the DTFC, this is used by
                 * DTF nodes to know when the DTFC has gone away. The way it works 
                 * is that the DTFC will heart beat each node every 5s but if he 
                 * fails to heart beat 2 in a row then the component will just 
                 * shutdown.
                 */
                if (DTFNode.getType().equals(DTFConstants.DTFX_ID) && 
                    !Action.getComponents().hasComponents()) {
                    // if you're a dtfx and you don't have registered components
                    // then you have no reason to care about heartbeats
                } else { 
                    if ( !keepAlive() ) {
                        if ( Node.isExecuting() || Comm.isBusy() ) { 
                        	if ( System.currentTimeMillis() - lastmessage > 5000 ) {
	                            lastmessage = System.currentTimeMillis();
	                            _logger.warn("Avoiding disconnecting because " + 
	                            		     "it is currently running an " + 
	                            		     "action. This is indicative of a " + 
	                            		     "node being overwhelmed with work");
                        	}
                            continue;
                        }
                        
                        _logger.info("DTFC failed to heartbeat for " +
                                     HEARTBEAT_TIMEOUT/1000 + 
                                     "s, shutting down node.");
                        _running = false; 
                        return;
                    }
                }
            }
            ThreadUtil.pause(HEARTBEAT_INTERVAL);
        }
    }
  
    private static AtomicLong execution = new AtomicLong(0);
    public static boolean isBusy() { 
        return execution.intValue() != 0;
    }

    public boolean isUp() {
        return _running;
    }

    public void shutdown() {
        if (_server != null)
            _server.shutdown();
        
        Iterator iterator = _clients.keySet().iterator();
        
        while (iterator.hasNext()) { 
            String id = (String)iterator.next();
            CommClient c = (CommClient)_clients.get(id);
            c.shutdown();
        }
       
        _running = false;
    }

    private static HashMap<String, CommClient> _clients = 
                                              new HashMap<String, CommClient>();
    public static void addClient(String id, CommClient client) { 
        _clients.put(id, client);
    }
    
    public static CommClient removeClient(String id) { 
        return _clients.remove(id);
    }

    /*
     * XXX: temporary solution, will need to revisit this soon but for now this
     *      is the easiest way to call back to the runner on the same thread
     *      that originated the current action execution. This is mainly used by
     *      tags that need to return a result back to the same calling thread.
     */
    public Action sendActionToCaller(String id, Action action) throws DTFException {
        try { 
            execution.incrementAndGet();
	        Sequence sequence = new Sequence();
	       
	        String tid = (String)Action.getContext(Node.ACTION_DTFX_THREADID);
	        if ( tid == null ) { tid = Thread.currentThread().getName(); }
	        
	        sequence.setThreadID(tid);
	        sequence.addAction(action);
	        return sendAction(id, sequence);
        } finally { 
            execution.decrementAndGet();
        }
    }
    
    public Action sendAction(String id, Action action) throws DTFException {
        try { 
            execution.incrementAndGet();
	        CommClient client = getCommClient(id);
	        return  client.sendAction(id, action);
        } finally { 
            execution.decrementAndGet();
        }
    }

    public void executeOnComponent(String id, Action action) throws DTFException {
        try { 
            execution.incrementAndGet();
            executeOnComponent(id, action, false);
        } finally { 
            execution.decrementAndGet();
        }
    }
    
    public void executeOnComponent(String id,
                                   Action action,
                                   boolean nohooks) throws DTFException {
        try { 
            execution.incrementAndGet();
	        Component component = new Component();
	        component.setId(id);
	        component.addAction(action);
	        // this isn't suppose to redo the hooks again...
	        component.execute(nohooks);        
        } finally { 
            execution.decrementAndGet();
        }
    }
    
    public CommClient getCommClient(String id) throws DTFException { 
        CommClient client = (CommClient)_clients.get(id); 
       
        if (client == null) { 
            checkAndConnectToDTFC();
            return (CommClient)_clients.get("dtfc");
        }
        
        return client;
    } 
}
