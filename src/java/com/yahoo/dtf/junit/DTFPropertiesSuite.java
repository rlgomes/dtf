package com.yahoo.dtf.junit;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;
import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.yahoo.dtf.config.Properties;
import com.yahoo.dtf.exception.DTFException;

/**
 * Properties needs to be tested throughly because its a custom implementation
 * of the Java Properties class that tries to be more memory efficient while
 * giving the right property isolation between parent and children threads.
 *
 * @author rlgomes
 */
public class DTFPropertiesSuite extends DTFJUnitTest {

    private static int CLONE_DEPTH = 20;
    
    /**
     * Validate that properties are correctly isolated in underlying clones in 
     * a way that does not overwrite the value of the original Properties object
     * and they always contain the correct value for this specific clone.
     * 
     * P1 has {{a,a}}
     *
     * P2 = P1.clone()
     * 
     * P1 has {{a,a}}
     * P2 has {{a,a}}
     * 
     * P2.set(a,1)
     * 
     * P1 has {{a,a}}
     * P2 has {{a,1}}
     */
    @Test
    public void propertyIsolation() { 
        Properties props = new Properties();
        props.put("a","a");
        
        Properties[] clones = new Properties[CLONE_DEPTH+1];
        clones[0] = props;
        for (int i = 1; i < CLONE_DEPTH+1; i++) { 
	        clones[i] = (Properties) clones[i-1].clone();
	        clones[i].put("a","" + i);

	        Assert.assertTrue(props.get("a").equals("a"));
	        Assert.assertTrue(clones[i].get("a").equals("" + i));
	        for (int j = i; j > 0; j--) { 
	            Assert.assertTrue(clones[j].get("a").equals("" + j));
	        }
        }
    }
    
    /**
     * Validate that property inheritance works correctly independently of how
     * many cloned generations there are. Basically the property defined in the
     * original Properties object should never be lost.
     * 
     * P1 has {{a,a},{z,z}}
     *
     * P2 = P1.clone()
     * 
     * P1 has {{a,a},{z,z}}
     * P2 has {{a,a},{z,z}}
     * 
     * P2.set(a,1)
     * 
     * P1 has {{a,a},{z,z}}
     * P2 has {{a,1},{z,z}}
     */
    @Test(timeout=60000)
    public void propertyInheritance() { 
        Properties props = new Properties();
        props.put("a","a");
        props.put("z","z");
        
        Properties[] clones = new Properties[CLONE_DEPTH+1];
        clones[0] = props;
        for (int i = 1; i < CLONE_DEPTH+1; i++) { 
            clones[i] = (Properties) clones[i-1].clone();
            clones[i].put("a","" + i);

            Assert.assertTrue(clones[i].get("z").equals("z"));
            for (int j = i; j > 0; j--) { 
                Assert.assertTrue(clones[j].get("z").equals("z"));
            }
        }
    }

    /**
     * Verify that property resolution works even for properties that have been 
     * moved into the read/write list and that access to that same property is
     * not done in a wrong manner just because of the cloning.
     *
     * P1 has {{a,a}} # but a is now known to be in the r/w properties list
     *
     * P2 = P1.clone()
     * 
     * P1 has {{a,a}}
     * P2 has {{a,a}}
     * 
     * P2.set(a,1)
     * 
     * P1 has {{a,a}}
     * P2 has {{a,1}}
     */
    @Test(timeout=60000)
    public void propertyResolution() { 
        Properties props = new Properties();
        props.put("a","a");
        props.put("a","a");
        
        Properties[] clones = new Properties[CLONE_DEPTH+1];
        clones[0] = props;
        for (int i = 1; i < CLONE_DEPTH+1; i++) { 
            clones[i] = (Properties) clones[i-1].clone();
            clones[i].put("a",""+i);

            Assert.assertTrue(clones[i].get("a").equals(""+i));
            for (int j = i; j > 0; j--) { 
	            Assert.assertTrue(clones[j].get("a").equals(""+j));
            }
        }
    }
    
    private static double ITERATIONS = 10000;
   
    /**
     * Put performance test for comparison if anyone tries to make a better 
     * implementation.
     */
    @Test(timeout=60000) 
    public void putPerf() { 
        Properties props = new Properties();
       
        double start = System.currentTimeMillis();
        for(long i = 0; i < ITERATIONS; i++) { 
            props.put("key" + i, "value" + i);
        }
        double stop = System.currentTimeMillis();
       
        
        double duration = (stop-start)/1000;
        double ops_per_s = ITERATIONS/duration;
       
        System.out.println("puts/sec: " + ops_per_s);
    }

    /**
     * Put performance test for comparison if anyone tries to make a better 
     * implementation. This test differs from the previous because its done
     * on the cloned Properties object and not the original Properties object.
     */
    @Test(timeout=60000) 
    public void putPerfInCopy() { 
        Properties pprops = new Properties();
        Properties props = null;
        
        String data = new String(new byte[32]);
        
        for(long i = 0; i < 5000; i++)
            pprops.put("key" + i, data + i);
        
        props = (Properties) pprops.clone();
        props = (Properties) props.clone();
       
        double start = System.currentTimeMillis();
        for(long i = 0; i < ITERATIONS; i++) 
            props.put("key" + i, data + i);
        double stop = System.currentTimeMillis();
        
        double duration = (stop-start)/1000;
        double ops_per_s = ITERATIONS/duration;
       
        System.out.println("Puts/sec: " + ops_per_s);
    }
   
    /**
     * Clone performance test.
     */
    @Test(timeout=60000) 
    public void clonePerf() { 
        Properties props = new Properties();
       
        String data = new String(new byte[32]);
        for(long i = 0; i < ITERATIONS; i++) { 
            props.put("key" + i, data + i);
        }
        
        double start = System.currentTimeMillis();
        for(long i = 0 ; i < ITERATIONS; i++) { 
            props.clone();
        }
        double stop = System.currentTimeMillis();
       
        double duration = (stop-start)/1000;
        double ops_per_s = ITERATIONS/duration;
       
        System.out.println("clones/sec: " + ops_per_s);
    }
   
    /**
     * Validate that removal of a property from a cloned Propertie object does 
     * not remove the same key from the original Properties object.
     */
    @Test(timeout=60000)
    public void removeTest() { 
        Properties pprops = new Properties();
        pprops.put("a", "b");
       
        Properties cprops = (Properties) pprops.clone();

        Assert.assertTrue(pprops.containsKey("a"));
        Assert.assertEquals(pprops.get("a"),"b");
        
        Assert.assertTrue(cprops.containsKey("a"));
        Assert.assertEquals(cprops.get("a"),"b");
        
        cprops.remove("a");

        Assert.assertFalse(cprops.containsKey("a"));
        Assert.assertEquals(cprops.get("a"),null);
    }
   
    /**
     * Simple enumeration test to validate that when we print all the 
     * available keys in a cloned Properties object that it doesn't include
     * repeated keys.
     * @throws DTFException 
     */
    @Test
    public void enumerationTest() throws DTFException { 
        Properties pprops = new Properties();
        pprops.put("a", "b");
        pprops.put("c", "c");
        pprops.put("a", "a");
        
        Properties cprops = (Properties) pprops.clone();
        
        cprops.put("c", "c");
        cprops.put("z", "z");
        cprops.put("a", "a");
        cprops.put("d", "d");
        cprops.put("b", "b");
        
        Enumeration<Object> elements = cprops.keys();
        
        HashMap<String,AtomicInteger> expected_elements = 
                                           new HashMap<String, AtomicInteger>();
        
        expected_elements.put("a", new AtomicInteger());
        expected_elements.put("b", new AtomicInteger());
        expected_elements.put("c", new AtomicInteger());
        expected_elements.put("z", new AtomicInteger());
        expected_elements.put("d", new AtomicInteger());
        
        while ( elements.hasMoreElements() ) { 
            String elem = elements.nextElement().toString();
            expected_elements.get(elem).incrementAndGet();
            System.out.println("elem " + elem);
        }
        
        for (Entry<String, AtomicInteger> entry : expected_elements.entrySet()) { 
            if ( entry.getValue().intValue() != 1 ) { 
                Assert.fail(entry.getKey() + " => " + entry.getValue() +  
                            ", expected 1");
            }
        }
    }
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DTFPropertiesSuite.class);
    }
}
