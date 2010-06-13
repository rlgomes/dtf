package com.yahoo.dtf.util;

import java.io.IOException;
import java.util.Vector;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.recorder.Event;

public class YinstUtil {
	public static final String SET_OPTION          = "set";

	public static final String START_OPTION        = "start";
	public static final String STOP_OPTION         = "stop";
	public static final String RESTART_OPTION      = "restart";
	
	public static final String LS_OPTION           = "ls";

	public static final String INSTALL_OPTION      = "install";
	public static final String REMOVE_OPTION       = "remove";
	
	public static final String RELOAD_OPTION       = "reload";

	private String YINST_COMMAND = "yinst";
	
	private boolean _sudo = false;
	
	public YinstUtil() { 
	    this(true);
	}
	
	public YinstUtil(boolean sudo) { 
	    _sudo = sudo;
	}
	
	public Vector<String> set(String property, String value) throws DTFException {
		Vector<String> lines = null;

		String command = null;
		if (value == null || value.trim().equals("")) { 
			command = YINST_COMMAND + " " + SET_OPTION + " " + property;
		} else { 
			command = YINST_COMMAND + " " + SET_OPTION + " " + 
		                 property + "=" + value;
			Action.getLogger().info("Setting [" + property + "=" + value + "]");
		}
		
		try { 
		    if ( _sudo ) command = "sudo " + command;
		    
			Process p = Runtime.getRuntime().exec(command);
			lines = StreamUtil.readToVector(p.getInputStream());
			
			if ( p.waitFor() != 0 )  {
				String cause = StreamUtil.readToString(p.getErrorStream());
				throw new DTFException("Failed to execute [" + command + 
						               "] cause [" + cause + "]");
			}
		} catch (IOException e) { 
			throw new DTFException("Unable to execute [" + command + "]",e);
		} catch (InterruptedException e) {
			throw new DTFException("Unable to execute [" + command + "]",e);
		}
		
		return lines;
	}

	public void start(String pkgname) throws DTFException { 
		execute(YINST_COMMAND + " " + START_OPTION + " " + pkgname);
	}

	public void stop(String pkgname) throws DTFException { 
		execute(YINST_COMMAND + " " + STOP_OPTION + " " + pkgname);
	}

	public void restart(String pkgname) throws DTFException { 
		execute(YINST_COMMAND + " " + RESTART_OPTION + " " + pkgname);
	}

	public void ls(String pkgname) throws DTFException { 
		execute(YINST_COMMAND + " " + LS_OPTION + " " + pkgname);
	}

	public void install(String pkgname) throws DTFException { 
		execute(YINST_COMMAND + " " + INSTALL_OPTION + " " + pkgname);
	}

	public void remove(String pkgname) throws DTFException { 
		execute(YINST_COMMAND + " " + REMOVE_OPTION + " " + pkgname);
	}
	
	public void reload(String pkgname) throws DTFException { 
		execute(YINST_COMMAND + " " + RELOAD_OPTION + " " + pkgname);
	}
	
	private void execute(String command) throws DTFException { 
	    Command cmd = new Command(_sudo);
		try { 
		    Event event = cmd.execute("yinst", command);
		    Action.getRecorder().record(event);
		} finally { 
		    cmd.close();
		}
	}

}