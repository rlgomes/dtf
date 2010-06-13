package com.yahoo.dtf.components;

import java.util.HashMap;

import com.yahoo.dtf.DTFConstants;
import com.yahoo.dtf.actions.protocol.Lock;
import com.yahoo.dtf.exception.DTFException;

public class Components {

    public HashMap<String, Lock> _elems = new HashMap<String, Lock>();
   
    public Components() { }
    
    public void registerComponent(String key, Lock lock) { _elems.put(key, lock); }
    public void unregisterComponent(String key) { _elems.remove(key); }
    
    public Lock getComponent(String key) throws DTFException {
        Object obj = _elems.get(key);
        
        if (obj == null) {
           
            if (key.equals(DTFConstants.DTFC_COMPONENT_ID)) 
                return new Lock("dtfc",null,0);
            
            throw new DTFException("Component with id: " + key + " not registered.");
        }
        
        return (Lock)obj;
    }
    
    public boolean hasComponents() { return _elems.size() != 0; } 
}
