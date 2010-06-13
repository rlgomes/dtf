package com.yahoo.dtf.junit;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

import junit.framework.JUnit4TestAdapter;
import junit.framework.TestCase;

import org.junit.BeforeClass;
import org.junit.Test;

import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.exception.PropertyException;
import com.yahoo.dtf.util.DTFRandom;

/**
 *
 * @author rlgomes
 */
public class RandomGenSuite extends DTFJUnitTest {

    private NumberFormat percentage = new DecimalFormat("##.00");
    
    private long seed = System.nanoTime();
    
    private Random[] gens = new Random[]{ new Random(seed),
                                          new DTFRandom(seed),
                                        };

    @BeforeClass
    public static void setup() { 
        try {
            DTFNode.init();
        } catch (PropertyException e) {
            e.printStackTrace();
            TestCase.fail("Unable to retrieve proprety.");
        } catch (ParseException e) {
            e.printStackTrace();
            TestCase.fail("Unable to retrieve proprety.");
        } catch (DTFException e) {
            e.printStackTrace();
            TestCase.fail("Unable to init DTF.");
        }
    }
    
    @Test(timeout=600000)
    public void distributionTestForSmallDataset() throws PropertyException, ParseException {
        getLogger().info("Small Dataset Distribution");
        int samplesize = 10;
        long iterations = getConfig().getPropertyAsInt("iterations",1000);
       
        for (int r = 0; r < gens.length; r++) { 
            int[] occurences = new int[samplesize];
	        Random rand = gens[r];
	        String rname = rand.getClass().getSimpleName();
	        
	        for (int i = 0; i < iterations; i++) { 
	            occurences[rand.nextInt(samplesize)]++;
	        }
	        
	        double max = Float.MIN_VALUE;
	        double ideal = (double)iterations/samplesize;
	       
	        for (int i = 0; i < samplesize; i++) { 
	            double diff = 0;
	            
	            if ( ideal > occurences[i] ) 
	                diff = ideal - occurences[i];
	            else 
	                diff = occurences[i] - ideal;
	            
	            if ( diff > max )
	                max = diff;
	        }
	      
	        getLogger().info(rname + " offby " +
	                         percentage.format((max/ideal)*100.0f) + "%");
        }
    }

    @Test(timeout=600000)
    public void distributionTestForLargeDataset() throws PropertyException, ParseException {
        getLogger().info("Large Dataset Distribution");
        int samplesize = 10;
        long iterations = getConfig().getPropertyAsInt("iterations",10000000);
       
        for (int r = 0; r < gens.length; r++) { 
            int[] occurences = new int[samplesize];
	        Random rand = gens[r];
	        String rname = rand.getClass().getSimpleName();
	        
	        for (int i = 0; i < iterations; i++) { 
	            occurences[rand.nextInt(samplesize)]++;
	        }
	        
	        double max = Float.MIN_VALUE;
	        double ideal = (float)iterations/samplesize;
	       
	        for (int i = 0; i < samplesize; i++) { 
	            double diff = 0;
	            
	            if ( ideal > occurences[i] ) 
	                diff = ideal - occurences[i];
	            else 
	                diff = occurences[i] - ideal;
	            
	            if ( diff > max )
	                max = diff;
	        }
	      
	        getLogger().info(rname + " offby " + 
	                         percentage.format((max/ideal)*100.0f) + "%");
        }
    }

    @Test(timeout=600000)
    public void nextIntPerformanceTest() throws PropertyException, ParseException {
        long iterations = getConfig().getPropertyAsInt("iterations",10000000);
      
        for (int r = 0; r < gens.length; r++) { 
	        Random rand = gens[r];
	        String rname = rand.getClass().getSimpleName();
            long start = System.currentTimeMillis();
	        for (long i = 0; i < iterations; i++) { 
	            rand.nextInt();
	        }
	        long stop = System.currentTimeMillis();
        
	        double duration = (stop-start)/1000.0f;
	        getLogger().info(rname + " ops/sec: " + (int)(iterations/duration));
        }
    }
   
    @Test(timeout=600000)
    public void nextLongPerformanceTest() throws PropertyException, ParseException {
        long iterations = getConfig().getPropertyAsInt("iterations",10000000);
      
        for (int r = 0; r < gens.length; r++) { 
            Random rand = gens[r];
            String rname = rand.getClass().getSimpleName();
            long start = System.currentTimeMillis();
            for (long i = 0; i < iterations; i++) { 
                rand.nextLong();
            }
            long stop = System.currentTimeMillis();
        
            double duration = (stop-start)/1000.0f;
            getLogger().info(rname + " ops/sec: " + (int)(iterations/duration));
        }
    }

    @Test(timeout=600000)
    public void nextBytesThroughputPerformanceTest() throws PropertyException, ParseException {
        long iterations = getConfig().getPropertyAsInt("iterations",4096);
        int size = 32*1024;
      
        for (int r = 0; r < gens.length; r++) { 
            Random rand = gens[r];
            String rname = rand.getClass().getSimpleName();
            byte[] bytes = new byte[size];
            long start = System.currentTimeMillis();
            for (long i = 0; i < iterations; i++) { 
                rand.nextBytes(bytes);
            }
            long stop = System.currentTimeMillis();
        
            double duration = (stop-start)/1000.0f;
            getLogger().info(rname + " bytes/sec: " + (int)((iterations*size)/duration));
        }
    }

    @Test(timeout=600000)
    public void validateDataTest() throws DTFException {
        long iterations = getConfig().getPropertyAsInt("iterations",20*1024);
        int size = 32*1024;
     
        // we only validate DTFRandom because the other Random always had this 
        // issue
        DTFRandom rand = new DTFRandom();
        getLogger().info("Validating data for DTFRandom.");
        byte[] bytes = new byte[size];
        for (long i = 0; i < iterations; i++) { 
            rand.nextBytes(bytes);
            for (int b = 0; b < bytes.length-1; b++) { 
                if ( bytes[b] == '$' && bytes[b+1] == '{') 
                    throw new DTFException("Found a property within the randomly generated data.");
            }
        }
    }
   
    @Test(timeout=600000)
    public void simpleTest() { 
        DTFRandom rand = new DTFRandom(1234567890);
       
        getLogger().info("Generating nextInt() sequence from seed 1234567890");
        StringBuffer results = new StringBuffer();
        for (int i = 0; i < 10; i++) 
            results.append(rand.nextInt() + ",");

        getLogger().info(results.toString());

        byte[] bytes = new byte[16];
        rand.nextBytes(bytes);
        getLogger().info("randomString [" + new String(bytes) + "]");
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(RandomGenSuite.class);
    }
}
