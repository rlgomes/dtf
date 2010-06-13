package com.yahoo.dtf.recorder;

import java.util.ArrayList;
import java.util.Vector;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.recorder.Attribute;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.exception.RecorderException;
import com.yahoo.dtf.util.ByteArrayUtil;

public class Event extends Action {
    
    private String name = null;
    
    private long start = -1;
    private long stop = -1;
    
    public Event() { }
    public Event(String name) {
        this();
        setName(name);
    }
    
    public void execute() throws RecorderException { 
        getRecorder().record(this);
    }
    
    public void start() { start = System.currentTimeMillis(); }
    public void stop() { stop = System.currentTimeMillis(); }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; } 
 
    private Attribute findAttribute(String name) {
        ArrayList attributes = children();
        for (int i = 0; i < attributes.size(); i++) { 
            Attribute attrib = (Attribute) attributes.get(i);
            if (attrib.retName().equals(name)) { 
                return attrib;
            }
        }
        return null;
    }
    
    public Attribute retAttribute(String name) {
        return (Attribute)findAttribute(name);
    }
    
    public Long attributeAsLong(String name) throws ParseException { 
        try { 
            return new Long(findAttribute(name).getValue());
        } catch (NumberFormatException e) { 
            throw new ParseException("Unable to parse attribute value.",e);
        }
    }
    
    public Integer attributeAsInt(String name) throws ParseException { 
        try { 
            return new Integer(findAttribute(name).getValue());
        } catch (NumberFormatException e) { 
            throw new ParseException("Unable to parse attribute value.",e);
        }
    }
    
    public Boolean attributeAsBoolean(String name) throws ParseException { 
        return Boolean.valueOf(findAttribute(name).getValue());
    } 
    
    public boolean isIndex(String key) throws ParseException {
        Attribute attribute = retAttribute(key);
        return (attribute != null ? attribute.isIndex() : false);
    }

    public synchronized void addAttribute(String name, String value, boolean index) {
        Attribute attribute = new Attribute(name,value,index);
        addAction(attribute);
    }
    
    public synchronized void addAttribute(String name, String value, int length, boolean index) {
        Attribute attribute = new Attribute(name,value,length,index);
        addAction(attribute);
    }
    
    public synchronized void addAttributesAndOverwrite(Vector attributes) {
        for (int i = 0; i < attributes.size(); i++) { 
            Attribute attrib = (Attribute)attributes.get(i);
            Attribute fAttrib = findAttribute(attrib.retName());
                  
            if (fAttrib != null) { 
                fAttrib.setValue(attrib.retValue());
            } else 
                super.addAction(attrib);
        }
    }
    
    public void addAttribute(String name, boolean value) {
        addAttribute(name,""+value,false);
    }
    
    public void addAttribute(String name, long value) {
        addAttribute(name,""+value,false);
    }

    public void addAttribute(String name, double value) {
        addAttribute(name,""+value,false);
    }
    
    public void addAttribute(String name, String value) {
        addAttribute(name,value,false);
    }
    
    public void addAttribute(String key, byte[] bytes) {
        addAttribute(key, ByteArrayUtil.byteArrayToHexString(bytes));
    }
  
    public long getStart() { return start; }
    public void setStart(long start) { this.start = start; }
    
    public long getStop() { return stop;}
    public void setStop(long stop) { this.stop = stop;}
}
