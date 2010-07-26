package com.yahoo.dtf.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import com.yahoo.dtf.components.PropertyState;
import com.yahoo.dtf.config.Config;
import com.yahoo.dtf.config.DynamicProperty;
import com.yahoo.dtf.DTFProperties;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.exception.PropertyException;
import com.yahoo.dtf.logger.DTFLogger;

/**
 * 
 * @author Rodney Gomes
 */
public final class Config implements Cloneable {
    
    private static DTFLogger _log = DTFLogger.getLogger(Config.class);
    
    // Static Properties
    private Properties _properties = null;
   
    private static HashMap<String, DynamicProperty> _dynamic = 
                                         new HashMap<String, DynamicProperty>();
    
    public Config() throws DTFException { 
        this(true);
    }
    
    private Config(boolean init) throws DTFException {
        if (init) init();
    }
    
    public static void registerDynamicProperty(String key, DynamicProperty dyn) throws DTFException {
        if ( _dynamic.containsKey(key) )
            throw new DTFException("Dynamic property with the name [" + key + 
                                   "] already exists");
        
        if ( _log.isDebugEnabled() ) 
            _log.debug("Registering dynamic property [" + key + "]");
            
        _dynamic.put(key,dyn);
        PropertyState.addPropertiesToIgnore(key);
    }
    
    public static Set<String> getDynamicProperties() { 
        return _dynamic.keySet();
    }
    
    private void init() throws DTFException {
        _properties = new Properties();
        
        java.util.Properties aux = (java.util.Properties) 
                                                 System.getProperties();
        
        for (Entry<Object, Object> entry : aux.entrySet())
            _properties.put(entry.getKey(), entry.getValue());
       
        String home = getProperty(DTFProperties.DTF_HOME); 
        File config = new File(home + File.separatorChar + "dtf.properties");
        if (config.exists()) {
            FileInputStream fis;
            try {
                fis = new FileInputStream(config);
                loadProperties(fis,false);
            } catch (FileNotFoundException e) {
                throw new DTFException("Bad config.",e);
            }
        }
        
        initDTFProperties();
    }
    
    public void initDTFProperties() throws ParseException { 
        /*
         * Dynamic properties that are used internally in the DTF framework.
         */ 
        Iterator<String> iterator = _dynamic.keySet().iterator();
        while ( iterator.hasNext() ) { 
            String key = iterator.next();
            put(key,_dynamic.get(key));
        }

        // add default properties
        if (getProperty(DTFProperties.DTF_XML_FILENAME) != null) { 
	        String pathToXML = new File(getProperty(DTFProperties.DTF_XML_FILENAME)).getParent();
	        if (pathToXML == null)
	            pathToXML = "";
	        _properties.put(DTFProperties.DTF_XML_PATH, pathToXML);
        }
    }
    
    public void put(String key, DynamicProperty property) { 
        _properties.put(key, property);
    }

    public String callDynamicProperty(String key, String args) throws ParseException {
        Object result = _properties.get(key);
        
        if (result instanceof DynamicProperty) {
            return ((DynamicProperty)result).getValue(args);
        } else {
            throw new ParseException("No dynamic property named [" + key + "]");
        }
    }

    public <T extends Object> boolean isType(String key, Class<T> c) { 
        return c.isInstance(_properties.get(key));
    }
   
    public boolean isStream(String key) { 
        return _properties.get(key) instanceof DTFStream;
    }
    
    public <T> T getPropertyAs(String key) throws ParseException { 
        try { 
            return (T) _properties.get(key);
        } catch (ClassCastException e) { 
            // no easy way to do instanceOf on a generic type, since it only 
            // exists at runtime but we can catch the ClassCastException and 
            // give back a better error message.
            throw new ParseException("Property [" + key + 
                                     "] not of the specified type.",e);
        }
    }

    public DTFStream getPropertyAsStream(String key) { 
        return (DTFStream) _properties.get(key);
    }
    
    public String getProperty(String key) throws ParseException {
        Object result = _properties.get(key);
        
        if (result instanceof DynamicProperty) {
            /*
             * Normal dynamic property without arguments
             */
            return ((DynamicProperty)result).getValue(null);
        } else {
            return (String) result;
        }
    }

    public String getProperty(String key, String defaultValue) throws ParseException {
        String result = getProperty(key);
        if (result == null) 
            return defaultValue;
        else 
            return result;
    }

    public int getPropertyAsInt(String key, int defaultValue) throws PropertyException, ParseException {
        String result = getProperty(key);
       
        try { 
            if (result == null) 
                return defaultValue;
            else
                return new Integer(result).intValue(); 
        } catch (NumberFormatException e) { 
            throw new PropertyException("Invalid int value for property '" + 
                                        key + "'",e);
        }
    }
   
    public Boolean getPropertyAsBoolean(String key, 
                                        Boolean defaultValue) throws ParseException  {
        String result = getProperty(key);

        if (result == null) 
            return defaultValue;
        else
            return Boolean.valueOf(result);
    }
   
    public int getPropertyAsInt(String key) throws PropertyException, ParseException {
        String result = getProperty(key);
       
        try { 
            if (result == null) 
                return -1;
            else
                return new Integer(result).intValue(); 
        } catch (NumberFormatException e) { 
            throw new PropertyException("Invalid int value for property '" + 
                                        key + "'",e);
        }
    }
    
    public synchronized boolean contains(Object value) {
        return _properties.contains(value);
    }
    
    public synchronized boolean containsKey(Object key) {
        return _properties.containsKey(key);
    }

    public void loadProperties(InputStream is, boolean overwrite) throws DTFException {
       loadProperties(is,overwrite,null);
    }
   
    public void loadProperties(InputStream is,
                               boolean overwrite,
                               String encoding) throws DTFException {
        try {
            // DTF default file encoding
            if ( encoding == null ) 
                encoding = "UTF-8";
            
            java.util.Properties properties = new java.util.Properties();
            properties.load(is);
            Enumeration keys = properties.keys();
            while (keys.hasMoreElements()) { 
                Object key = keys.nextElement();
                Object value = properties.get(key);
                if ( overwrite || !containsKey(key)) {
                    if ( encoding != null ) {
                        key = new String(((String)key).
                                               getBytes(encoding),encoding);
                        value = new String(((String)value).
                                               getBytes(encoding),encoding);
                    }
                    _properties.put(key, value);
                }
            }
        } catch (IOException e) {
            throw new DTFException("Unable to load properties from specified stream.",e);
        }
    }
    
    public Properties getProperties() { return _properties; }
    
    public Object setProperty(String key, String value) {
        return _properties.put(key, value);
    }

    public Object setProperty(String key, long value) {
        return _properties.put(key, "" + value);
    }

    public Object setProperty(String key, String value, boolean overwrite) {
        if ( overwrite ) {
           return _properties.put(key, value);
        } else if (!containsKey(key)) {
           return _properties.put(key, value);
        }
        
        return _properties.get(key);
    }
    
    public Object clone() {
        if ( _log.isDebugEnabled() )
            _log.debug("cloning " + _properties.size() + " properties.");

        Config config;
        try {
            config = new Config(false);
        } catch (DTFException e) {
            throw new RuntimeException("Unable to clone.",e);
        }
        config._properties = (Properties)this._properties.clone();
        return config;
    }
    
    public synchronized Object remove(Object key) {
        return _properties.remove(key);
    }
}
