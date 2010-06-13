package com.yahoo.dtf.junit;

import java.util.regex.Pattern;

import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.JUnit4TestAdapter;
import junit.framework.TestCase;

import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.exception.PropertyException;
import com.yahoo.dtf.util.NumberUtil;

public class NumberUtilSuite extends DTFJUnitTest {

    private static Pattern longPattern = Pattern.compile("[0-9]{1,19}+");

    private static Pattern doublePattern = Pattern.compile("[0-9]*\\.?[0-9]*");

    private static int ITERATIONS = 1000000;
    
    @BeforeClass
    public static void setup() { 
        try {
            DTFNode.init();
            ITERATIONS = getConfig().getPropertyAsInt("iterations", 1000000);
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
    public void isLongPerfTest() { 
        
        long start,stop;
        
        String[] data = new String[]{"1000000","1233334a"};
        
        for (int s = 0; s < data.length; s++) { 
            String value = data[s];
            getLogger().info("Testing [" + value + "]");
           
            start = System.currentTimeMillis();
            for(int i = 0; i < ITERATIONS; i++) { 
               NumberUtil.isLong(value);
            }
            stop = System.currentTimeMillis();
            getLogger().info("NumberUtil.isLong() took: " + (stop-start) + "ms");
            
            start = System.currentTimeMillis();
	        for(int i = 0; i < ITERATIONS; i++) { 
	            longPattern.matcher(value).matches();
	        }
            stop = System.currentTimeMillis();
            getLogger().info("Regular expression took:  " + (stop-start) + "ms");
	
            start = System.currentTimeMillis();
	        for(int i = 0; i < ITERATIONS; i++) { 
	            isLong(value);
	        }
            stop = System.currentTimeMillis();
            getLogger().info("Long.valueOf() took:      " + (stop-start) + "ms");

            getLogger().info("regular expression matching: " + longPattern.matcher(value).matches());
            getLogger().info("NumberUtil.isLong:           " + NumberUtil.isLong(value));
            getLogger().info("isLong using Long.valueOf(): " + isLong(value));
        }
    }

    public static boolean isLong(String string) {
        try { 
            Long.valueOf(string);
            return true;
        } catch (NumberFormatException e) { 
            return false;
        }
    }

    @Test(timeout=600000)
    public void isDoublePerfTest() { 
        
        long start,stop;
        
        String[] data = new String[]{"100.123","123.334a"};
        
        for (int s = 0; s < data.length; s++) { 
            String value = data[s];
            getLogger().info("Testing [" + value + "]");
           
            start = System.currentTimeMillis();
            for(int i = 0; i < ITERATIONS; i++) { 
               NumberUtil.isDouble(value);
            }
            stop = System.currentTimeMillis();
            getLogger().info("NumberUtil.isDouble() took: " + (stop-start) + "ms");
            
            start = System.currentTimeMillis();
	        for(int i = 0; i < ITERATIONS; i++) { 
	            doublePattern.matcher(value).matches();
	        }
            stop = System.currentTimeMillis();
            getLogger().info("Regular expression took:  " + (stop-start) + "ms");
	
            start = System.currentTimeMillis();
	        for(int i = 0; i < ITERATIONS; i++) { 
	            isDouble(value);
	        }
            stop = System.currentTimeMillis();
            getLogger().info("Double.valueOf() took:      " + (stop-start) + "ms");

            getLogger().info("regular expression matching: " + doublePattern.matcher(value).matches());
            getLogger().info("NumberUtil.isdouble:           " + NumberUtil.isDouble(value));
            getLogger().info("isDouble using Double.valueOf(): " + isDouble(value));
        }
    }

    public static boolean isDouble(String string) {
        try { 
            Double.valueOf(string);
            return true;
        } catch (NumberFormatException e) { 
            return false;
        }
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(NumberUtilSuite.class);
    }
}
