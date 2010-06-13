package com.yahoo.dtf.logger;

import com.yahoo.dtf.logger.RemoteLogger;
import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.flowcontrol.Sequence;
import com.yahoo.dtf.actions.logging.Log;
import com.yahoo.dtf.comm.rpc.Node;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.state.ActionState;

public class RemoteLogger {
    
    private static RemoteLogger _instance = null;
    
    private RemoteLogger() { }
    
    public static synchronized RemoteLogger getInstance() { 
        if (_instance == null)
            _instance = new RemoteLogger();
        
        return _instance;
    }

    public void info(String message) throws DTFException { 
        String tag = new Throwable().getStackTrace()[1].getClassName(); 
        info(tag, message);
    }
        
    public void info(String tag, String message) throws DTFException { 
        Log log = new Log();
        log.setCDATA(message);
        log.setTag(tag);
        log.setLevel(Log.INFO);
        sendLog(log);
    }

    public void error(String message) throws DTFException { 
        String tag = new Throwable().getStackTrace()[1].getClassName(); 
        error(tag, message);
    }
    
    public void error(String tag, String message) throws DTFException { 
        Log log = new Log();
        log.setCDATA(message);
        log.setTag(tag);
        log.setLevel(Log.ERROR);
        sendLog(log);
    }
    
    public void warn(String message) throws DTFException { 
        String tag = new Throwable().getStackTrace()[1].getClassName(); 
        warn(tag, message);
    }
    
    public void warn(String tag, String message) throws DTFException { 
        Log log = new Log();
        log.setCDATA(message);
        log.setTag(tag);
        log.setLevel(Log.WARN);
        sendLog(log);
    }
    
    private void sendLog(Log log) throws DTFException { 
        ActionState as = ActionState.getInstance();
        String threadId = 
              (String)as.getState().getContext(Node.ACTION_DTFX_THREADID);
        
        if (DTFNode.getOwner() != null && threadId != null) { 
            String ownerId = DTFNode.getOwner().getOwner();
            Action.getComm().sendActionToCaller(ownerId, log);
        }
    }
}