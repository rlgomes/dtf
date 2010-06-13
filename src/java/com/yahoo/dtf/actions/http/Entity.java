package com.yahoo.dtf.actions.http;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.util.CDATA;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.streaming.DTFInputStream;

/**
 * @dtf.tag entity
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc this tag defines an entity to be passed in the body of an http
 *               request. The data will be placed in the body of the message 'as
 *               is' and there is currently no support for multi-part messages.
 *               (Can be added later)
 * 
 * @dtf.tag.example 
 * <http_get uri="${dtf.http.uri}" perfrun="true" onFailure="fail">
 *      <entity value="XXXXX"/>
 * </http_get>
 * 
 */
public class Entity extends CDATA {
 
    /**
     * @dtf.attr.name value
     * @dtf.attr.desc the value of the entity to attach to the current http 
     *                request.
     */
    private String value = null;
    
    public void execute() throws DTFException {
        // nothing to do.
    }
    
    public String getValue() throws ParseException { 
        return replaceProperties(value);
    }
    
    public void setValue(String value) { this.value = value; }
    
    public DTFInputStream getEntityStream() throws ParseException { 
        if ( value == null ) { 
            return getCDATAStream();
        } else { 
            return replacePropertiesAsInputStream(value);
        }
    }
}
