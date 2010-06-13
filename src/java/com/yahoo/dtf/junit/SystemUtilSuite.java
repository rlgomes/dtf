package com.yahoo.dtf.junit;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.util.SystemUtil;

/**
 * SystemUtil JUnit test, just to validate basic functionality.
 * 
 * @author rlgomes
 */
public class SystemUtilSuite extends DTFJUnitTest {
   
    @Test(timeout=600000)
    public void deleteEmptyDirectory() throws DTFException, ParseException { 
      
        File dir = new File("test.directory.to.be.deleted");
        if ( !dir.mkdirs() ) 
            throw new DTFException("Unable to create directory [" + dir + "]");
        
        try {
            SystemUtil.deleteDirectory(dir);
        } catch (IOException e) {
            throw new DTFException("Error deleting directory.",e);
        }
    }

    @Test(timeout=600000)
    public void deleteNonEmptyDirectory() throws DTFException, ParseException { 
        File dir = new File("test.directory.to.be.deleted");
        if ( !dir.mkdirs() ) { 
            throw new DTFException("Unable to create directory [" + dir + "]");
        }
        
        File subdir = new File(dir,"subdir");
        if ( !subdir.mkdirs() ) 
            throw new DTFException("Unable to create directory [" + subdir + "]");
        
        try {
            File.createTempFile("test", "test",subdir);
        } catch (IOException e) {
            throw new DTFException("Error creating temporary file.",e);
        }
        
        try {
            SystemUtil.deleteDirectory(dir);
        } catch (IOException e) {
            throw new DTFException("Error deleting directory.",e);
        }
    }
   
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(SystemUtilSuite.class);
    }
}
