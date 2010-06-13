package com.yahoo.dtf.recorder;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

public class Attribute extends Action {

    private boolean index = false;
    
    private String type = null;

    private String name = null;

    protected String value = null;
    
    private int length = -1; 


    public Attribute() { }
    
    public Attribute(String name, String value) { 
        setName(name);
        setValue(value);
        setIndex(false);
    }
    
    public Attribute(String name, String value, boolean index) { 
        setName(name);
        setValue(value);
        setIndex(index);
    }
    
    public Attribute(String name, String value, int length, boolean index) { 
        setName(name);
        setValue(value);
        setIndex(index);
        setLength(length);
    }
    
    public void execute() throws DTFException {
        
    }

    public boolean isIndex() throws ParseException { return index; }
    public String getIndex() throws ParseException { return ""+index; }
    public void setIndex(String index) throws ParseException { this.index = toBoolean("index",index); }
    public void setIndex(boolean index) { this.index = index; }

    public String getType() throws ParseException { return type; }
    public void setType(String type) { this.type = type; }
    
    public boolean equals(Object obj) {
        if ( obj instanceof Attribute ) { 
            Attribute other = (Attribute) obj;
            try {
                return other.getName().equals(this.getName());
            } catch (ParseException e) { 
                getLogger().error("Issue tryign to compare Attribute.",e);
            }
        }
        
        return false;
    }
    
    public String getName() throws ParseException { return name; }
    public void setName(String name) { this.name = name; }

    public String getValue() throws ParseException { return value; }
    public void setValue(String value) { this.value = value; }

    public int getLength() throws ParseException { return length; }
    public void setLength(String length) throws ParseException { this.length = toInt("length",length); } 
    public void setLength(int length) { this.length = length; } 
   
    /*
     * ONLY USE INTERNALLY!!!
     */
    public String retName() { return name; } 
    public String retValue() { return value; }
    
    @Override
    public int hashCode() {
        assert false: "No hash code assigned";
        return 42;
    } 
    
}