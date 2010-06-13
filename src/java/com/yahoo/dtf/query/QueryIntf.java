package com.yahoo.dtf.query;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import com.yahoo.dtf.DTFConstants;
import com.yahoo.dtf.actions.conditionals.Condition;
import com.yahoo.dtf.exception.QueryException;

/**
 * Its called QueryIntf because it use to be an interface, dont' want to change 
 * the name and have to update any references from other projects. Keeping the 
 * same name is much easier.
 * 
 * @author rlgomes
 */
public abstract class QueryIntf {
  
    /**
     * 
     * @param is
     * @param uri
     * @param fields
     * @param constraints
     * @param event
     * @param property
     * @param encoding
     * @throws QueryException
     */
    public abstract void open(URI uri,
			                  ArrayList fields, 
			                  Condition constraints, 
			                  String event,
			                  String property,
			                  String encoding) throws QueryException;

    /**
     * legacy function when we use to not have encodings required for backward
     * compatibility.
     * 
     * @param is
     * @param uri
     * @param fields
     * @param constraints
     * @param event
     * @param property
     * @throws QueryException
     */
    public void open(URI uri,
                     ArrayList fields, 
                     Condition constraints, 
                     String event,
                     String property) throws QueryException {
        open(uri,fields,constraints,event,property,DTFConstants.DEFAULT_ENCODING);
    }

    /**
     * 
     * @return
     */
    public abstract String getProperty();
   
    /**
     * 
     * @param recycle
     * @return
     * @throws QueryException
     */
    public abstract HashMap<String, String> next(boolean recycle) throws QueryException;
  
    /**
     * 
     * @throws QueryException
     */
    public abstract void close() throws QueryException;
   
    /**
     * 
     * @throws QueryException
     */
    public abstract void reset() throws QueryException;
    
    /**
     * This function is required to know exactly how many times this query has
     * been reset. This number can be used by other tags to generate events that
     * are useful at runtime to know if we're trying to find an element that
     * doesn't exist at any point within the events that are found by this 
     * query. So by having the reset count we can easily know when we've reset 
     * and be able to make a decision. 
     */
    public abstract long getResetCount();
}
