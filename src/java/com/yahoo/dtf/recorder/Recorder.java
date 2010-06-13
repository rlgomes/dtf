package com.yahoo.dtf.recorder;

import java.util.ArrayList;
import java.util.Vector;

import com.yahoo.dtf.recorder.Event;
import com.yahoo.dtf.recorder.Recorder;
import com.yahoo.dtf.recorder.RecorderBase;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.recorder.Attribute;
import com.yahoo.dtf.config.Config;
import com.yahoo.dtf.exception.RecorderException;

public class Recorder {
    
    private static String DEFAULT_ATTRIBS_CTX = "dtf.recorder.default.attribs";
    
    private RecorderBase _recorder = null;
    private Recorder _parent = null;
    private String _eventPattern = null;
    
    /**
     * 
     * @param recorder RecorderIntf to use.
     * @param event set to null if you don't want to filter on events.
     */
    public Recorder(RecorderBase recorder, String eventPattern) {
        _recorder = recorder;
        _eventPattern = eventPattern;

    }

    private static synchronized Vector<Attribute> getAttribs() { 
        Vector<Attribute> attribs = (Vector<Attribute>)
                                 Action.getContext(DEFAULT_ATTRIBS_CTX);
        
        if ( attribs == null )  {
            attribs = new Vector<Attribute>();
            Action.registerContext(DEFAULT_ATTRIBS_CTX, attribs);
        }
        
        return attribs;
    }
    
    private void addDefaultAttribute(Attribute attrib) { 
        Vector<Attribute> attribs = getAttribs();
        if ( attribs.contains(attrib) )
            attribs.remove(attrib);
        
        attribs.add(attrib);
    }
    
    public void addDefaultAttribute(String name, String value) { 
        Attribute attrib = new Attribute(name,value,false);
        addDefaultAttribute(attrib);
        
        if (_parent != null)
            _parent.addDefaultAttribute(attrib);
    }
   
    public void addDefaultAttribute(String name, String value, int length) { 
        Attribute attrib = new Attribute(name,value,length,false);
        addDefaultAttribute(attrib);
        
        if (_parent != null)
            _parent.addDefaultAttribute(attrib);
    }
    
    public void record(Event event) throws RecorderException {
        if (_eventPattern == null || event.getName().startsWith(_eventPattern)) {
            /*
             * We need to record the recorded time on the client side of 
             * recording because time from client side can not be used for
             * calculating the exact time an event occurred.
             */
            Vector<Attribute> attribs = getAttribs();
            event.addAttributesAndOverwrite(attribs);
             
            _recorder.record(event);
        }
       
        if (_parent != null) {
            _parent.record(event);
        } else {
            /*
             * Once the event reaches the topmost Recorder parent then we can 
             * record this event and its fields to make it available in the 
             * test case so the user can validate the fields of the event during
             * certain FVT tests.
             */
	        Config config = Action.getConfig();
	        eventToConfig(event, config);
        }
    }
    
    public static void eventToConfig(Event event, Config config) { 
        ArrayList<Attribute> attributes = event.findActions(Attribute.class);
        String eventName = event.getName() + ".";
        
        config.setProperty(eventName + "start", event.getStart());
        config.setProperty(eventName + "stop", event.getStop());
        for (int i = 0; i < attributes.size(); i++) { 
            Attribute attr = attributes.get(i);
            config.setProperty(eventName + attr.retName(), attr.retValue());
        }
    }
    
    public void setParent(Recorder recorder) { _parent = recorder; } 
    public Recorder getParent() { return _parent; } 
   
    public void start() throws RecorderException { _recorder.start(); }
    public void stop() throws RecorderException {  _recorder.stop();  }
}
