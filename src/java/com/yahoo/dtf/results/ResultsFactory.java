package com.yahoo.dtf.results;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.HashMap;

import com.yahoo.dtf.recorder.RecorderBase;
import com.yahoo.dtf.recorder.RecorderFactory;
import com.yahoo.dtf.util.SystemUtil;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.RecorderException;
import com.yahoo.dtf.logger.DTFLogger;

public class ResultsFactory {
    
    private static DTFLogger _logger = DTFLogger.getLogger(RecorderFactory.class);
    
    private static HashMap<String, Class> _results = new HashMap<String, Class>();
    
    public static ResultsBase getRecorder(String type, URI uri) throws DTFException { 
        Class resultsClass = _results.get(type);
       
        if (resultsClass == null)
            throw new RecorderException("Unsupported results type [" + type + "]");
        
        Class[] parameters = new Class[] {URI.class, boolean.class};
        Object[] args = new Object[] {uri, true};

        try {
            return (ResultsBase) resultsClass.getConstructor(parameters).newInstance(args);
        } catch (IllegalArgumentException e) {
            throw new RecorderException("Unable to instantiate results [" + type + "].",e);
        } catch (SecurityException e) {
            throw new RecorderException("Unable to instantiate results [" + type + "].",e);
        } catch (InstantiationException e) {
            throw new RecorderException("Unable to instantiate results [" + type + "].",e);
        } catch (IllegalAccessException e) {
            throw new RecorderException("Unable to instantiate results [" + type + "].",e);
        } catch (InvocationTargetException e) {
            throw new RecorderException("Unable to instantiate results [" + type + "].",e);
        } catch (NoSuchMethodException e) {
            throw new RecorderException("Unable to instantiate results [" + type + "].",e);
        }
    }
    
    public static <T extends ResultsBase> 
           void registerResults(String name, Class<T> resultsClass) 
           throws DTFException { 
        
        if (_results.containsKey(name)) 
            _logger.warn("Overwriting results implementation for [" + name + "]");
        
        if (_logger.isDebugEnabled())
            _logger.debug("Registering results [" + name + "]");
        
        _results.put(name, resultsClass);
    }
}
