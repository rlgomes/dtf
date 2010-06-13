package com.yahoo.dtf.actions.protocol.deploy;

import com.yahoo.dtf.exception.ParseException;


public class Dtfx extends DTFNode {

    private String test = null; 

    private String logs = null;
    
    public String getTest() throws ParseException { return replaceProperties(test); }
    public void setTest(String test) { this.test = test; }

    public String getLogs() throws ParseException { return replaceProperties(logs); }
    public void setLogs(String logs) { this.logs = logs; }
}
