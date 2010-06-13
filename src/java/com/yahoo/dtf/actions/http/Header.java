package com.yahoo.dtf.actions.http;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag header
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc this tag is used within the context of an http request and 
 *               will define a simple header with a name and value. 
 * 
 * @dtf.tag.example 
 * <http_get uri="${dtf.http.uri}" perfrun="true" onFailure="fail">
 *      <header name="header" value="XXXXX"/>
 * </http_get>
 * 
 */
public class Header extends Action {
  
    /**
     * @dtf.attr.name name
     * @dtf.attr.desc the name of the header being specified.
     */
    private String name = null;

    /**
     * @dtf.attr.name value
     * @dtf.attr.desc the value of the header being specified.
     */
    private String value = null;
    
    public void execute() throws DTFException {
        // nothing to do.
    }
    
    public String getName() throws ParseException { 
        return replaceProperties(name);
    }
    public void setName(String name) { this.name = name; }

    public String getValue() throws ParseException { 
        return replaceProperties(value);
    }
    public void setValue(String value) { this.value = value; }
}
