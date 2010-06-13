package com.yahoo.dtf.actions.component;

import java.util.ArrayList;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.flowcontrol.Sequence;
import com.yahoo.dtf.actions.protocol.Lock;
import com.yahoo.dtf.components.ComponentHook;
import com.yahoo.dtf.components.ComponentReturnHook;
import com.yahoo.dtf.components.ComponentUnlockHook;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.state.DTFState;

/**
 * @dtf.tag component
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc This tag encapsulates the remote tags that are to be executed 
 *               on components that have been locked and identified by the 
 *               {@dtf.link Lockcomponent} tag. The children tags specified 
 *               within this tag are executed on the component identified by the
 *               id attribute and all of the events thrown by those children 
 *               tags are replayed back on the runner as if the actions had 
 *               occurred localy so you can record them as you would any local
 *               activities.
 * 
 * @dtf.tag.example 
 * <component id="DTFA1">
 *     <echo>***********************************</echo>
 *     <echo>This is being printed from the dtfx</echo>
 *     <echo>***********************************</echo>
 * </component>
 * 
 * @dtf.tag.example 
 * <component id="DTFA2">
 *     <sleep time="3s"/>
 *     <echo>This is being printed from the dtfx</echo>
 * </component>
 */
public class Component extends Action {

    /**
     * @dtf.attr id
     * @dtf.attr.desc The unique identifier of a component already locked with 
     *                the {@dtf.link Lockcomponent} tag.
     */
    private String id = null;
   
    public Component() { }
    
    private static ArrayList<ComponentHook> _hooks =  
                                                 new ArrayList<ComponentHook>();

    private static ArrayList<ComponentReturnHook> _rhooks =  
                                           new ArrayList<ComponentReturnHook>();

    private static ArrayList<ComponentUnlockHook> _uhooks =  
                                           new ArrayList<ComponentUnlockHook>();
    
    public static void registerComponentHook(ComponentHook hook) { 
        _hooks.add(hook);
    }

    public static ArrayList<ComponentHook> getComponentHooks() { 
        return _hooks;
    }

    public static void registerComponentReturnHook(ComponentReturnHook hook) { 
        _rhooks.add(hook);
    }

    public static ArrayList<ComponentReturnHook> getComponentReturnHooks() { 
        return _rhooks;
    }

    public static void registerComponentUnlockHook(ComponentUnlockHook hook) { 
        _uhooks.add(hook);
    }

    public static ArrayList<ComponentUnlockHook> getComponentUnlockHooks() { 
        return _uhooks;
    }

    public void execute() throws DTFException {
        execute(true);
    }
    
    public void execute(boolean withhooks) throws DTFException {
        DTFState state = getState();
        Lock lock = state.getComponents().getComponent(getId());
       
        Sequence sequence = new Sequence();
        // very important, this thread id is used to create a base config for 
        // this thread on the agent side that is used to store the unchanged
        // properties, ranges, etc.
        sequence.setThreadID(Thread.currentThread().getName());
      
        String id = getId();
        state.disableReplace();
        try { 
            /*
             * We can't try to do handleComponent work on the DTFC, that is 
             * pointless and will most likely result in some unpredictable 
             * error.
             */
	        if ( withhooks ) {
	            for (int i = 0; i < _hooks.size(); i++) { 
	                long start = System.currentTimeMillis();
	                ArrayList<Action> others = 
	                                      _hooks.get(i).handleComponent(id);
	                sequence.addActions(others);
	                long stop = System.currentTimeMillis();
	                if ( Action.getLogger().isDebugEnabled() ) {
	                    Action.getLogger().
	                           debug(_hooks.get(i).getClass().getSimpleName() + 
	                                 " took " + (stop-start) + "ms.");
	                }
	            }
	        }

	        sequence.addActions(children());
	        Action result = getComm().sendAction(lock.getId(), sequence);
                
            if ( result != null ) result.execute();
        } catch (DTFException e) { 
            e.setComponent(getId());
            throw e;
        } finally { 
            state.enableReplace();
        }
    }
    
    public String getId() throws ParseException { return replaceProperties(id); }
    public void setId(String id) { this.id = id; }
}
