package com.yahoo.dtf.junit;

import java.text.ParseException;
import java.util.Date;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.util.TimeUtil;

/**
 * TimeUtil JUnit test, just to validate basic functionality.
 *
 * @author rlgomes
 */
public class TimeUtilSuite extends DTFJUnitTest {
   
    @Test(timeout=600000)
    public void simpleParse() throws DTFException, ParseException { 
        TimeUtil.dateStampToDate("2009-01-30-00.00.00.000");
       
        long timestamp = TimeUtil.getCurrentTime();
        String datestamp = TimeUtil.dateStampToDateStamp(timestamp);
        Date date = TimeUtil.dateStampToDate(datestamp);
        
        assert (date.getTime() == timestamp) : "Timestamp parsing must have mangled the date.";
        
        TimeUtil.getDateStamp();
        TimeUtil.getTimeStamp();
    }
   
    @Test(timeout=600000)
    public void equalityTest() throws DTFException, ParseException {
        long l1 = TimeUtil.parseTime("testprop", "86400000");
        long l2 = TimeUtil.parseTime("testprop", "86400000ms");
        long l3 = TimeUtil.parseTime("testprop", "86400s");
        long l4 = TimeUtil.parseTime("testprop", "1440m");
        long l5 = TimeUtil.parseTime("testprop", "24h");
        long l6 = TimeUtil.parseTime("testprop", "1d");
        
        long[] ltimestamps = new long[]{l1,
                                        l2,
                                        l3,
                                        l4,
                                        l5,
                                        l6
                                       };
       
        for (int i = 0; i < ltimestamps.length-1; i++) { 
            assert (ltimestamps[i] == ltimestamps[i+1]) : 
                          "long timestamps are different for " + i + " and " + 
                          (i+1) + " got " + ltimestamps[i] + " and " + 
                          ltimestamps[i+1];
        }
        
        int i1 = TimeUtil.parseTimeToInt("testprop", "86400000");
        int i2 = TimeUtil.parseTimeToInt("testprop", "86400000ms");
        int i3 = TimeUtil.parseTimeToInt("testprop", "86400s");
        int i4 = TimeUtil.parseTimeToInt("testprop", "1440m");
        int i5 = TimeUtil.parseTimeToInt("testprop", "24h");
        int i6 = TimeUtil.parseTimeToInt("testprop", "1d");
        
        int[] itimestamps = new int[]{i1,
                                      i2,
                                      i3,
                                      i4,
                                      i5,
                                      i6
                                     };
       
        for (int i = 0; i < itimestamps.length-1; i++) { 
            assert (itimestamps[i] == itimestamps[i+1]) : 
                          "int timestamps are different for " + i + " and " + 
                          (i+1) + " got " + itimestamps[i] + " and " + 
                          itimestamps[i+1];
        }
    }
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TimeUtilSuite.class);
    }
}
