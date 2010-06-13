package com.yahoo.dtf.config;

import com.yahoo.dtf.exception.ParseException;

/**
 * Interface used to define a dyanmic property within DTF, some examples of 
 * dynamic properties include dtf.timestamp, which will return the 
 * System.currentMillis().
 *
 * Always remember that when creating a dynamic property this class can not have
 * any state and that if you really require retaining state between calls to 
 * protect your getValue method accordingly or use global/thread context feature
 * from Action.getState().
 */
public interface DynamicProperty {
    public String getValue(String args) throws ParseException;
}
