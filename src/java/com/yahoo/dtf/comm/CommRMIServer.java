package com.yahoo.dtf.comm;

import java.rmi.AccessException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMISocketFactory;
import java.rmi.server.UnicastRemoteObject;

import com.yahoo.dtf.comm.rpc.NodeInterface;
import com.yahoo.dtf.exception.CommException;
import com.yahoo.dtf.logger.DTFLogger;

public class CommRMIServer implements CommServer {
   
    private static DTFLogger _logger = DTFLogger.getLogger(CommRMIServer.class);
    
    private Registry _registry = null;
    
    private String _addr = null;
    private int _port = -1;

    private Remote remote = null;
    private RMISocketFactory _rmisf = null;
   
    public CommRMIServer(String addr, int port) throws CommException {
        _addr = addr;
        _port = port;
        System.setSecurityManager(new RMISecurityManager());
        _rmisf = new RMITunnelSF();
    }

    public void addHandler(String name, Object obj) throws CommException {
        if (obj instanceof Remote) {
            remote = (Remote) obj;
            try {
                NodeInterface stub = (NodeInterface) 
					                UnicastRemoteObject.exportObject(remote,
					                                                 _port,
					                                                 _rmisf,
					                                                 _rmisf);

                if (_logger.isDebugEnabled())
                    UnicastRemoteObject.setLog(System.out);

                _registry.rebind(name, stub);
            } catch (RemoteException e) {
                throw new CommException("Error registering handler.", e);
            }
            _logger.debug("Registered [" + name + "]");
        }
    }

    public String getAddress() {
        return _addr;
    }

    public int getPort() {
        return _port;
    }

    public void printStats() {
        // XXX: not implemented yet.
    }

    public void shutdown() {
        try {
            _registry.unbind("node");
            UnicastRemoteObject.unexportObject(remote, true);
            UnicastRemoteObject.unexportObject(_registry, true);
        } catch (NoSuchObjectException e) {
            throw new RuntimeException("Unable to shutdown RMIServer correctly",e);
        } catch (AccessException e) {
            throw new RuntimeException("Unable to shutdown RMIServer correctly",e);
        } catch (RemoteException e) {
            throw new RuntimeException("Unable to shutdown RMIServer correctly",e);
        } catch (NotBoundException e) {
            throw new RuntimeException("Unable to shutdown RMIServer correctly",e);
        }
    }

    public void start() throws CommException {
        // this assures that the server starts up on the same hostname that was
        // defined as being the dtf.listen.addr for this component
        System.setProperty("java.rmi.server.hostname",_addr);
        
        if (_port != -1) {
            try { 
                // Bind the remote object's stub in the registry
                _registry = LocateRegistry.createRegistry(_port,
                                                          _rmisf,
                                                          _rmisf);
            } catch (RemoteException e) { 
                throw new CommException("Unable to start up RMIServer.",e);
            }
        } else { 
            boolean bound = false;
            int retries = 0;
            _port = 30000; 
            while (!bound) { 
                try { 
                    _registry = LocateRegistry.createRegistry(_port,
                                                              _rmisf,
                                                              _rmisf);
                    bound = true;
                } catch (RemoteException e) { 
                    retries++;
                    _port++;
                }
            }
        }
        _logger.info("Listening at [" + _port + "]");
    }
}
