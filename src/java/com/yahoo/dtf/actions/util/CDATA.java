package com.yahoo.dtf.actions.util;

import java.util.Hashtable;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.streaming.DTFInputStream;

public class CDATA extends Action {
   
    private String CDATA = null;
   
    public CDATA() { }
    
    public void setCDATA(String CDATA) { this.CDATA = CDATA; }
    public String getCDATA() throws ParseException { return replaceProperties(CDATA); } 

    public DTFInputStream getCDATAStream() throws ParseException { 
        return replacePropertiesAsInputStream(CDATA);
    } 

    public void execute() throws DTFException { }
    
    @Override
    protected Hashtable<String, Object> getAttribs(Class actionClass) {
        Hashtable<String, Object> attribs = super.getAttribs(actionClass);
        String cdata;
        
        if ( CDATA != null ) 
            attribs.put("CDATA", CDATA);
        
        return attribs;
    }
}
