package com.yahoo.dtf.actions.util;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

public class DTFProperty extends CDATA {

    public final static int DEFAULT_PROPERTY_LENGTH = 64;

    /**
     * @dtf.attr name
     * @dtf.attr.desc name of the property to define.
     */
    private String name = null;
    
    /**
     * @dtf.attr value
     * @dtf.attr.desc value to give the property defined.
     */
    protected String value = null;
    
    /**
     * @dtf.attr length
     * @dtf.attr.desc The length of this property is used by the by recorders 
     *                and only has real significance for the database recorder 
     *                that needs to specify the column length when creating the 
     *                schema for recording events.
     */
    private String length = null;

    public DTFProperty() { }

    public void execute() throws DTFException { }

    public String getName() throws ParseException { return replaceProperties(name); }
    public void setName(String name) { this.name = name; }

    public String getValue() throws ParseException { return replaceProperties(value); }
    public void setValue(String value) { this.value = value; }

    public int getLength() throws ParseException { return toInt("length",length,64); }
    public void setLength(String length) { this.length = length; } 
   
    /*
     * ONLY USE INTERNALLY!!!
     */
    public String retName() { return name; } 
    public String retValue() { return value; }
}
