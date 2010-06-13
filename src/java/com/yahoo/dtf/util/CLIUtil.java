package com.yahoo.dtf.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.recorder.Event;

/**
 * contains some useful methods  that can be used across different CLI 
 * implementations that want to read back Events from the STDOUT of the CLI.
 * 
 *
 */
public class CLIUtil {

    /**
     * 
     * @param br the BufferedReader that is connected to the STDOUT of the CLI.
     * @param command the command name to be used when reporting errors
     * @param id the unique identifier of this CLI session to be used when 
     *           reporting errors.
     * @return
     * @throws DTFException
     * @throws IOException
     */
    public static Event readEvent(BufferedReader br, 
                                  String command,
                                  String id,
                                  boolean throwException) 
                  throws DTFException, IOException {
        LinkedHashMap<String, String> props = new LinkedHashMap<String, String>();

        String line = br.readLine();
        String errorMsg = null;
        while ((line != null) && (!line.trim().equals(""))) {
            // comment skip this line
            if (line.charAt(0) != '#') {
                // using split is more expensive because it uses Pattern
                // class to do the matching.
                int index = line.indexOf('=');

                if (index == -1) {
                    throw new DTFException("Invalid response [" + line + "]");
                }

                String key = line.substring(0, index);
                String value = line.substring(index + 1);

                if (key.endsWith("errorMsg")) {
                    errorMsg = value;
                }

                if (throwException && 
                    key.endsWith(".succeeded") && 
                    !value.equalsIgnoreCase("true")) {
                    throw new DTFException("Error executing [" + command + 
                                           "] cause [" + errorMsg + 
                                           "] on [" + id + "]");

                }
                props.put(key, value);
            }

            line = br.readLine();
        }
       
        return hashMapToEvent(props);
    }
   
    /**
     * Converts a HashMap<String,String> to an event based on the fact that the
     * keys of the HashMap are the full name of the event+.+keyname for existing
     * events that were previously generated.
     * 
     * @param map
     * @return
     */
    public static Event hashMapToEvent(HashMap<String, String> map) {
        Iterator<Entry<String,String>> entries = map.entrySet().iterator();
        Event event = new Event();

        // find the eventName first
        String eventName = null;
        while ( entries.hasNext() ) {
            Entry<String,String> entry = entries.next();
            String key = entry.getKey();
            if (key.endsWith(".start")) {
                eventName = key.substring(0, key.lastIndexOf("."));
                event.setName(eventName);
                break;
            }
        }

        entries = map.entrySet().iterator();
        while ( entries.hasNext() ) {
            Entry<String,String> entry = entries.next();
            String key = entry.getKey();

            if (key.endsWith(".start")) {
                event.setStart(new Long(entry.getValue()).longValue());
            } else if (key.endsWith(".stop")) {
                event.setStop(new Long(entry.getValue()).longValue());
            } else {
                String attribName = StringUtil.replace(key, eventName + ".", "");
                event.addAttribute(attribName, entry.getValue());
            }
        }
        return event;
    }
   
}
