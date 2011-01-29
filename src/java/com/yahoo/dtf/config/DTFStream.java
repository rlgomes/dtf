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
import com.yahoo.dtf.streaming.StringInputStream;

/**
 * 
 * @dtf.feature Streaming Properties
 * @dtf.feature.group DTF Properties
 * 
 * @dtf.feature.desc
 * <p>
 * This feature is extremely useful when you need to generate large amounts of
 * data but you can't afford to have it all stored in memory at one time during
 * execution because it would break your application. Referencing a streaming
 * property is as easy as referencing any other property and can be done so, 
 * like so:
 * </p> 
 * <pre>${dtf.stream([random|repeat|etc],[size],[additional arguments separated by commas])}</pre>
 * <p>
 * Internally these properties are not resolved into the data that they 
 * represent but instead into an InputStream that can be read and immediately 
 * sent to whatever OutputStream is awaiting data. To the test writer they're 
 * used the same way any other property would be used but they allow the test
 * writer to generate hundreds of streams sending GB objects at the same time
 * while never hold more than a few MB (total) in memory. The main difference 
 * when using these properties is in the way the property is handled within 
 * the tag code where the developer can pick up an InputStream to read the data 
 * in a way more efficient manner than being give a huge chunk of data in a 
 * String.
 * </p>
 *
 * The Streaming properties are broken down into the following 2 types:
 * 
 * <h2>Unstructured Stream Properties</h2>
 * <p>
 * There are a few unstructured stream types types already built into DTF which
 * allow you to generate random and repeated data patterns, these include:
 * </p>
 * <ul>
 *      <li>{@dtf.link Random Stream Type}</li>
 *      <li>{@dtf.link Repeat Stream Type}</li>
 * </ul> 
 *
 * <h2>Structured Stream Properties</h2>
 * <p>
 * There are also structured data streaming types which generate data that are 
 * structured documents such as XML or JSON. The currently available structured
 * stream types are:
 * </p>
 * <ul>
 *      <li>{@dtf.link XML Stream Type}</li>
 * </ul>
 * 
 * <h2>Using DTF Streams in your Code</h2>
 * Now to take advantage of streaming properties as the developer of a tag you 
 * need to use a slightly different method when retrieving property data. This
 * means that instead of using <b>replaceProperties()</b> function you will have
 * to use the <b>replacePropertiesAsInputStream()</b> which if the underlying
 * property is a DTF streaming property it will return an InputStream that you 
 * can use to get your data without having to house it all in memory.
 */

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
    
    public static DTFInputStream getStringAsStream(String data) 
           throws ParseException { 
        DTFInputStream wrapper = new DTFInputStream(data.length(),new String[0]);
        wrapper.setInputStream(new StringInputStream(data)); 
        return wrapper;
    }
    
    public DTFInputStream getValueAsStream(String args) throws ParseException {
        String[] parts = args.split(",");
        
        if ( args.length() < 1 ) 
            throw new ParseException("Expecting more args for dtf.stream, got [" 
                                     + args + "]");
            
        String name = parts[0];

        String arg1 = null;
        String[] arguments = new String[parts.length-2];
        
        if ( arguments.length > 0 ) { 
            for (int i = 2; i < parts.length; i++) { 
                arguments[i-2] = parts[i];
            }
        }
       
        if ( args.length() >= 2 ) 
            arg1 = parts[1];

        long size = 0;
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
	           streamclass.getConstructor(new Class[]{long.class,String[].class});
	        DTFInputStream stream = c.newInstance(new Object[]{size, arguments});
	        DTFInputStream wrapper = new DTFInputStream(size,arguments);
	        wrapper.setInputStream(stream);
	        return wrapper;
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
