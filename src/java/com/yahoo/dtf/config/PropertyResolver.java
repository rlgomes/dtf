package com.yahoo.dtf.config;

import java.util.regex.Matcher;

/**
 * This class defines an interface for different type of property resolvers 
 * which can be used by DTF. The idea behind a PropertyResolver is that you can 
 * easily define your own type of properties that can be resolve in a different
 * way then just looking them up in the available list of properties.
 * 
 * @author rlgomes
 */
public abstract class PropertyResolver {

    /**
     * Here you'll identify the signature that is to be used to identify the 
     * exact signature of a property that should be resolved by this
     * Property Resolver.
     *  
     * @return
     */
    public abstract Matcher getSignature();
    
    /**
     * 
     * @param property
     * @return
     */
    public abstract String getPropetyValue(String property);
    
}
