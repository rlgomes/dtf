package com.yahoo.dtf.recorder;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.yahoo.dtf.DTFConstants;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.RecorderException;
import com.yahoo.dtf.exception.StorageException;
import com.yahoo.dtf.stats.GenCalcStats;

public class StatsRecorder extends RecorderBase {

    private HashMap<String, GenCalcStats> stats = null;
    
    public StatsRecorder(URI uri,
                        boolean append,
                        String encoding) throws StorageException { 
        super(uri, append, encoding);
        stats = new HashMap<String, GenCalcStats>();
    }

    public StatsRecorder(URI uri,
                           boolean append) throws StorageException { 
        this(uri, append, DTFConstants.DEFAULT_ENCODING);
    }
    
    public void record(Event event) throws RecorderException {
        try {
            String name= event.getName();
            GenCalcStats stat = null;
            
            synchronized (stats) { 
	            stat = stats.get(name);
	            if ( stat == null ) {
	                stat = new GenCalcStats(null);
	                stats.put(name, stat);
	            }
            }
            
            stat.updateStats(event);
        } catch (DTFException e) {
            throw new RecorderException("Error updating statistic.",e);
        }
    }

    public void stop() throws RecorderException {
        Iterator<Entry<String,GenCalcStats>> iter = stats.entrySet().iterator();
        
        while ( iter.hasNext() ) {
            Entry<String,GenCalcStats> stat = iter.next();
	        LinkedHashMap<String, String> props = stat.getValue().getCurrentStats();
	        Event event = new Event(stat.getKey());
	        
	        Iterator<Entry<String,String>> attributes = props.entrySet().iterator();
			while ( attributes.hasNext() ) {
				Entry<String, String> entry = attributes.next();
				event.addAttribute(entry.getKey(), entry.getValue());
			}
	
			Action.getRecorder().record(event);
        }
    }

    public void start() throws RecorderException { }
}
