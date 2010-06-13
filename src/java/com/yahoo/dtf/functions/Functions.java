package com.yahoo.dtf.functions;

import java.util.Hashtable;
import com.yahoo.dtf.actions.function.Function;

public class Functions {

    private Hashtable<String, Function> _functions = null;
   
    public Functions() { 
        _functions = new Hashtable<String, Function>();
    }
    
    public void addFunction(String name, Function function) { 
        _functions.put(name, function);
    }
    
    public Function getFunction(String name) { 
        return _functions.get(name);
    }
    
    public boolean hasFunction(String name) { 
        return _functions.containsKey(name);
    }
    
    public void clear() { 
        _functions.clear();
    }
}
