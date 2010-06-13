package com.yahoo.dtf.plugin.util;

import java.util.ArrayList;

import com.yahoo.dtf.exception.DTFException;

public class PluginCLIPool {
    
	private static PluginCLIPool _instance = null;

	ArrayList<PluginCLI> _pool;
	
	private PluginCLIPool() { 
		_pool = new ArrayList<PluginCLI>();
	}
	
	public static synchronized PluginCLIPool getInstance() { 
	    if ( _instance == null ) { 
	        _instance = new PluginCLIPool();
	    }
	    
	    return _instance;
	}
	
	public synchronized void checkIn(PluginCLI cli) { 
	    _pool.add(cli);
	}
	
	public synchronized PluginCLI checkOut() throws DTFException { 
	    if ( _pool.size() != 0 )
	        return _pool.remove(0);
	
		return new PluginCLI();
	}
}
