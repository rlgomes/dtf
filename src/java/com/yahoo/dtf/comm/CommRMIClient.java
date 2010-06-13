package com.yahoo.dtf.comm;

import java.io.InvalidClassException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;

import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.component.Attrib;
import com.yahoo.dtf.actions.protocol.Connect;
import com.yahoo.dtf.comm.rpc.ActionResult;
import com.yahoo.dtf.comm.rpc.NodeInterface;
import com.yahoo.dtf.exception.CommException;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.logger.DTFLogger;
import com.yahoo.dtf.state.DTFState;
import com.yahoo.dtf.util.ThreadUtil;

public class CommRMIClient extends CommClient {

    private static DTFLogger _logger = DTFLogger.getLogger(CommRMIClient.class);
    
    private CommServer _server = null;
    private NodeInterface _node = null;
   
    public CommRMIClient(String caddr, int cport) throws DTFException {
        this(caddr, cport, null);
    }

    public CommRMIClient(String caddr,
                         int cport,
                         CommServer server)
            throws DTFException {
        super(caddr,cport);
        
        System.setSecurityManager(new RMISecurityManager());
        _server = server;
        try {
            if (_logger.isDebugEnabled())
                _logger.debug("Connecting to " + getAddr() + ":" + getPort());
            _node = (NodeInterface) Naming.lookup("rmi://" + getAddr() + ":" +
                                                  getPort() + "/node");
        } catch (RemoteException e) {
            throw new CommException("Unable to connect to DTFC.",e);
        } catch (NotBoundException e) {
            throw new CommException("Unable to connect to DTFC.",e);
        } catch (MalformedURLException e) {
            throw new CommException("Unable to connect to DTFC.",e);
        }
    }
    
    public Boolean heartbeat() {
        try {
            /*
             * XXX:
             * 
             * Easy thing to do here is that if we're in the middle of a 
             * sendAction then we can assume that guy is still alive and not
             * have to issue the heartbeat itself. 
             */
            return _node.heartbeat();
        } catch (RemoteException e) {
            return false;
        }
    }

    public void printStats() {

    }

    public void register() throws DTFException {
        String id = Action.getLocalID();

        Connect connect;
        connect = new Connect(id);
        
        /*
         * Put default Attribs into connect message.
         */
        Iterator objs = _attribs.values().iterator();
        while (objs.hasNext()) {
            Attrib attrib = (Attrib) objs.next();
            connect.addAttrib(attrib);
        }

        connect.setAddress(_server.getAddress());
        connect.setPort(_server.getPort());
        connect.setBuildid(DTFNode.getBuildID());
        
        _logger.info("Registering " + connect);
        try {
            _node.register(connect).execute();
            _logger.info("Connected to DTFC");
        } catch (RemoteException e) {
            convertException(e, "Unable to register.");
        }
    }
   
    /**
     * This little method tries to do its best to only throw exceptions that
     * are important to the person running the test. This avoids all of the
     * exception layering that happens when using Communication APIs such as
     * RMI between the various components.
     * 
     * @param e
     * @param message
     * @throws DTFException
     */
    private void convertException(RemoteException e,
                                  String message) throws DTFException { 
        Throwable aux = e.getCause();
        while (aux != null) { 
            if (aux instanceof DTFException) {
                throw (DTFException)aux;
            }
            
            if (aux instanceof InvalidClassException)  {
                throw new RuntimeException("You are running two different " + 
                                           "builds of DTF and there are " + 
                                           "incompatible class versions.",
                                           e.getCause());
            }
            aux = aux.getCause();
        }
        throw new CommException(message,e);
    }

    private AtomicLong actioncounter = new AtomicLong(0);
    
    public ActionResult sendAction(String id, Action action)
            throws DTFException {
        try {
            actioncounter.incrementAndGet();
            return _node.execute(id, action);
        } catch (RemoteException e) {
            convertException(e,"Error sending action.");
        } finally { 
            actioncounter.decrementAndGet();
        }
        throw new RuntimeException("This line should never be executed.");
    }
   
    @Override
    public boolean isSendingAction() {
        return actioncounter.intValue() != 0;
    }

    public void shutdown() {

    }

    public void unregister(DTFState state) throws DTFException {
        String id = Action.getLocalID();
        
        if (id == null)
            return;
        
        Connect connect;
        connect = new Connect(id);
        /*
         * Put default Attribs into connect message.
         */
        Iterator objs = _attribs.values().iterator();
        while (objs.hasNext()) {
            Attrib attrib = (Attrib) objs.next();
            connect.addAttrib(attrib);
        }
        

        try {
            _node.unregister(connect).execute();
        } catch (RemoteException e) {
            convertException(e,"Unable to register node.");
        }
    }
}
