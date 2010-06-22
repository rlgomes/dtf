package com.yahoo.dtf;

import java.util.ArrayList;
import java.util.Hashtable;

import com.yahoo.dtf.NodeInfo;
import com.yahoo.dtf.NodeState;
import com.yahoo.dtf.actions.protocol.Connect;
import com.yahoo.dtf.actions.protocol.Lock;
import com.yahoo.dtf.comm.CommClient;
import com.yahoo.dtf.exception.ActionException;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.LockException;
import com.yahoo.dtf.logger.DTFLogger;


public class NodeState {
    private static DTFLogger _logger = DTFLogger.getLogger(NodeState.class);
    private static NodeState instance = null;

    public synchronized static NodeState getInstance() {
       if (instance == null) {
           instance = new NodeState();
       }
       return instance;
    }
   
    private ArrayList<NodeInfo> connectedNodes = null;
    private Hashtable<String,ArrayList<NodeInfo>> lockedNodes = null;
    
    private NodeState() { 
        connectedNodes = new ArrayList<NodeInfo>();
        lockedNodes = new Hashtable<String, ArrayList<NodeInfo>>();
    }
    
    public ArrayList<NodeInfo> getRegisteredNodes() { 
        return connectedNodes;
    }
    
    public synchronized NodeInfo addNode(Connect connect, CommClient client) throws DTFException{
        NodeInfo ni = new NodeInfo(connect, client);
       
        _logger.info("Registering " + ni);
        
        if (ni.findAttrib(DTFProperties.DTF_NODE_TYPE).equalsIgnoreCase("dtfx")) { 
            // dtfx are not to be locked or unlocked
            ni.lock(null);
        }
        
        if (connectedNodes.contains(ni)) 
            throw new DTFException("Node already registered with id: " + 
                                   ni.getId());
        
        connectedNodes.add(ni);
        return ni;
    }
    
    public synchronized boolean isNodeRegistered(String id) { 
        for (int i = 0; i < connectedNodes.size(); i++) {
            NodeInfo ni = (NodeInfo)connectedNodes.get(i); 
            if (ni.getId().equals(id))
                return true;
        }
        return false;
    }
    
    public synchronized NodeInfo getNodeInfo(String id) throws DTFException { 
        for (int i = 0; i < connectedNodes.size(); i++) {
            NodeInfo ni = (NodeInfo)connectedNodes.get(i); 
            if (ni.getId().equals(id))
                return ni;
        }
        throw new DTFException("Unable to find node: " + id);
    }
    
    public synchronized void removeNode(Connect conn) throws DTFException{
        NodeInfo ni = new NodeInfo(conn,null);
       
        _logger.info("Unregistering " + conn);
        
        if (!connectedNodes.contains(ni)) 
            throw new DTFException("Node not registered with id: " + 
                                   conn.getId());
       
        connectedNodes.remove(ni);
        
        // unlock all components held by this agent
        ArrayList<NodeInfo> locks = lockedNodes.get(conn.getId());

        if (locks != null && locks.size() != 0) {
            _logger.info("Unlocking all compoennts for " + conn.getId());
            String ids = "";
            for(int i = 0; i < locks.size(); i++) {
                NodeInfo locked = locks.get(i);
                ids += locked.getId() + ", ";
                try { 
                    locked.unlock();
                } catch (DTFException e) { 
                    _logger.error("Unable to unlock component [" + locked + 
                                  "] will remove this agent.",e);
                    removeNode(locked);
                    throw e;
                }
            }
            _logger.info("Unlocked components {" + ids + "}");
        } else {
            _logger.info("No locked components.");
        }
        
        lockedNodes.remove(conn.getId());
    }
    
    public synchronized void checkForOrphans() {
        for (int i = 0; i < connectedNodes.size(); i++) {
            NodeInfo ni = connectedNodes.get(i);
            if (ni.isLocked() && ni.getOwner() != null) {
                if (!isNodeRegistered(ni.getOwner())) {
                    _logger.warn("Found orpahend agent [" + ni.getId()
                                 + "], releasing now.");
                    try {
                        ni.unlock();
                    } catch (DTFException e) {
                        _logger.error("Failure to unlock " + ni, e);
                    }
                }
            }
        }
    }
    
    private void addNode(String id, NodeInfo node) {
        ArrayList<NodeInfo> locks = lockedNodes.get(id);
        if (locks == null)  {
            locks = new ArrayList<NodeInfo>();
            lockedNodes.put(id, locks);
        }
        locks.add(node);
    }
  
    private void removeNode(String id, NodeInfo node) throws ActionException {
        ArrayList<NodeInfo> locks = lockedNodes.get(id);
       
        if (locks == null) 
            throw new ActionException("Agent never locked by this agent.");
        
        locks.remove(node);
    }
   
    /**
     * This method will attempt to all or none of the locks specified by the 
     * array of Locks passed as an argument. If we were unable to lock all of 
     * the 
     * 
     * @param locks
     * @return
     * @throws DTFException
     */
    public synchronized NodeInfo[] lockNodes(Lock[] locks) throws DTFException { 
        NodeInfo[] niLocks = new NodeInfo[locks.length];
        
        for (int i = 0; i < niLocks.length; i++) 
            niLocks[i] = new NodeInfo(locks[i]);
     
        int n = 0;
        boolean matched = false;
        for (n = 0; n < niLocks.length; n++) { 
            matched = false;
	        for(int i = 0; i < connectedNodes.size(); i++) {
	            NodeInfo ni = (NodeInfo) connectedNodes.get(i);
	               
	            if ( _logger.isDebugEnabled() )
	                _logger.debug("Trying to match " + ni + 
	                              " with " + niLocks[n]);
	                
	            if (!ni.isLocked() && niLocks[n].matches(ni)) { 
	                if (_logger.isDebugEnabled())
	                    _logger.debug("Locked " + ni + " for " + 
	                                  locks[n].getOwner());
	                ni.lock(locks[n].getOwner());
	                niLocks[n] = ni;
	                matched = true;
	            } else 
	                if (_logger.isDebugEnabled())
	                    _logger.debug("Didn't find match " + ni);
	        }
	        
	        if ( !matched )
	            break;
        }
        
        if ( matched ) { 
            for (n = 0; n < niLocks.length; n++) { 
		        addNode(locks[n].getOwner(),niLocks[n]);
		        // move this node to the end of the list so we'll give all nodes
		        // a chance when locking
		        connectedNodes.add(connectedNodes.remove(n));
            }
            return niLocks;
        }

        for (n = 0; n < niLocks.length; n++) { 
            if ( niLocks[n] != null ) {
                if ( niLocks[n].isLocked() )
                    niLocks[n].unlockWithoutRelease();
            }
        }
      
        StringBuffer buff = new StringBuffer("[");
        
        for (NodeInfo ni : niLocks) 
            if ( ni != null ) 
                buff.append(ni + ", ");
        
        buff.replace(buff.length()-1, buff.length(), "]");
        
        throw new LockException("No agent found to match " + buff);
    }
    
    public synchronized NodeInfo unlockNode(Lock lock) throws DTFException {
        NodeInfo niLock = new NodeInfo(lock);
        for(int i = 0; i < connectedNodes.size(); i++) {
            NodeInfo ni = (NodeInfo) connectedNodes.get(i);
            if (_logger.isDebugEnabled())
                _logger.debug("Trying to match " + ni + " with " + niLock);
            /*
             * We only care that the id matches the list of ids that this 
             * component owns.
             */
            if (ni.isLocked() && niLock.getId().equals(ni.getId())) { 
                removeNode(lock.getOwner(), ni);
                ni.unlock();
                if (_logger.isDebugEnabled())
                    _logger.debug("Unlocked " + ni + " for " + lock.getOwner());
                return ni;
            }
        }
        throw new ActionException("Agent: " + niLock + " never locked.");
    }
    
    public synchronized NodeInfo unlockNodeWithoutRelease(Lock lock)
           throws DTFException {
        NodeInfo niLock = new NodeInfo(lock);
        for(int i = 0; i < connectedNodes.size(); i++) {
            NodeInfo ni = (NodeInfo) connectedNodes.get(i);
            if (_logger.isDebugEnabled())
                _logger.debug("Trying to match " + ni + " with " + niLock);
            /*
             * We only care that the id matches the list of ids that this 
             * component owns.
             */
            if (ni.isLocked() && niLock.getId().equals(ni.getId())) { 
                removeNode(lock.getOwner(), ni);
                if (_logger.isDebugEnabled())
                    _logger.debug("Unlocked " + ni + " for " + lock.getOwner());
                return ni;
            }
        }
        throw new ActionException("Agent: " + niLock + " never locked.");
    }
}
