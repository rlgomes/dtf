package com.yahoo.dtf.junit;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import junit.framework.JUnit4TestAdapter;
import junit.framework.TestCase;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.yahoo.dtf.DTFConstants;
import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.flowcontrol.Sequence;
import com.yahoo.dtf.actions.share.ShareOperation;
import com.yahoo.dtf.actions.util.EmptyAction;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.exception.PropertyException;
import com.yahoo.dtf.share.Share;
import com.yahoo.dtf.share.ShareFactory;

/**
 * 
 * @author rlgomes
 */
public class ShareSuite extends DTFJUnitTest {
   
    @BeforeClass
    public static void startUpNode() { 
        System.setProperty("dtf.node.type", DTFConstants.DTFX_ID);
        System.setProperty("dtf.node.name", "dummy");
        try {
            DTFNode.init();
        } catch (DTFException e) {
            e.printStackTrace();
            TestCase.fail("Unable to initialize DTF teststack.");
        }
    }
  
    @Test(timeout=600000)
    public void createSetAndDestroy() throws DTFException { 
        ArrayList<String> shares = ShareFactory.getShareNames();
        
        for (int i = 0; i < shares.size(); i++) { 
            Share share = ShareFactory.getShare(shares.get(i), "TESTSHARE");
            share.set(new Sequence());
            share.get(false).execute();
            ShareOperation.getShares().remove("TESTSHARE");
        }
    }
   
    @Ignore
    private static class SimpleSetter implements Runnable { 
        private Share share;
        private int iterations;
        
        private Throwable failure = null;
        
        public SimpleSetter(Share share, int iterations) throws PropertyException, ParseException { 
            this.share = share;
            this.iterations = iterations;
        }
        
        public void run() {
            try {
                for (long j = 0; j < iterations; j++) { 
                    Sequence sequence = new Sequence();
                    share.set(sequence);
                }
            } catch (Throwable t) {
                failure = t;
            }
        }
        
        public Throwable getFailure() { 
            return failure;
        }
    }
    
    @Ignore
    private static class SimpleGetter implements Runnable { 
        private Share share;
        private String sharename;
        private long iterations;
        
        private boolean blockncheck = false;
        private Throwable failure = null;
        
        public SimpleGetter(Share share, String sharename, long iterations, boolean blockncheck) { 
            this.share = share;
            this.sharename = sharename;
            this.iterations = iterations;
            this.blockncheck = blockncheck;
        }
        
        public void run() {
            try {
                for (long j = 0; j < iterations; j++) { 
                    Action action = share.get(blockncheck);
                    assert (action != null) : 
                           "Share [" + sharename + "] returns nulls! this is not valid";
             
                    // no EmptyAction acceptable when blocking.
                    if ( blockncheck ) { 
		                assert !(action instanceof EmptyAction) : 
		                          " when blocking on get you must return an action.";
                    } 
                    
                    action.execute();
                }
            } catch (Throwable t) {
                failure= t;
            }
        }

        public Throwable getFailure() { 
            return failure;
        }
    }

    @Test(timeout=600000)
    public void concurrentAccess() throws DTFException { 
        ArrayList<String> shares = ShareFactory.getShareNames();
        
        final int ITERATIONS = getConfig().getPropertyAsInt("iterations", 50000);
        
        for (int i = 0; i < shares.size(); i++) { 
            ExecutorService exec = Executors.newFixedThreadPool(10);
            getLogger().info("Testing [" + shares.get(i) + "]");
            final String sharename = shares.get(i);
            final Share share = ShareFactory.getShare(sharename, "TESTSHARE");
            ArrayList<SimpleSetter> setters = new ArrayList<SimpleSetter>();
            ArrayList<SimpleGetter> getters = new ArrayList<SimpleGetter>();
            
            for (int s = 0; s < 5; s++) {
                SimpleSetter ss = new SimpleSetter(share,ITERATIONS);
                setters.add(ss);
                exec.execute(ss);
            }

            for (int g = 0; g < 5; g++) {
                SimpleGetter sg = new SimpleGetter(share,sharename,ITERATIONS,false);
                getters.add(sg);
                exec.execute(sg);
            }
            
            exec.shutdown();
            try {
                exec.awaitTermination(600000, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
	            e.printStackTrace();
	            TestCase.fail("Unable to wait for termination of threads.");
	            break;
            }
            
            for (int t = 0; t < setters.size(); t++) { 
                if ( setters.get(t).getFailure() != null ) { 
                    setters.get(t).getFailure().printStackTrace();
                    TestCase.fail("failed to execute setter.");
                }
            }

            for (int t = 0; t < getters.size(); t++) { 
                if ( getters.get(t).getFailure() != null ) { 
                    getters.get(t).getFailure().printStackTrace();
                    TestCase.fail("failed to execute getter.");
                }
            }
            
            ShareOperation.getShares().remove("TESTSHARE");
        }
    }

    @Test(timeout=600000)
    public void validateBlockingGets() throws DTFException { 
        ArrayList<String> shares = new ArrayList<String>();
        shares.add("queue");
        
        final int ITERATIONS = getConfig().getPropertyAsInt("iterations", 100);
        
        for (int i = 0; i < shares.size(); i++) { 
            ExecutorService exec = Executors.newFixedThreadPool(10);
            getLogger().info("Testing [" + shares.get(i) + "]");
            final String sharename = shares.get(i);
            final Share share = ShareFactory.getShare(sharename, "TESTSHARE");
            ArrayList<SimpleSetter> setters = new ArrayList<SimpleSetter>();
            ArrayList<SimpleGetter> getters = new ArrayList<SimpleGetter>();
            
            for (int s = 0; s < 3; s++) {
                SimpleSetter ss = new SimpleSetter(share,ITERATIONS);
                setters.add(ss);
                exec.execute(ss);
            }

            for (int g = 0; g < 3; g++) {
                SimpleGetter sg = new SimpleGetter(share,sharename,ITERATIONS,true);
                getters.add(sg);
                exec.execute(sg);
            }
            
            exec.shutdown();
            try {
                exec.awaitTermination(600000, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
	            e.printStackTrace();
	            TestCase.fail("Unable to wait for termination of threads.");
	            break;
            }

            for (int t = 0; t < setters.size(); t++) { 
                if ( setters.get(t).getFailure() != null ) { 
                    setters.get(t).getFailure().printStackTrace();
                    TestCase.fail("failed to execute setter.");
                }
            }

            for (int t = 0; t < getters.size(); t++) { 
                if ( getters.get(t).getFailure() != null ) { 
                    getters.get(t).getFailure().printStackTrace();
                    TestCase.fail("failed to execute getter.");
                }
            }
            
            ShareOperation.getShares().remove("TESTSHARE");
        }
    }
    

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ShareSuite.class);
    }
}
