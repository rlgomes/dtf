package com.yahoo.dtf.actions.plugin;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.plugin.util.PluginCLI;
import com.yahoo.dtf.plugin.util.PluginCLIPool;

public class Cli_setoptions extends Action {

    private static PluginCLIPool pool = PluginCLIPool.getInstance();
    
    private String returnevents = null;

    public void execute() throws DTFException {
	    PluginCLI cli = pool.checkOut();
	    try { 
	        if ( isReturnevents() ) { 
	            cli.returnEventsOn();
	        } else { 
	            cli.returnEventsOff();
	        }
	    } finally {
	        pool.checkIn(cli);
	    }
	}
	
	public static PluginCLIPool getPool() { return pool; } 

	public boolean isReturnevents() throws ParseException { return toBoolean("returnevents",returnevents); }
	
	public String getReturnevents() throws ParseException { return replaceProperties(returnevents); }
    public void setReturnevents(String returnevents) { this.returnevents = returnevents; }
}
