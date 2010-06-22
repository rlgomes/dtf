package com.yahoo.dtf.actions.component;

import java.util.ArrayList;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.flowcontrol.Sequence;
import com.yahoo.dtf.actions.protocol.Lock;
import com.yahoo.dtf.comm.Comm;
import com.yahoo.dtf.comm.CommClient;
import com.yahoo.dtf.comm.CommRMIClient;
import com.yahoo.dtf.components.LockHook;
import com.yahoo.dtf.config.Config;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.util.TimeUtil;

/**
 * @dtf.tag lockcomponent
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc This tag is used to identify and lock the components required 
 *               for the execution of the current test case. When you lock a 
 *               {@dtf.link Component} you can then define a global alias 
 *               by which it will be known for use by other tags such as the 
 *               {@dtf.link Component} tag.
 * 
 * @dtf.tag.example 
 * <local>
 *     <echo>Remote counter retrieval</echo>
 *     <lockcomponent id="DTFA1">
 *         <attrib name="type" value="DTFA"/>
 *     </lockcomponent>
 * </local>
 */
public class Lockcomponent extends Action {

    /**
     * @dtf.attr id
     * @dtf.attr.desc Specifies the internal ID that will be used to identify 
     *                this component in the test case. 
     */
    private String id = null;
    
    /**
     * @dtf.attr name
     * @dtf.attr.desc Specifies the exact name of the component we want to lock,
     *                this name will identify one and only one component that
     *                has been registered with the DTFC. 
     */
    private String name = null;
    
   
    /**
     * @dtf.attr timeout
     * @dtf.attr.desc The timeout in seconds to wait for the component to 
     *                become available to the DTFC. This timeout is useful when
     *                automating testing and wanting to start up the framework 
     *                along with the test driver (DTFX) and not knowing when 
     *                the necessary components are available. Using the timeout
     *                you can estimate the component will be come available with
     *                in X amount of time. 
     */
    private String timeout = null;
   
    public Lockcomponent() { }
   
    /**
     * internal method which is responsible for assembling the the Lock
     * to be sent to the DTFC.
     * 
     * @return
     * @throws ParseException 
     */
    protected Lock assembleLock() throws DTFException { 
        Lock lock = new Lock(getName(),getId(),getTimeout());
        lock.setXMLLocation(this);
        
        ArrayList aux = findActions(Attrib.class);
        ArrayList<Attrib> attribs = new ArrayList<Attrib>();
        
        /*
         * Add all Attribs that we're looking for in a component
         * make sure to resolve all properties before shipping this off...
         */
        for (int i = 0; i < aux.size(); i++) { 
            Attrib cur = (Attrib)aux.get(i);
            Attrib tmp = new Attrib(cur.getName(),
                                    cur.getValue(),
                                    cur.isTestProp());
            attribs.add(tmp);
        }
        lock.addActions(attribs);
        
        return lock;
    }
    
    /**
     * internal method which is responsible for handling the returned lock and
     * calling the required lock hooks.
     * 
     * @param lock
     * @throws DTFException
     */
    protected void handleLockResponse(Lock lock) throws DTFException { 
        /*
         * If the connection from the controller is tunneled then we have
         * no other choice but to connect through the controller.
         */
        if (!lock.getTunneled()) { 
            CommClient client = new CommRMIClient(lock.getAddress(),
                    lock.getPort());
           
            if (client.heartbeat().booleanValue()) { 
                if (getLogger().isDebugEnabled())
                    getLogger().debug("Direct connection to component " +
                                      "being used.");

                Comm.addClient(lock.getId(), client);
            }
        } else {
            if (getLogger().isDebugEnabled()) {
                getLogger().debug("Tunneling detected, talking to the "  +
                                  "component throught the controller");
            }
        }
        
        /*
         * Record  the right attributes as test properties in the record 
         * and set the right test properties for the others.
         */
        ArrayList<Attrib> attribs = lock.findActions(Attrib.class);
        Config config = getConfig();
        for(int i = 0; i < attribs.size(); i++) { 
            Attrib attrib = (Attrib)attribs.get(i);
            
            if (attrib.isTestProp())
                config.setProperty(getId() + "." + attrib.getName(),
                                   attrib.getValue());
            
            getResults().recordProperty(getId() + "." + attrib.getName(), 
                                        attrib.getValue());
        }

        getComponents().registerComponent(getId(), lock);
        getLogger().info("Component locked " + lock + " as " + getId());

        // execute LockHooks!
        for (int i = 0; i < _lockhooks.size(); i++) {
            ArrayList<Action> actions = _lockhooks.get(i).init(this.getId());
            Sequence sequence = new Sequence();
            sequence.addActions(actions);
            getComm().executeOnComponent(id, sequence);
        } 
    }
  
    // tricky code not for anyone to change.. :) 
    public void execute() throws DTFException { 
        // Whenever we haven't been named lets make sure to register first.
        getComm().checkAndConnectToDTFC();
        
        Lock lock = assembleLock();
        
        Action result = getComm().sendActionToCaller("dtfc", lock);
        
        // TODO: why not execute the result ? 
        if (result != null) {
            Lock returnedLock = (Lock)result.findAllActions(Lock.class).get(0);
            handleLockResponse(returnedLock);
        } else { 
            throw new DTFException("Unable to register component :(.");
        }
    }
   
    private static ArrayList<LockHook> _lockhooks = new ArrayList<LockHook>();
    public static void registerLockHook(LockHook hook) { 
        _lockhooks.add(hook);
    }
    
    public static ArrayList<LockHook> getLockHooks() { return _lockhooks; }

    public String getId() throws ParseException { return replaceProperties(id); }
    public void setId(String id) { this.id = id; }

    public String getName() throws ParseException { return replaceProperties(name); }
    public void setName(String name) { this.name = name; }

    public long getTimeout() throws ParseException { 
        return TimeUtil.parseTime("timeout",replaceProperties(timeout)); 
    }
    public void setTimeout(String timeout) { this.timeout = timeout; }
}
