package com.yahoo.dtf.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.yahoo.dtf.util.PeekableIterator;

/**
 * To make property cloning more efficient between parent threads and children 
 * threads this class will use an array of previous copies to keep track of 
 * all of the properties ever created while making sure only to write a property
 * to the latest generations position in the array. Basically this is how that
 * will work:
 * 
 * Thread1: Properties:{[a=b,b=c]}
 * 
 * Thread 1 creates 2 underlying threads which now have the following:
 * Thread2: Properties:{[a=b,b=c],[]}
 * Thread3: Properties:{[a=b,b=c],[]}
 * 
 * When Thread2 writes to a property, z then it would do so in its last array
 * of Properties, like so:
 * 
 * Thread2: Properties:{[a=b,b=c],[z=d]}
 * 
 * With this the lookup for z would find it in the latest generation of 
 * properties while the lookup for b would have to go one generation back. 
 * 
 * With this approach the older generation of properties remain unaltered by 
 * secondary threads while allowing those secondary threads to see all of the 
 * properties created as intended.
 * 
 * @author rlgomes
 */
public class Properties extends java.util.Properties {

    private final static Object REMOVED = new Object();
    
    private TreeMap<Object, Object>[] props = null;
    
    // used to avoid the props[props.length-1] array lookup ;)
    private TreeMap<Object, Object> writeprops = null;

    public Properties() {
        props = new TreeMap[1];
        props[0] = new TreeMap<Object, Object>();
        writeprops = props[0];
    }

    public Properties(TreeMap<Object,Object>[] props) {
        this.props = props;
        this.writeprops = props[props.length-1];
    }
    
    @Override
    public synchronized int size() {
        int size = 0;
       
        for(int i = 0; i < props.length-1; i ++)
            size += props[i].size();
        
        return size;
    }
   
    @Override
    public synchronized Object put(Object key, Object value) {
        // all writes go to the latest generation
        return writeprops.put(key, value);
    }
    
    @Override
    public synchronized Object remove(Object key) {
        Object result = null;
        for (int i = props.length-1; i >= 0; i--) { 
            if ( (result = props[i].get(key)) != null )
                break;
        }
       
        // mark as REMOVED so we don't look it up in a previous generation
        writeprops.put(key, REMOVED);
        return result;
    }

    @Override
    public synchronized String getProperty(String key) {
        for (int i = props.length-1; i >= 0; i--) { 
            String result = props[i].get(key).toString();
            
            if ( result == REMOVED )
                return null;
            
            if ( result != null ) 
                return result;
        }
        
        return null;
    }

    @Override
    public synchronized Object get(Object key) {
        for (int i = props.length-1; i >= 0; i--) { 
            Object value = props[i].get(key);
            
            if ( value == REMOVED )
                return null;
            
            if ( value != null )
                return value;
        }
        
        return null;
    }
  
    @Override
    public synchronized Object clone() {
        TreeMap<Object,Object>[] props = new TreeMap[this.props.length+1];
       
        System.arraycopy(this.props, 0, props, 0, this.props.length);
        props[props.length-1] = new TreeMap<Object, Object>();
        
        Properties properties = new Properties(props);
        return properties;
    }

    @Override
    public synchronized boolean containsKey(Object key) {
        if ( writeprops.get(key) == REMOVED ) 
            return false;
        
        for (int i = props.length-1; i >= 0; i--) { 
            if ( props[i].containsKey(key) ) return true;
        }
        
        return false;
    }

    @Override
    public synchronized Enumeration<Object> keys() {
        final PeekableIterator<Object>[] fenums = 
                                             new PeekableIterator[props.length];
        
        for (int i = 0; i < props.length; i++) { 
            Iterator<Object> iter = props[i].keySet().iterator();
            fenums[i] = new PeekableIterator<Object>(iter);
        }
        
        return new Enumeration<Object>() {
            PeekableIterator<Object>[] enums = fenums;

            public boolean hasMoreElements() {
                for (int i = 0; i < enums.length; i++) { 
                    if ( enums[i].hasNext() ) return true;
                }
                return  false;
            }

            public Object nextElement() {
                String min = null;
                
                for (int i = 0; i < enums.length; i++) { 
                    if ( enums[i].hasNext() ) { 
	                    if ( min == null ) {
	                        min = enums[i].peek().toString();
	                    } else {
	                        String aux = enums[i].peek().toString();
	                        if ( aux.compareTo(min) < 0 )
	                            min = aux;
	                    }
                    } 
                }

                for (int i = 0; i < enums.length; i++) { 
                    if ( enums[i].hasNext() && enums[i].peek().equals(min) )
                        enums[i].remove();
                }
               
                return min;
            }
        };
    }
    
    /**
     * @deprecated this method is not allowed.
     */
    @Deprecated
    @Override
    public synchronized Enumeration<Object> elements() {
        throw new RuntimeException("Method not allowed.");
    }

    /**
     * @deprecated this method is not allowed.
     */
    @Deprecated
    @Override
    public Set<java.util.Map.Entry<Object, Object>> entrySet() {
        throw new RuntimeException("Method not allowed.");
    }
    
    /**
     * @deprecated this method is not allowed.
     */
    @Deprecated
    @Override
    public synchronized void clear() {
        throw new RuntimeException("Method not allowed.");
    }
    
    /**
     * @deprecated this method is not allowed.
     */
    @Deprecated
    @Override
    public synchronized boolean isEmpty() {
        throw new RuntimeException("Method not allowed.");
    }
    
    /**
     * @deprecated this method is not allowed.
     */
    @Deprecated
    @Override
    public Set<Object> keySet() {
        throw new RuntimeException("Method not allowed.");
    }
    
    /**
     * @deprecated this method is not allowed.
     */
    @Deprecated
    @Override
    public void list(PrintStream out) {
        throw new RuntimeException("Method not allowed.");
    }
    
    /**
     * @deprecated this method is not allowed.
     */
    @Deprecated
    @Override
    public void list(PrintWriter out) {
        throw new RuntimeException("Method not allowed.");
    }
    
    /**
     * @deprecated this method is not allowed.
     */
    @Deprecated
    @Override
    public synchronized void load(InputStream inStream) throws IOException {
        throw new RuntimeException("Method not allowed.");
    }
    
    /**
     * @deprecated this method is not allowed.
     */
    @Deprecated
    public synchronized void load(Reader reader) throws IOException {
        throw new RuntimeException("Method not allowed.");
    }
    
    /**
     * @deprecated this method is not allowed.
     */
    @Deprecated
    @Override
    public synchronized void loadFromXML(InputStream in) throws IOException,
            InvalidPropertiesFormatException {
        throw new RuntimeException("Method not allowed.");
    }
    
    /**
     * @deprecated this method is not allowed.
     */
    @Deprecated
    @Override
    public Enumeration<?> propertyNames() {
        throw new RuntimeException("Method not allowed.");
    }
    
    /**
     * @deprecated this method is not allowed.
     */
    @Deprecated
    @Override
    public synchronized void putAll(Map<? extends Object, ? extends Object> t) {
        throw new RuntimeException("Method not allowed.");
    }
    
    /**
     * @deprecated this method is not allowed.
     */
    @Deprecated
    @Override
    public synchronized void save(OutputStream out, String comments) {
        throw new RuntimeException("Method not allowed.");
    }
    
    /**
     * @deprecated this method is not allowed.
     */
    @Deprecated
    @Override
    public void store(OutputStream out, String comments) throws IOException {
        throw new RuntimeException("Method not allowed.");
    }
    
    /**
     * @deprecated this method is not allowed.
     */
    @Deprecated
    public void store(Writer writer, String comments) throws IOException {
        throw new RuntimeException("Method not allowed.");
    }
    
    /**
     * @deprecated this method is not allowed.
     */
    @Deprecated
    @Override
    public synchronized void storeToXML(OutputStream os, String comment)
            throws IOException {
        throw new RuntimeException("Method not allowed.");
    }
    
    /**
     * @deprecated this method is not allowed.
     */
    @Deprecated
    @Override
    public synchronized void storeToXML(OutputStream os, String comment,
            String encoding) throws IOException {
        throw new RuntimeException("Method not allowed.");
    }
    
    /**
     * @deprecated this method is not allowed.
     */
    @Deprecated
    public Set<String> stringPropertyNames() {
        throw new RuntimeException("Method not allowed.");
    }
    
    /**
     * @deprecated this method is not allowed.
     */
    @Deprecated
    @Override
    public Collection<Object> values() {
        throw new RuntimeException("Method not allowed.");
    }
    
    /**
     * @deprecated this method is not allowed.
     */
    @Deprecated
    @Override
    public synchronized boolean equals(Object o) {
        throw new RuntimeException("Method not allowed.");
    }
    
    /**
     * @deprecated this method is not allowed.
     */
    @Deprecated
    @Override
    public synchronized Object setProperty(String key, String value) {
        throw new RuntimeException("Method not allowed.");
    }

    /**
     * @deprecated this method is not allowed.
     */
    @Deprecated
    @Override
    public synchronized boolean contains(Object value) {
        throw new RuntimeException("Method not allowed.");
    }

    /**
     * @deprecated this method is not allowed.
     */
    @Deprecated
    @Override
    public String getProperty(String key, String defaultValue) {
        throw new RuntimeException("Method not allowed.");
    }

    /**
     * @deprecated this method is not allowed.
     */
    @Deprecated
    @Override
    public boolean containsValue(Object value) {
        throw new RuntimeException("Method not allowed.");
    }
}
