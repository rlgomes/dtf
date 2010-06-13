package com.yahoo.dtf.comm.rpc;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.yahoo.dtf.comm.rpc.ActionResult;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.protocol.Connect;
import com.yahoo.dtf.exception.DTFException;

public interface NodeInterface extends Remote {
   
    /**
     * 
     * @return
     */
    public Boolean heartbeat() throws RemoteException;
   
    /**
     * 
     * @param action
     * @return
     */
    public ActionResult execute(String id, Action action) throws RemoteException;

    /**
     * 
     * @param connect
     * @return
     * @throws DTFException
     */
    public ActionResult register(Connect connect) throws RemoteException;

    /**
     * 
     * @param connect
     * @return
     * @throws DTFException
     */
    public ActionResult unregister(Connect connect) throws RemoteException;
}
