package com.yahoo.dtf.actions.plugin;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.plugin.util.PluginCLI;
import com.yahoo.dtf.plugin.util.PluginCLIPool;

public class Cli_gettimeofday extends Action {

    private static PluginCLIPool pool = PluginCLIPool.getInstance();

	public void execute() throws DTFException {
	    PluginCLI cli = pool.checkOut();
        try { 
            // to get the event back we've just made it so that the CLI will 
            cli.gettimeofday();
        } finally {
            pool.checkIn(cli);
        }
	}
}
