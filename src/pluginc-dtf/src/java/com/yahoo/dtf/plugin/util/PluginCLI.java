package com.yahoo.dtf.plugin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.exception.PluginException;
import com.yahoo.dtf.recorder.Event;
import com.yahoo.dtf.util.CLIUtil;

public class PluginCLI {
    
    private static String TEST_CLI_NAME = "./plugin_cli";
    private static String CLI_OUTPUT_DIR = "cli_output";

    private Process _process = null;
    private OutputStream _os = null;
    private BufferedReader _is = null;
    
    private String eventFile = null;
    
    private boolean returnevents = false;
    
    public PluginCLI() throws PluginException, ParseException {
        Runtime rt = Runtime.getRuntime();
        
        char sep = File.separatorChar;
        String path = CLI_OUTPUT_DIR;
        File dir = new File(path);
        
        if (!dir.exists())
            dir.mkdirs();
          
        String command = null;
        try {
            eventFile = path + sep + "cli.out";
            String errorFile = path + sep + "cli.err";
    
            command = TEST_CLI_NAME + " " + "-dtf  -o " + eventFile + 
                                                 " -e " + errorFile;
                      
            Action.getLogger().info("Executing ["+ command +"]");
            _process = rt.exec(command);
        } catch (IOException e) {
            throw new PluginException("Unable to start ymb_cli with command [" + 
                                      command + "]",e);
        }
        
        _os = _process.getOutputStream();
        _is = new BufferedReader(new InputStreamReader(_process.getInputStream()));
    }
    
    public void test(String arg1, String arg2) throws PluginException {
        pushCommand("test " + arg1 + " " + arg2);
    }

    public void gettimeofday() throws PluginException {
        pushCommand("gettimeofday");
    }

    public void returnEventsOn() throws PluginException {
        pushCommand("return_events_on");
        returnevents = true;
    }

    public void returnEventsOff() throws PluginException {
        returnevents = false;
        pushCommand("return_events_off");
    }

    public void quit() throws PluginException {
        pushCommand("quit");
    }
    
    public String getEventFile() { return eventFile; }
    
	private void pushCommand(String command) throws PluginException { 
		try {
			_os.write((command + "\n").getBytes());
			_os.flush();
		
			// if we're in the mode to read returned events then make sure to 
			// read them because otherwise the secondary CLI process will get 
			// stuck trying to write to the STDOUT, since no one is reading.
			if ( returnevents ) { 
			    Event event = CLIUtil.readEvent(_is,
			                                    command,
			                                    "PluginCLI",
			                                    false);
			    Action.getRecorder().record(event);
			}
		} catch (IOException e) {
			throw new PluginException("Unable to send command [" + 
			                          command + "]",e);
		} catch (DTFException e) {
			throw new PluginException("Unable to send command [" + 
			                          command + "]",e);
        }
	}
}
