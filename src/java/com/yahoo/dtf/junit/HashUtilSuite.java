package com.yahoo.dtf.junit;

import java.text.ParseException;
import java.util.Random;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.util.Counter;
import com.yahoo.dtf.util.HashUtil;

/**
 * HashUtil JUnit test, just to validate basic functionality.
 *
 * @author rlgomes
 */
public class HashUtilSuite extends DTFJUnitTest {
   
    @Test(timeout=600000)
    public void simpleSHA1Calculation() throws DTFException, ParseException { 
        String hash1 = HashUtil.sha1("data1");
        String hash2 = HashUtil.sha1("data2");
        
        assert (!hash1.equals(hash2)) : "Collisions detected!";
    }

    @Test(timeout=600000)
    public void smallDataPerformance() throws DTFException, ParseException { 
        long iterations = 200000;
        Random rand = new Random(System.currentTimeMillis());
        byte[] bytes = new byte[128];
        rand.nextBytes(bytes);
        String data = new String(bytes);
      
        Counter counter = new Counter();
        
        counter.start();
        for ( long i = 0; i < iterations; i++ ) 
            HashUtil.sha1(data);
        counter.stop();
        
        double duration = counter.getDurationInSeconds();
        long ops_per_sec = (long)(iterations/duration);
        getLogger().info("sha1/s: " + ops_per_sec);
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(HashUtilSuite.class);
    }
}
