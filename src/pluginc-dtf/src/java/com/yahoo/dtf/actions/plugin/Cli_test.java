package com.yahoo.dtf.actions.plugin;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.plugin.util.PluginCLI;
import com.yahoo.dtf.plugin.util.PluginCLIPool;

/**
 * Example test action that interacts directly with the C CLI that is is also 
 * available as an example of using DTF to test APIs that are not written in 
 * Java.
 *
 * @author rlgomes
 *
 */
public class Cli_test extends Action {

    private static PluginCLIPool pool = PluginCLIPool.getInstance();
    
    private String arg1 = null;
    
    private String arg2 = null;
    
	public void execute() throws DTFException {
	    PluginCLI cli = pool.checkOut();
	    try { 
	        cli.test(getArg1(), getArg2());
	    } finally {
	        pool.checkIn(cli);
	    }
	}
	
	public static PluginCLIPool getPool() { return pool; } 

    public String getArg1() throws ParseException { return replaceProperties(arg1); }
    public void setArg1(String arg1) { this.arg1 = arg1; }

    public String getArg2() throws ParseException { return replaceProperties(arg2); }
    public void setArg2(String arg2) { this.arg2 = arg2; }
}
