package com.yahoo.dtf.recorder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.yahoo.dtf.recorder.RecorderBase;
import com.yahoo.dtf.recorder.RecorderFactory;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.RecorderException;
import com.yahoo.dtf.logger.DTFLogger;
import com.yahoo.dtf.util.SystemUtil;


public class RecorderFactory {
    private static DTFLogger _logger = DTFLogger.getLogger(RecorderFactory.class);
    
    private static HashMap<String, Class> _recorders = 
                                                   new HashMap<String, Class>();

    private static HashMap<String, Boolean> _usesURI = 
                                                   new HashMap<String, Boolean>();
    
    public static RecorderBase getRecorder(String type, 
                                           URI uri, 
                                           boolean append,
                                           String encoding) throws DTFException { 
        Class recorderClass = _recorders.get(type);
       
        if (recorderClass == null)
            throw new RecorderException("Unsupported recorder type [" + 
                                        type + "]");
        
        Class[] parameters = new Class[] {URI.class, boolean.class, String.class};
        Object[] args = new Object[] {uri, Boolean.valueOf(append), encoding};

        if ( !append && _usesURI.get(type) ) {
            String path = Action.getStorageFactory().getPath(uri);
            File dir = new File(path);
            if (dir.exists()) { 
                try {
                    SystemUtil.deleteDirectory(dir);
                } catch (IOException e) {
                    throw new DTFException("Error deleting output file.",e);
                }
                
                if ( _logger.isDebugEnabled() ) {
                    _logger.debug("Wiped [" + path + "]");
                }
            }
        }
        
        try {
            return (RecorderBase)
                     recorderClass.getConstructor(parameters).newInstance(args);
        } catch (IllegalArgumentException e) {
            throw new RecorderException("Unable to instantiate recoder [" + type + "].",e);
        } catch (SecurityException e) {
            throw new RecorderException("Unable to instantiate recoder [" + type + "].",e);
        } catch (InstantiationException e) {
            throw new RecorderException("Unable to instantiate recoder [" + type + "].",e);
        } catch (IllegalAccessException e) {
            throw new RecorderException("Unable to instantiate recoder [" + type + "].",e);
        } catch (InvocationTargetException e) {
            throw new RecorderException("Unable to instantiate recoder [" + type + "].",e);
        } catch (NoSuchMethodException e) {
            throw new RecorderException("Unable to instantiate recoder [" + type + "].",e);
        }
    }

    public static <T extends RecorderBase> 
           void registerRecorder(String name, Class<T> recorderClass) { 
        registerRecorder(name, recorderClass, true);
    }
    
    public static <T extends RecorderBase> 
           void registerRecorder(String name,
                                 Class<T> recorderClass,
                                 boolean usesURI) { 
        if (_recorders.containsKey(name)) 
            _logger.warn("Overwriting recorder implementation for [" + name + "]");
        
        if (_logger.isDebugEnabled())
            _logger.debug("Registering recorder [" + name + "]");
        
        _usesURI.put(name, usesURI);
        _recorders.put(name, recorderClass);
    }
    
    public static ArrayList<String> getRecorderNames() { 
        ArrayList<String> names = new ArrayList<String>();
        Iterator<String> iter = _recorders.keySet().iterator();
        
        while (iter.hasNext()) 
            names.add(iter.next());
        
        return names;
    }
}
