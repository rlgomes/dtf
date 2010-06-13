package com.yahoo.dtf.junit;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Vector;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.util.StreamUtil;

/**
 * StreamUtil JUnit test, just to validate basic functionality.
 * 
 * @author rlgomes
 */
public class StreamUtilSuite extends DTFJUnitTest {
   
    @Test(timeout=600000)
    public void readStream() throws DTFException, ParseException { 
        String data = "Hello World\n";
        ByteArrayInputStream bais = new ByteArrayInputStream(data.getBytes());
        try {
            StreamUtil.consume(bais);
        } catch (IOException e) {
            throw new DTFException("Error consuming InputStream.",e);
        }

        bais.reset();
        try {
            String data2 = StreamUtil.readToString(bais);
            assert ( data.equals(data2) ) : "Corrupted data!";
        } catch (IOException e) {
            throw new DTFException("Error reading InputStream.",e);
        }

        bais.reset();
        try {
            Vector<String> datas = StreamUtil.readToVector(bais);
            assert ( data.equals(datas.get(0) + "\n") ) : "Corrupted data!";
        } catch (IOException e) {
            throw new DTFException("Error reading InputStream.",e);
        }
    }
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(StreamUtilSuite.class);
    }
}
