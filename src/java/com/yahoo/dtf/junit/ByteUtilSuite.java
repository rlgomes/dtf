package com.yahoo.dtf.junit;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.util.ByteUtil;

/**
 * ByteUtil JUnit test, just to validate basic functionality.
 *
 * @author rlgomes
 */
public class ByteUtilSuite extends DTFJUnitTest {
   
    @Test(timeout=600000)
    public void simpleParse() throws DTFException { 
        long bytes = 0;
        
        bytes = ByteUtil.parseBytes("test", "2Mb");
        assert (bytes == (2*1024*1024L)/8);
        
        bytes = ByteUtil.parseBytes("test", "2GB");
        assert (bytes == 2*1024*1024*1024L);
    }
   
    @Test(timeout=600000)
    public void equalityTest() throws DTFException {
        
        long l1 = ByteUtil.parseBytes("test", "134217728");
        long l2 = ByteUtil.parseBytes("test", "1073741824b");
        long l3 = ByteUtil.parseBytes("test", "1048576Kb");
        long l4 = ByteUtil.parseBytes("test", "1024Mb");
        long l5 = ByteUtil.parseBytes("test", "1Gb");
        
        long[] lbytes = new long[]{ l1, l2, l3, l4, l5 };

		for (int i = 0; i < lbytes.length-1; i++) { 
			assert (lbytes[i] == lbytes[i+1]) : 
			  "bytes are different for " + i + " and " + (i+1);
		}

		l1 = ByteUtil.parseBytes("test", "1073741824");
		l2 = ByteUtil.parseBytes("test", "1073741824B");
		l3 = ByteUtil.parseBytes("test", "1048576KB");
        l4 = ByteUtil.parseBytes("test", "1024MB");
        l5 = ByteUtil.parseBytes("test", "1GB");
        
        lbytes = new long[]{ l1,l2, l3, l4, l5 };

		for (int i = 0; i < lbytes.length-1; i++) { 
		assert (lbytes[i] == lbytes[i+1]) : 
		  "bytes are different for " + i + " and " + (i+1);
		}
        
    }
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ByteUtilSuite.class);
    }
}
