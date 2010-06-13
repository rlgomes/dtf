package com.yahoo.dtf.actions.protocol;

import java.util.ArrayList;
import java.util.Hashtable;

import com.yahoo.dtf.actions.protocol.Connect;
import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.DTFProperties;
import com.yahoo.dtf.NodeInfo;
import com.yahoo.dtf.NodeState;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.component.Attrib;
import com.yahoo.dtf.comm.Comm;
import com.yahoo.dtf.comm.CommClient;
import com.yahoo.dtf.comm.CommRMIClient;
import com.yahoo.dtf.exception.CommException;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.util.CollectionsUtil;


/**
 * This action is used internally for connecting to the DTFC
 * 
 * @author Rodney Gomes
 */
public class Connect extends Action {
   
    private String id = null;
   
    /**
     * Sending some settings to the dtfc
     */
    private String address = null;
    private int port = -1;
    
    private String buildid = null;
    
    public Connect() {}
    
    public Connect(String id) throws DTFException {
        this.id = id;
        setAddress(getConfig().getProperty(DTFProperties.DTF_LISTEN_ADDR));
        setPort(getConfig().getPropertyAsInt(DTFProperties.DTF_LISTEN_PORT));
        
        addAttrib(new Attrib("dtf.tunneled",
        	      getConfig().getProperty(DTFProperties.DTF_TUNNELED,"false"), 
        	      true));
    }
    
    public void copy(NodeInfo info) throws DTFException {
        setId(info.getId());
        setAddress(info.getAddress());
        setPort(info.getPort());
        addAttribs(info.findActions(Attrib.class));
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public void execute() throws DTFException {
        if ( !DTFNode.getBuildID().equals(getBuildid()) ) {
            throw new DTFException("Build Id mismatch [" + getBuildid() + 
                                   "] != [" + DTFNode.getBuildID() + "], you" + 
                                   " can not connect to components from" + 
                                   " different builds.");
        }

        boolean tunneled = 
                        Boolean.valueOf(findAttrib(DTFProperties.DTF_TUNNELED));
        if ( tunneled && !Comm.tunnelExists(getAddress(), getPort()) ) { 
            throw new DTFException("There are no tunnels setup for [" + 
                                   getAddress() + ":" + getPort() + 
                                   "] on DTFC.");
        }
        
        
        // Register myself on the component that just accepted my 
        // ConnectAction object, make sure I dont' have an older
        // connection laying around and if so remove it.
        NodeState ns = NodeState.getInstance();

        /* 
         * Check for a previous register.
         */
        ArrayList<NodeInfo> nodes = ns.getRegisteredNodes();
        for (int i = 0; i < nodes.size(); i++) { 
            NodeInfo nodeinfo = nodes.get(i);
           
            if (nodeinfo.equals(this)) { 
                if (getLogger().isDebugEnabled())
                    getLogger().debug("Found previous register, deleting...");
                
                ns.removeNode(nodeinfo);
            }
        }
        
        /*
         * Create the new register.
         */
        CommClient client = new CommRMIClient(getAddress(), getPort());
        
        if (!client.heartbeat()) { 
            throw new CommException("Unable to heartbeat component [" + 
                                    getId() + "]");
        }
        
        copy(ns.addNode(this,client));
        getLogger().info("Agent " + getId() + " connected, from " + 
                         getAddress() + ":" + getPort());
    }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }
    
    public boolean equals(Object obj) {
        if (obj instanceof Connect) { 
            Connect other = (Connect)obj; 
            
            if ( other.getId() == null || getId() == null )
                return false;
           
            if (other.getId().equals(getId()) && 
                other.getAddress().equals(getAddress()) && 
                other.getPort() == getPort())
                return true;
        }
        
        return false;
    }
    
    @Override
    public int hashCode() {
        assert false: "No hash code assigned";
        return 42;
    } 
    
    public void addAttrib(Attrib attrib) { 
       ArrayList current_attribs = findActions(Attrib.class);
       
       if (!CollectionsUtil.isIn(attrib, current_attribs))
           addAction(attrib);
    }
   
    public void addAttribs(ArrayList attribs) { 
       ArrayList current_attribs = findActions(Attrib.class);
      
       for (int i = 0; i < attribs.size(); i++) { 
           if (!CollectionsUtil.isIn((Attrib)attribs.get(i), current_attribs))
               addAction((Action)attribs.get(i));
       }
    }
    
    public String findAttrib(String name) throws ParseException { 
       ArrayList attribs = findActions(Attrib.class);
      
       for (int i = 0; i < attribs.size(); i++) { 
           Attrib attrib = (Attrib) attribs.get(i);
           if (attrib.getName().equals(name))
               return attrib.getValue();
       }
       
       return null;
    }
   
    /**
     * Just attaching the attributes dangled on the Connect action as well as
     * the hostname, port and id associated with this Connect.
     */
    protected Hashtable getAttribs(Class actionClass) {
        Hashtable result =  super.getAttribs(actionClass);
        ArrayList attribs = findActions(Attrib.class);
        
        for (int i = 0; i < attribs.size(); i++) {
            Attrib attrib = (Attrib)attribs.get(i);
            try {
                result.put(attrib.getName(),attrib.getValue());
            } catch (ParseException e) {
                getLogger().warn("Unable to get property names.",e);
            }
        }
       
        /*
         * Print the host information for this component incase someone needs
         * access to the agent in order to debug something
         */
        if (getAddress() != null)
            result.put("hostname",getAddress());
        
        result.put("port",getPort());

        if ( getId() != null )
            result.put("id",getId());
       
        return result;
    }

    public void setBuildid(String buildid) { this.buildid = buildid; }
    public String getBuildid() { return buildid; }
}
