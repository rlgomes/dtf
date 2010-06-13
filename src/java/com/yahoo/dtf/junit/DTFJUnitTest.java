package com.yahoo.dtf.junit;

import junit.framework.JUnit4TestAdapter;

import org.junit.Ignore;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.config.Config;
import com.yahoo.dtf.logger.DTFLogger;

/**
 * Base for DTF JUnit tests that can easily be used to getLoggers and other 
 * required DTF functionality.
 * 
 * @author rlgomes
 *
 */
@Ignore
public class DTFJUnitTest {
   
    public static DTFLogger getLogger() { 
        return Action.getLogger();
    }
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DTFJUnitTest.class);
    }
    
    public static Config getConfig() { 
        return Action.getConfig();
    }
}