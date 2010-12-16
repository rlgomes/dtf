package com.yahoo.dtf.query;

import java.util.Enumeration;
import java.util.Hashtable;

import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.NodeShutdownHook;
import com.yahoo.dtf.query.Cursor;
import com.yahoo.dtf.query.Cursors;
import com.yahoo.dtf.exception.QueryException;
import com.yahoo.dtf.logger.DTFLogger;


public class Cursors implements NodeShutdownHook {
    
    private DTFLogger _logger = DTFLogger.getLogger(Cursors.class);

    private Hashtable _cursors = null;
  
    /**
     * 
     * @param recorder RecorderIntf to use.
     * @param event set to null if you don't want to filter on events.
     */
    public Cursors() {
        _cursors = new Hashtable();
        DTFNode.registerShutdownHook(this);
    }
   
    public void addCursor(String name, Cursor cursor) { 
        _cursors.put(name, cursor);
    }
   
    public Cursor getCursor(String name) { 
        return (Cursor)_cursors.get(name);
    }
    
    public void close() { 
        Enumeration keys = _cursors.keys();
        
        while (keys.hasMoreElements())  {
            String key = (String)keys.nextElement();
            Cursor cursor = (Cursor)_cursors.get(key);
            try {
                cursor.close();
            } catch (QueryException e) {
                _logger.warn("Error closing query.", e);
            }
        }
    }
    
    public void shutdown() { close(); }
}
