package com.yahoo.dtf.junit;

import java.text.ParseException;
import java.util.ArrayList;

import junit.framework.JUnit4TestAdapter;
import junit.framework.TestCase;

import org.junit.BeforeClass;
import org.junit.Test;

import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.component.Lockcomponent;
import com.yahoo.dtf.actions.function.Function;
import com.yahoo.dtf.actions.protocol.Lock;
import com.yahoo.dtf.actions.reference.Referencable;
import com.yahoo.dtf.components.LockHook;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.functions.Functions;
import com.yahoo.dtf.references.References;

/**
 * ComponentHook JUnit testsuite to validate how much time is spent per request
 * when processing the available component hooks.
 * 
 * @author rlgomes
 */
public class LockHookSuite extends DTFJUnitTest {
    
    private static final int COMPONENT_COUNT = 128;
    private static final int FUNCTION_COUNT = 1000;
    private static final int REFERENCE_COUNT = 1000;
  
    @Test(timeout=600000)
    public void lockHookTimer() throws DTFException, ParseException { 
        ArrayList<LockHook> hooks = Lockcomponent.getLockHooks();

        getLogger().info("");
        getLogger().info("Testing LockHooks with:");
        getLogger().info(" * " + FUNCTION_COUNT + " functions registered.");
        getLogger().info(" * " + REFERENCE_COUNT + " references created.");
        getLogger().info(" * " + COMPONENT_COUNT + " components locked.");
        getLogger().info(" * dummy communication layer being used.");
        getLogger().info("");
        
        for (int i = 0; i < hooks.size(); i++) { 
            LockHook hook = hooks.get(i);
            
            long start = System.currentTimeMillis();
	        for (int l = 0; l < COMPONENT_COUNT; l++) {
		        Lockcomponent lc = new Lockcomponent();
		        lc.setId("ID" + l);
	            hook.init(lc.getId());
	        }
            long stop = System.currentTimeMillis();
            getLogger().info(hook.getClass().getSimpleName() + 
                             " hook took " + (stop-start)/COMPONENT_COUNT + 
                             "ms on average.");
        }
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(LockHookSuite.class);
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

        // create some dummy functions to send to the components at lock-time
        Functions functions = Action.getState().getFunctions();
        for (int i = 0; i < FUNCTION_COUNT; i++) { 
	        Function function = new Function();
	        function.setName("function-" + i);
	        function.setExport("true");
            Function.getExportableFunctions().add(function);
	        functions.addFunction(function.getName(), function);
        }

        // create some dummy references to send to the components at lock-time
        References references = Action.getState().getReferences();
        for (int i = 0; i < REFERENCE_COUNT; i++) { 
            Referencable ref = new Referencable();
            ref.setId("reference-" + i);
            references.addReference(ref.getId(), ref);
        }
    }
}
