package com.yahoo.dtf.recorder;

import java.net.URI;
import java.util.Iterator;

import com.yahoo.dtf.DTFConstants;
import com.yahoo.dtf.recorder.Event;
import com.yahoo.dtf.recorder.RecorderBase;
import com.yahoo.dtf.recorder.Attribute;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.exception.RecorderException;

public class ObjectRecorder extends RecorderBase {
    
    private String name = null;
    
    public ObjectRecorder(URI uri,
                          boolean append) throws RecorderException  {
        this(uri, append, DTFConstants.DEFAULT_ENCODING);
    }
    
    public ObjectRecorder(URI uri,
                          boolean append,
                          String encoding) throws RecorderException  {
        super(uri,append,encoding);
        // validate we only support the scheme "property" 
        if (!uri.getScheme().equals("property")) 
            throw new RecorderException("Only accepting sceheme property://XXX");
        
        // make sure to include host + authority + path so that we have the 
        // full property name
        if (uri.getHost() != null && !uri.getHost().trim().equals(""))
            name = uri.getHost();

        if (uri.getAuthority() != null && !uri.getAuthority().trim().equals(""))
            name = uri.getAuthority();
    }

    public void stop() throws RecorderException {
        
    }

    public void start() throws RecorderException {
        
    }

    public void record(Event event) throws RecorderException {
        Action.getConfig().setProperty(name + ".start", "" + event.getStart());
        Action.getConfig().setProperty(name + ".stop", "" + event.getStop());
      
        Iterator attributes = event.children().iterator();
        
        try { 
            while (attributes.hasNext()) { 
                Attribute attribute = (Attribute)attributes.next();
                Action.getConfig().setProperty(name + "." + attribute.getName(), 
                                               "" + attribute.getValue());
            }     
        } catch (ParseException e) {
            throw new RecorderException("Unable to parse attribute.",e);
        }
    }
}
