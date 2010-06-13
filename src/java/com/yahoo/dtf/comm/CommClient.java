package com.yahoo.dtf.comm;

import java.util.HashMap;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.component.Attrib;
import com.yahoo.dtf.comm.rpc.ActionResult;
import com.yahoo.dtf.exception.CommException;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.logger.DTFLogger;
import com.yahoo.dtf.state.DTFState;

public abstract class CommClient {
   
    private static DTFLogger _logger = DTFLogger.getLogger(CommClient.class);
    
    static final String  TUNNEL_CONF_FILE = "tunnel.conf";
    
    private String _caddr = null;
    private int _cport = -1;
   
    /**
     * Abstract constructor will allow for all of the underlying communication 
     * clients to share the same tunneling code.
     * 
     * @param caddr
     * @param cport
     * @throws CommException
     */
    public CommClient(String caddr, int cport) throws DTFException {
        _caddr = caddr;
        _cport = cport;
        
        if ( Comm.getTunnels().containsKey(caddr + cport) ) {
            _caddr = "127.0.0.1";
            _cport = Comm.getTunnels().get(caddr + cport);
        }
    }

    protected String getAddr() { return _caddr; } 
    protected int getPort() { return _cport; } 
    
    public abstract ActionResult sendAction(String id, Action action) throws DTFException;
    
    public abstract Boolean heartbeat();
    public abstract void shutdown();
    public abstract void register() throws DTFException ;
    public abstract void unregister(DTFState state) throws DTFException;

    public abstract boolean isSendingAction();

    protected final static HashMap<String, Attrib> _attribs = 
                                                  new HashMap<String, Attrib>();
    
    public static void addAgentAttribute(String name, String value) {
        addAgentAttribute(name, value, false);
    }

    public static void addAgentAttribute(String name, int value) {
        addAgentAttribute(name, ""+value, false);
    }

    public static void addAgentAttribute(String name,
                                  String value,
                                  boolean isTestProperty) {
        if (_attribs.containsKey(name)) 
            _logger.warn("Overwriting [" + name + "] client attribute.");
       
        _attribs.put(name, new Attrib(name, value, isTestProperty));
    }

    public abstract void printStats();
}
