package com.yahoo.dtf.components;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

import com.yahoo.dtf.DTFProperties;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.properties.BaseProperty;
import com.yahoo.dtf.actions.protocol.range.RangeCreate;
import com.yahoo.dtf.config.Config;
import com.yahoo.dtf.config.Properties;
import com.yahoo.dtf.config.RangeProperty;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * A ComponentHook that will make sure that the component always has the latest
 * version of all of the properties found on the runner. Currently keeps track 
 * of all properties sent so that it can make sure to not re-send existing and 
 * unchanged properties.
 * 
 * This current implementation also handles the RangeProperties in a not so 
 * elegant manner, but it works quite well for the time being.
 * 
 * @author rlgomes
 */
public class PropertyState implements ComponentHook {
    
    private final static String SENT_PROPS = "dtf.sendprops.";

    /*
     * Keep track of the sent properties per component
     * 
     * XXX: need to clean up these contexts on an unlock or thread death 
     */
    private synchronized static Hashtable<String,String> 
                                                 getSentProperties(String id) { 
        String tname = Thread.currentThread().getName();
        Hashtable<String,String> sent = 
          (Hashtable<String,String>) Action.getContext(SENT_PROPS + id + tname);
        
        if ( sent == null ) { 
            sent = new Hashtable<String,String>();
            Action.registerContext(SENT_PROPS + id + tname, sent);
        }
           
        return sent;
    }
   
    /* 
     * Static list of some DTF specific properties that should not be sent 
     * over to the component otherwise they will change properties that are 
     * essential to the correct behavior of the DTF framework.
     */
    private static HashMap<String, Integer> PROPERTIES_TO_IGNORE = 
                                                 new HashMap<String, Integer>();
    static { 
        PROPERTIES_TO_IGNORE.put(DTFProperties.DTF_NODE_TYPE,    0);
        PROPERTIES_TO_IGNORE.put(DTFProperties.DTF_NODE_OS,      0);
        PROPERTIES_TO_IGNORE.put(DTFProperties.DTF_NODE_OS_ARCH, 0);
        PROPERTIES_TO_IGNORE.put(DTFProperties.DTF_NODE_OS_VER,  0);
    } 
    
    public static void addPropertiesToIgnore(String key) { 
        PROPERTIES_TO_IGNORE.put(key,  0);
    }
   
    public ArrayList<Action> handleComponent(String id) throws DTFException {
        Config config = Action.getConfig();
        Properties props = config.getProperties();
        Enumeration keys = props.keys();
        ArrayList<Action> result = new ArrayList<Action>();
        
        Hashtable<String, String> sent = getSentProperties(id);
        
        while (keys.hasMoreElements()) { 
            String key = (String) keys.nextElement();
            if ( !PROPERTIES_TO_IGNORE.containsKey(key) ) { 
                if ( config.isType(key, RangeProperty.class) ) {
                    /*
                     * Ranges are treated differently because they need to be
                     * reconstructed on the other side.
                     * 
                     * XXX: would like to make this more generic so it can be 
                     *      applied for other things other than ranges.
                     */
                    RangeProperty rp = config.getPropertyAs(key);
                    String hash = sent.get(rp.getName());
                    
                    if ( hash == null || !hash.equals(""+rp.hashCode()) ) {
	                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	                    DataOutputStream dos = new DataOutputStream(baos);
	                    rp.suspendState(dos);
	                    try { 
	                        dos.close();
	                    } catch (IOException e) { 
	                        throw new ParseException("Error transporting range",e);
	                    }
	                    RangeCreate rc = new RangeCreate();
	                    rc.setName(rp.getName());
	                    rc.bytes(baos.toByteArray());
	                    result.add(rc);
	                    
	                    sent.put(rp.getName(), "" + rp.hashCode());
                    }
                } else { 
                    String value = (String) config.getProperty(key);
                    
                    if ( value == null ) {
                        continue;
                    }
                    
                    String hash = sent.get(key);

                    BaseProperty prop = new BaseProperty();
                    
                    if ( hash != null && hash.equals(value) )
                        continue;

                    prop.setName(key);
                    prop.setValue(value);
                    result.add(prop);
                    
                    sent.put(key,value);
                }
            }
        }

        return result;
    }
}
