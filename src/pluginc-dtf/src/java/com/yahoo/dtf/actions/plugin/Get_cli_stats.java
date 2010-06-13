package com.yahoo.dtf.actions.plugin;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.file.Returnfile;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.plugin.util.PluginCLI;
import com.yahoo.dtf.plugin.util.PluginCLIPool;

public class Get_cli_stats extends Action {

    private String uri = null;
    
	public void execute() throws DTFException {
	    PluginCLIPool pool = Cli_test.getPool();
	    PluginCLI cli = pool.checkOut();
	    cli.quit();
        Returnfile.pushFileToRunner(getUri(), 
                                    0,
                                    cli.getEventFile(),
                                    false,
                                    false);
	}

    public String getUri() throws ParseException { return replaceProperties(uri); }
    public void setUri(String uri) { this.uri = uri; }
}
