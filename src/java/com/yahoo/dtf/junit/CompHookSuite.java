package com.yahoo.dtf.junit;

import java.text.ParseException;
import java.util.ArrayList;

import junit.framework.JUnit4TestAdapter;
import junit.framework.TestCase;

import org.junit.BeforeClass;
import org.junit.Test;

import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.component.Component;
import com.yahoo.dtf.actions.protocol.Lock;
import com.yahoo.dtf.components.ComponentHook;
import com.yahoo.dtf.config.Config;
import com.yahoo.dtf.exception.DTFException;

/**
 * 
 * @author rlgomes
 */
public class CompHookSuite extends DTFJUnitTest {
    
    private static final int COMPONENT_COUNT = 128;
    private static final int PROPERTY_COUNT = 10000;
  
    @Test(timeout=600000)
    public void componentHookTimer() throws DTFException, ParseException { 
        ArrayList<ComponentHook> hooks = Component.getComponentHooks();

        getLogger().info("");
        getLogger().info("Testing ComponentHooks with:");
        getLogger().info(" * " + PROPERTY_COUNT + " properties created.");
        getLogger().info(" * " + COMPONENT_COUNT + " components locked.");
        getLogger().info(" * dummy communication layer being used.");
        getLogger().info("");

        for (int i = 0; i < hooks.size(); i++) { 
            ComponentHook hook = hooks.get(i);
            
            long start = System.currentTimeMillis();
            for (int l = 0; l < COMPONENT_COUNT; l++) {
                hook.handleComponent("ID"+l);
            }
            long stop = System.currentTimeMillis();
            getLogger().info(hook.getClass().getSimpleName() + 
                             " hook took " + (stop-start)/COMPONENT_COUNT + 
                             "ms on average.");
        }
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CompHookSuite.class);
    }
    
    @BeforeClass
    public static void startUpNode() throws DTFException { 
        System.setProperty("dtf.node.type", "dtfc");
        System.setProperty("dtf.node.name", "dummy");

        System.setProperty("java.security.policy","src/config/policy.txt");
        
        try {
            DTFNode.init();
        } catch (DTFException e) {
            e.printStackTrace();
            TestCase.fail("Unable to initialize DTF teststack.");
        }

        // set the dummy comm layer that does nothing...
        Action.getState().setComm(new DummyComm(Action.getConfig()));
        
        // add a few dummy component locks
        for (int i = 0; i < COMPONENT_COUNT; i++) { 
	        Lock lock = new Lock();
	        lock.setId("ID" + i);
	        Action.getComponents().registerComponent(lock.getId(), lock);
        }
        
        // create some dummy references to send to the components at lock-time
        Config config = Action.getConfig();
        for (int i = 0; i < PROPERTY_COUNT; i++) { 
            config.setProperty("property-"+i, i);
        }
    }
}
