package com.yahoo.dtf.actions.selenium.commands.state;

import java.util.Arrays;

import com.yahoo.dtf.actions.selenium.Selenium;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * Helper class that makes it very easy to add new getXXX commands to the 
 * list of available selenium commands within DTF. You extend from this class
 * and implement the getValue() method and the rest is taken care of.
 * 
 * @author rlgomes
 */
public abstract class SeleniumGetStateTag extends Selenium {
    
    /**
     * @dtf.attr property
     * @dtf.attr.desc  the name of the property to save the value to.
     */
    private String property = null;
 
    /**
     * Get the current value of the selenium state required and return it so it
     * can be saved in the property identified by the attribute property.
     * 
     * @return
     * @throws DTFException
     */
    public abstract Object getValue() throws DTFException;
    
    @Override
    public final void execute() throws DTFException {
        Object obj = getValue();
        String value = null;
        if ( obj instanceof String[] ) { 
            value = Arrays.toString((String[])obj);
        } else { 
            value = "" + obj;
        }
        getConfig().setProperty(getProperty(), value);
    }
    
    public String getProperty() throws ParseException {
        return replaceProperties(property);
    }
    
    public void setProperty(String property) {
        this.property = property;
    }
}
