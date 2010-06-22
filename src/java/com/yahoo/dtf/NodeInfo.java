package com.yahoo.dtf;

import java.util.ArrayList;

import com.yahoo.dtf.NodeInfo;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.component.Attrib;
import com.yahoo.dtf.actions.flowcontrol.Sequence;
import com.yahoo.dtf.actions.protocol.Connect;
import com.yahoo.dtf.actions.protocol.Lock;
import com.yahoo.dtf.actions.protocol.ReleaseAgent;
import com.yahoo.dtf.comm.CommClient;
import com.yahoo.dtf.comm.rpc.Node;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.util.StringUtil;


public class NodeInfo extends Connect {
    
    private boolean locked = false;
    private CommClient _client = null;
  
    // Safety mutex for generating the unique names for components.
    private static Object genObject = new Object();
    private static long componentCount = 0;
    
    private String _owner = null;
    
    public NodeInfo() { }
   
    public NodeInfo(Connect conn, CommClient client) throws DTFException {
        _client = client;
        
        setId(conn.getId());
        addAttribs(conn.findActions(Attrib.class));
        
        // generate unique name for this component
        if (getId() == null) { 
            synchronized(genObject) {
                String type = findAttrib(DTFProperties.DTF_NODE_TYPE);
                setId((type == null ? "dtfa" : type) + "-" + componentCount++);
            }
        }
       
        setAddress(conn.getAddress());
        setPort(conn.getPort());
    }

    public NodeInfo(Lock lock) throws DTFException {
        setId(lock.getId());
        addAttribs(lock.findActions(Attrib.class));
    }
    
    public boolean matches(NodeInfo ni) throws ParseException {
        ArrayList attribs = ni.findActions(Attrib.class);
        ArrayList myAttribs = findActions(Attrib.class);
     
        for(int i = 0; i < myAttribs.size(); i++) { 
            Attrib attrib = (Attrib)myAttribs.get(i);
            boolean found = false;
            
            for(int j = 0; j < attribs.size(); j++) { 
                if (attrib.matches((Attrib)attribs.get(j))) {
                    found = true;
                    break;
                }
            }
           
            if (!found) return false;
        }
        
        if (StringUtil.equalsIgnoreCase(getId(), ni.getId())) 
            return true;
           
        return false;
    }
  
    public void lock(String owner) { 
        assert locked == false; 
        locked = true;
        _owner = owner;
    }
    
    public void unlockWithoutRelease() { 
        assert locked == true;
        locked = false;
    }
    
    public void unlock() throws DTFException { 
        assert locked == true;
        
        ReleaseAgent release = new ReleaseAgent();
        Sequence sequence = new Sequence();
        
        if ( getContext(Node.ACTION_DTFX_THREADID) == null ) { 
            sequence.setThreadID("main");
        } else { 
            sequence.setThreadID((String)getContext(Node.ACTION_DTFX_THREADID));
        }
        sequence.addAction(release);
        
        getClient().sendAction(getId(), sequence);
        
        locked = false;
    }
    
    public boolean isLocked() { return locked; }
    
    public CommClient getClient() { return _client; } 
    
    public String getOwner() { return _owner; }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
