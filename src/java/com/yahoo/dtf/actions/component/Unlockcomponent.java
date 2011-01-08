package com.yahoo.dtf.actions.component;

import java.util.ArrayList;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.protocol.Lock;
import com.yahoo.dtf.actions.protocol.Unlock;
import com.yahoo.dtf.comm.Comm;
import com.yahoo.dtf.comm.CommClient;
import com.yahoo.dtf.components.ComponentUnlockHook;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag unlockcomponent
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
public class Unlockcomponent extends Action {

    /**
     * @dtf.attr id
     * @dtf.attr.desc Identifies the component to be unlocked by this testcase.
     *                When a testcase finishing executing it will also issue
     *                unlockcomponent request for each of the components that 
     *                were locked and never unlocked by the testcase.
     */
    private String id = null;
    
    public Unlockcomponent() { }
    
    public void execute() throws DTFException { 
        Lock lock = getComponents().getComponent(getId());
       
        getLogger().info("Unlocking " + lock.getId());
        Unlock unlock = new Unlock(lock.getId(), lock.getOwner());

        getComm().sendActionToCaller("dtfc", unlock).execute();
        getComponents().unregisterComponent(getId());
        
        ArrayList<ComponentUnlockHook> hooks = 
                                    Component.getComponentUnlockHooks();
        for (int i = 0; i < hooks.size(); i++) { 
            hooks.get(i).unlockComponent(getId());
            
            CommClient cc = Comm.removeClient(getId());
            if ( cc != null) cc.shutdown();
        }
       
        getLogger().info("Component unlocked " + getId());
    }
    
    public String getId() throws ParseException { return replaceProperties(id); }
    public void setId(String id) { this.id = id; }
}
