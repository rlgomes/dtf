package com.yahoo.dtf.actions.properties;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag unset 
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc This tag simply unsets the property defined by the attribute 
 *               property from the DTF state. This allows you to make a property
 *               dissappear from execution.
 *
 * @dtf.tag.example 
 * <unset property="blah"/>
 */
public class Unset extends Action {

    /**
     * @dtf.attr property
     * @dtf.attr.desc The name of the property to unset.
     */
    private String property = null;
    
    @Override
    public void execute() throws DTFException {
        if ( getProperty() != null ) { 
            getConfig().remove(getProperty());
        }
    }

    public String getProperty() throws ParseException { return replaceProperties(property); }
    public void setProperty(String property) { this.property = property; }
}
