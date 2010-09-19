package com.yahoo.dtf.config;

import com.yahoo.dtf.exception.ParseException;

/**
 * <p>
 * Interface used to define a dynamic property within DTF, some examples of 
 * dynamic properties include dtf.timestamp, which will return the 
 * System.currentMillis().
 * </p>
 * <p> 
 * Always remember that when creating a dynamic property this class can not have
 * any state and that if you really require retaining state between calls to 
 * protect your getValue method accordingly or use global/thread context feature
 * from Action.getState().
 * </p>
 */
public interface DynamicProperty {
    
    /**
     * Return the dynamic value of this dynamic property and use the arguments
     * passed for passing relevant information to this class. The arguments is
     * just whatever was the arguments in the property definition, such as:
     * 
     * ${mydynamicproperty(arguments)}
     * 
     * Their syntax is pretty flexible but you can't use $,{,},( or ) symbols 
     * since they would confuse the parsing of the property.
     * 
     * Note: If there are no arguments the args will be null.
     * 
     * @param args arguments passed to the dynamic property. When there are no 
     *             arguments then null is passed.
     * @return
     * @throws ParseException
     */
    public String getValue(String args) throws ParseException;
}
