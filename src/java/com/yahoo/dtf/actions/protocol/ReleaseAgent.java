package com.yahoo.dtf.actions.protocol;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.http.HttpBase;
import com.yahoo.dtf.actions.protocol.Lock;
import com.yahoo.dtf.comm.Comm;
import com.yahoo.dtf.comm.rpc.Node;
import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.state.ActionState;
import com.yahoo.dtf.state.DTFState;

/**
 * does the opposite of SetupAgent and releases this agent from being owned
 * by anyone, allowing it release any stale data or references. 
 */
public class ReleaseAgent extends Action {
    
    public ReleaseAgent() { }
    
    public ReleaseAgent(Lock lock) { 
        
    }
    
    public void execute() throws DTFException {
        /*
         * Call all CleanUp hooks.
         */
        for (int i = 0; i < _cleanupHooks.size(); i++) { 
            _cleanupHooks.get(i).cleanup();
        }
        
        /*
         * Catch uncleaned global context
         */
        ActionState as = ActionState.getInstance();
        DTFState mainState = as.getState(Node.BASE_CONFIG);
        Hashtable globalCtx = mainState.getGlobalContext();
        Enumeration keys = globalCtx.keys();
        
        StringBuffer contexts = new StringBuffer();
        while (keys.hasMoreElements()) { 
            String key = (String)keys.nextElement();
            
            // only acceptable global ctx left behind.
            if (!key.equals(Node.ACTION_DTFX_THREADID))
                contexts.append(key + ",");
        }
        
        if ( contexts.length() != 0 )
            getLogger().warn("Unfreed global contexts [" + contexts + "]");
        globalCtx.clear();
        
        // now lets make sure there's only 2 state objects left, 1 from the main 
        // thread and the other from this thread that is executing right now.
     
        Thread[] threads = new Thread[Thread.activeCount()];
        int numThreads = Thread.enumerate(threads);
       
        HashMap<String,Thread> threadsLookup = new HashMap<String, Thread>();
        for (int i = 0; i < numThreads; i++) { 
            threadsLookup.put(threads[i].getName(), threads[i]);
        }
        
        Enumeration states = as.getStates();
        contexts = new StringBuffer();
        while (states.hasMoreElements()) { 
            String key = (String) states.nextElement(); 
            /*
             * Do not delete the state of this current thread, main or the agent
             * base config.
             */
            if (!(key.equals("main") || 
                  key.equals(Thread.currentThread().getName()) ||
                  key.equals(Node.BASE_CONFIG))) {
                contexts.append(key + ",");
	            as.delState(key);
            }
        }
        if ( contexts.length() != 0 )
            getLogger().warn("Unfreed local contexts [" + contexts + "]");
        
        /*
         * We release the connections because the older connections are not
         * being release because in the constructor of HttpOperation we 
         * tell the API to not check for stale connections (this helps 
         * performance but would then generate 1 single failure to connect
         * because of an old stale connection);
         */
        HttpBase.releaseConnections();
        
        /*
         * Throw away all of the currently exported references on the main state
         */
        mainState.getReferences().clear();
        
        /*
         * Throw away all of the currently exported functions on the main state
         */
        mainState.getFunctions().clear();
       
        // clean up rmi clients
        Comm.removeClient(DTFNode.getOwner().getId());
        
        DTFNode.setOwner(null);
        getLogger().info("This agent is no longer in use by anyone.");
    }
    
    private static ArrayList<CleanUpHook> _cleanupHooks = new ArrayList<CleanUpHook>();
    public static void addCleanUpHook(CleanUpHook cleanup) { 
        _cleanupHooks.add(cleanup);
    }
}
