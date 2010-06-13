package com.yahoo.dtf.actions.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.ActionFactory;
import com.yahoo.dtf.actions.basic.Script;
import com.yahoo.dtf.actions.component.Component;
import com.yahoo.dtf.actions.protocol.Lock;
import com.yahoo.dtf.comm.Comm;
import com.yahoo.dtf.comm.rpc.Node;
import com.yahoo.dtf.components.ComponentUnlockHook;
import com.yahoo.dtf.exception.BreakException;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.logger.DTFLogger;
import com.yahoo.dtf.state.ActionState;
import com.yahoo.dtf.state.DTFState;

public class ScriptUtil {

    private static DTFLogger _logger = DTFLogger.getLogger(ScriptUtil.class);
    /**
     * 
     * 
     * @param xmlFile
     * @param state
     * @throws DTFException
     */
    public static void executeScript(String filename,
                                     InputStream xmlFile, 
                                     DTFState state)
                  throws DTFException {
        ActionState as = ActionState.getInstance();
        DTFState current = as.getState();
        
        as.setState(state);
        as.setState(Node.BASE_CONFIG, as.getState("main").duplicate());
        
        try {
            // Parsing errors would be reported here.
            Action root = ActionFactory.parseAction(xmlFile,filename);
            assert (root instanceof Script);
           
            state.setRoot(root);
            state.setAction(root);
            root.execute();
        } catch (BreakException e) { 
            // break point
            if ( _logger.isDebugEnabled() )
                _logger.debug("break point hit",e);
        } catch (DTFException e) { 
            /*
             * ScripUtil is the only place the exceptions are logged from and 
             * by controlling if it was logged we can avoid logging the same 
             * messages more than once.
             */
            if (!e.wasLogged())  {
                Action.getLogger().error("Error running script.",e);
                e.logged();
            }
            throw e;
        } finally {
            // clean up unlocked components
            try { 
	            if ( Comm.isConnected() ) 
	                Action.getComm().getCommClient("dtfc").unregister(state);
            } finally { 
                HashMap<String, Lock> components = Action.getComponents()._elems;
                Iterator<Entry<String,Lock>> entries = components.entrySet().iterator();
                ArrayList<ComponentUnlockHook> hooks = 
	                                        Component.getComponentUnlockHooks();
               
                while (entries.hasNext() ) { 
                    Entry<String, Lock> entry = entries.next();
                    
	                for (int i = 0; i < hooks.size(); i++) 
	                    hooks.get(i).unlockComponent(entry.getKey());
                }
                // no matter what delete the current state and set the previous
                // one correctly!
	            as.delState();
	            as.setState(current);
            }
        }
    }
}
