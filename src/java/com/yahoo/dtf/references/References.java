package com.yahoo.dtf.references;

import java.util.Enumeration;
import java.util.Hashtable;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.reference.Referencable;

public class References {

    private Hashtable<String, Referencable> _references = null;
   
    public References() { 
        _references = new Hashtable<String, Referencable>();
    }
    
    public void addReference(String id, Referencable action) { 
        _references.put(id, action);
    }
    
    public Action getReference(String id) { 
        return (Action)_references.get(id);
    }
    
    public boolean hasReference(String id) { 
        return _references.containsKey(id);
    }
    
    public Enumeration<Referencable> getAll() { 
        return _references.elements();
    }
    
    public void clear() { 
        _references.clear();
    }
}
