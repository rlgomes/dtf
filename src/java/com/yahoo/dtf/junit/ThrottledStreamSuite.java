package com.yahoo.dtf.junit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.util.Counter;
import com.yahoo.dtf.util.streams.ThrottledOutputStream;

/**
 * 
 * @author rlgomes
 */
public class ThrottledStreamSuite extends DTFJUnitTest {
   
    @Test(timeout=600000)
    public void bufferedRead() throws DTFException {
        byte[] data = new byte[10*1024];
        
        int dbps = 2*1024;
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ThrottledOutputStream tos = new ThrottledOutputStream(baos,dbps);
        try { 
            Counter cnt = new Counter();
            cnt.start();
            byte[] buffer = new byte[1024];
	        int read = 0;
	        while ( (read = bais.read(buffer)) != -1) { 
	            tos.write(buffer,0,read);
	        }
	        cnt.stop();
	        double duration = cnt.getDurationInSeconds();
	        double bps = data.length / duration;
	        double diff = (dbps > bps ? dbps-bps : bps-dbps);
	        int offby = (int) (100.0*(diff/dbps));
	        getLogger().info("bps: " + bps + " off by " + offby + "%");
        } catch (IOException e) { 
            throw new DTFException("Error using ThrottledOutputStream.",e);
        }
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ThrottledStreamSuite.class);
    }
}
