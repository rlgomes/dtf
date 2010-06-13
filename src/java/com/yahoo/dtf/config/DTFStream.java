package com.yahoo.dtf.config;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.yahoo.dtf.config.DynamicProperty;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.streaming.DTFInputStream;

public class DTFStream implements DynamicProperty {
    
    public static final String DTF_STREAM = "dtf.stream";
   
    private static HashMap<String, Class> _streams = 
                                   new HashMap<String, Class>();

    public static <T extends DTFInputStream> void registerStream(String name, 
                                                                 Class<T> stream) 
                  throws DTFException { 
        if ( _streams.containsKey(name) ) {
            throw new DTFException("Stream handler already exists with name [" 
                                   + name + "]");
        }
        _streams.put(name, stream);
    }
    
    public static ArrayList<String> getStreamNames() { 
        ArrayList<String> names = new ArrayList<String>();
        Iterator<String> iter = _streams.keySet().iterator();
        
        while ( iter.hasNext() ) { 
            names.add(iter.next());
        }
        return names;
    }
    
    public DTFInputStream getValueAsStream(String args) throws ParseException {
        String[] parts = args.split(",");
        
        if ( args.length() < 1 ) 
            throw new ParseException("Expecting more args for dtf.stream, got [" 
                                     + args + "]");
            
        String name = parts[0];
        String arg1 = null;
        String arg2 = null;
        
        if ( args.length() >= 2 ) 
            arg1 = parts[1];

        if ( args.length() >= 3 ) 
            arg2 = parts[2];

        long size = 1024;

        if ( arg1 != null )  {
            try {
                size = new Long(arg1);
            } catch (NumberFormatException e) { 
                throw new ParseException("Size should be a long not [" + 
                                         arg1 + "]");
            }
        }
        Class<DTFInputStream> streamclass = _streams.get(name);
        
        if ( streamclass == null )
            throw new ParseException("Unsupported stream [" + name + "]");
       
        try { 
	        Constructor<DTFInputStream> c = 
	           streamclass.getConstructor(new Class[]{long.class,String.class});
	        DTFInputStream stream = c.newInstance(new Object[]{size, arg2});
	        return stream;
        } catch(NoSuchMethodException e ) {
            throw new ParseException("Error instantiating DTFInputStream.",e);
        } catch (IllegalArgumentException e) {
            throw new ParseException("Error instantiating DTFInputStream.",e);
        } catch (InstantiationException e) {
            throw new ParseException("Error instantiating DTFInputStream.",e);
        } catch (IllegalAccessException e) {
            throw new ParseException("Error instantiating DTFInputStream.",e);
        } catch (InvocationTargetException e) {
            throw new ParseException("Error instantiating DTFInputStream.",e);
        }
    }
    
    public String getValue(String args) throws ParseException { 
        return getValueAsStream(args).getAsString();
    }
}
