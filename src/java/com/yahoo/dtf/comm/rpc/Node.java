package com.yahoo.dtf.comm.rpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import com.yahoo.dtf.comm.rpc.ActionResult;
import com.yahoo.dtf.comm.rpc.NodeInterface;
import com.yahoo.dtf.DTFConstants;
import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.NodeInfo;
import com.yahoo.dtf.NodeState;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.component.Component;
import com.yahoo.dtf.actions.flowcontrol.Sequence;
import com.yahoo.dtf.actions.protocol.CleanUpHook;
import com.yahoo.dtf.actions.protocol.Connect;
import com.yahoo.dtf.actions.protocol.Lock;
import com.yahoo.dtf.actions.protocol.Rename;
import com.yahoo.dtf.comm.Comm;
import com.yahoo.dtf.components.CleanUpState;
import com.yahoo.dtf.components.ComponentReturnHook;
import com.yahoo.dtf.config.Config;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.logger.DTFLogger;
import com.yahoo.dtf.recorder.internal.RemoteRecorder;
import com.yahoo.dtf.state.ActionState;
import com.yahoo.dtf.state.DTFState;

public class Node implements NodeInterface,CleanUpHook {
    
    public static final String BASE_CONFIG = "BaseConfig";
    
    public static final String ACTION_RESULT_CONTEXT  = "dtf.action.result.ctx"; 
    public static final String ACTION_DTFX_THREADID   = "dtf.dtfx.threadid.ctx"; 

    private static ArrayList<String> ctx = new ArrayList<String>();
    
    public Boolean heartbeat() {
        Comm.heartbeat();
        return Boolean.TRUE;
    }
    
    private static AtomicLong executions = new AtomicLong(0);
   
    public ActionResult execute(String id, Action action) throws RemoteException {
        ActionResult result = new ActionResult();
        ActionState as = ActionState.getInstance();
     
        executions.incrementAndGet();
        try { 
            if (DTFNode.getType().equals("dtfc")) { 
                if (id.equals("dtfc")) {
                    action.execute();
                    if (action instanceof Lock)
                        result.addAction(action);
                } else {
                    NodeInfo node = NodeState.getInstance().getNodeInfo(id);
                    return node.getClient().sendAction(id, action);
                }
            } else { 
                String threadId = null;
               
                if (action instanceof Sequence) 
                    threadId = ((Sequence)action).getThreadid();
               
                if (DTFNode.getType().equals(DTFConstants.DTFX_ID) && 
                    threadId != null) { 
                    /*
                     * XXX: I don't like this solution because it is in no way 
                     *      elegant or easy to understand.
                     *      
                     * Remote events being executed on the dtfx under a specified 
                     * threadid
                     * 
                     * We need to dup the state and also make sure that the Global 
                     * Context isn't shared by other threads calling back, but make sure
                     * to have the same properties.
                     * 
                     */
                    DTFState state = as.getState(threadId).duplicate();
                    as.setState(state);
                    // by default clone also clones the Config but in this case we 
                    // want callbacks to affect the currently running Config for that
                    // specific threadid
                    state.setConfig(as.getState(threadId).getConfig());
                    state.registerContext(ACTION_RESULT_CONTEXT, result);
                    try {
                        action.execute();
                    } finally { 
                        state.unRegisterContext(ACTION_RESULT_CONTEXT);
                        as.delState();
                    }
                } else { 
                    /*
                     * Use the base config to create the state for all of the 
                     * remote calls to this component. The base config is 
                     * recreated when this component is locked and is a way of 
                     * isolating executions between different test runs.
                     */
                    DTFState state = null;

                    /*
                     * This avoids conflicting state id name with an existing 
                     * thread on this side.
                     */
                    String rkey = CleanUpState.genThreadName(threadId);
                    
                    if ( threadId != null && as.hasState(rkey) )
                        state = as.getState(rkey);
                   
                    // first call to this agent by this remote thread
                    if ( state == null ) { 
                        DTFState main = as.getState(BASE_CONFIG);
                        Config config = (Config) main.getConfig().clone();
                        
                        state = new DTFState(config, main.getStorage());
	                    state.setGlobalContext(main.getGlobalContext());
	                    state.setComm(main.getComm());
	                    state.setFunctions(main.getFunctions());
	                    state.setReferences(main.getReferences());
	                    state.setRecorder(main.getRecorder());
	                   
	                    if ( threadId != null ) {
	                        as.setState(rkey, state);
	                        
	                        synchronized(ctx) { 
	                            ctx.add(rkey);
	                        }
	                    }
                    }
                    as.setState(state);
                   
                    RemoteRecorder rrecorder = null;
                    // create the remote recorder only after setting the state 
                    // so that this recorder can now save state related changes
                    // correctly
                    if ( threadId != null ) {
                        rrecorder = new RemoteRecorder(result, true, threadId);
                    	state.registerContext(ACTION_DTFX_THREADID, threadId);
                    }
                    
                    state.registerContext(ACTION_RESULT_CONTEXT, result);
                    try {
                        if ( rrecorder != null )
                            Action.pushRecorder(rrecorder, null);
                       
                        action.execute();

                        String owner = null;
                        Lock l = DTFNode.getOwner();
                        if (l != null) owner = l.getOwner();
             
                        boolean nohooks = Boolean.valueOf("" + 
                                             state.getContext("noreturnhooks"));
                        if ( owner != null && !nohooks ) { 
	                        // call ComponentReturnHooks
	                        ArrayList<ComponentReturnHook> rhooks = 
	                                            Component.getComponentReturnHooks();
	                        
	                        for (int i = 0; i < rhooks.size(); i++) { 
	                            ComponentReturnHook crh = rhooks.get(i);
	                            result.addActions(crh.returnToRunner(owner));
	                        }
                        }
                    } finally { 
                        state.unRegisterContext(ACTION_DTFX_THREADID);
                        state.unRegisterContext(ACTION_RESULT_CONTEXT);
                        state.unRegisterContext("noreturnhooks");
                        
                        if ( threadId != null ) {
                            Action.popRecorder();
	                        if ( as.hasState(threadId) )
	                            as.delState();
                        }
                    }
                }
            }
          
            return result;
        } catch (Throwable t) {
            /*
             * This code exists because we need to make sure to return a message
             * that can easily be used by the person developing or using a tag
             * to identify what the problem was. Since sometimes a new tag can 
             * throw Exceptions that are not fully serializable we need to 
             * handle this gracefully and be able to at least report that the 
             * exception message logged on the agent for review.
             * 
             * When this situation is hit this is in fact an issue with the tag
             * that was created throwing bad exceptions. So if you hit this 
             * point in the code you really shouldn't be throwing exceptions
             * that are not serializable, because that breaks quite a few things
             * in the way remote actions are handled.
             * 
             */
            if ( isSerializable(t) ) { 
                throw new RemoteException("Error executing action.",t);
            } else { 
                DTFLogger log = Action.getLogger();
	            log.error("Non serializable exception caught, you should fix" + 
	                      " this by throwing only exceptions that are " + 
	                      " serializable so DTF can report remote exceptions " +
	                      " correctly on the runner side.",t);
	            DTFException e = 
			               new DTFException("Non serializable exception on agent side [" + 
			                                t.getMessage() + 
			                                "], check agent for more details.");
	            throw new RemoteException("Error executing action.",e);
            }
        } finally { 
            executions.decrementAndGet();
        }
    }
    
    public static boolean isExecuting() { return executions.intValue() != 0; }
    
    public void cleanup() throws DTFException {
        ActionState as = ActionState.getInstance();

        for (int i = 0; i < ctx.size(); i++) { 
            as.delState(ctx.remove(i));
        }
       
        if ( Action.getLogger().isDebugEnabled() )
            Action.getLogger().debug("Cleaned up " + ctx.size() + " states.");
    }
    
    public static void cleanedup(String id) { 
        synchronized (ctx) { 
            ctx.remove(id);
        }
    }
    
    public boolean isSerializable(Object obj) { 
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    public ActionResult register(Connect connect) throws RemoteException {
        try { 
            ActionResult result = new ActionResult();
            connect.execute();
            Rename rename = new Rename();
            rename.setName(connect.getId());
            result.addAction(rename);
            return result;
        } catch (DTFException e) { 
            throw new RemoteException("Error registering.",e);
        }
    }
    
    public ActionResult unregister(Connect connect) throws RemoteException {
        try { 
            ActionResult result = new ActionResult();
            NodeState ns = NodeState.getInstance();
            ns.removeNode(connect);
            return result;
        } catch (DTFException e) { 
            throw new RemoteException("Error unregistering.",e);
        }
    }
}
