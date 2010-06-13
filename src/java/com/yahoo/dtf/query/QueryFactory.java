package com.yahoo.dtf.query;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.yahoo.dtf.query.QueryFactory;
import com.yahoo.dtf.query.QueryIntf;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.QueryException;
import com.yahoo.dtf.exception.RecorderException;
import com.yahoo.dtf.logger.DTFLogger;


public class QueryFactory {
    private static DTFLogger _logger = DTFLogger.getLogger(QueryFactory.class);
    private static HashMap _queries =  new HashMap();
    
    public static QueryIntf getQuery(String type) throws DTFException { 
        Class queryClass = (Class)_queries.get(type);
        
        if (queryClass == null)
            throw new RecorderException("Unsupported query type: '" + type + "'");
        
        Class[] parameters = new Class[] {};
        Object[] args = new Object[] {};
        
        try {
            return (QueryIntf) queryClass.getConstructor(parameters).newInstance(args);
        } catch (IllegalArgumentException e) {
            throw new QueryException("Unable to instantiate query [" + type + "].",e);
        } catch (SecurityException e) {
            throw new QueryException("Unable to instantiate query [" + type + "].",e);
        } catch (InstantiationException e) {
            throw new QueryException("Unable to instantiate query [" + type + "].",e);
        } catch (IllegalAccessException e) {
            throw new QueryException("Unable to instantiate query [" + type + "].",e);
        } catch (InvocationTargetException e) {
            throw new QueryException("Unable to instantiate query [" + type + "].",e);
        } catch (NoSuchMethodException e) {
            throw new QueryException("Unable to instantiate query [" + type + "].",e);
        }
    }
    
    public static <T extends QueryIntf> 
           void registerQuery(String name, Class<T> queryClass) { 
        if (_queries.containsKey(name)) 
            _logger.warn("Overwriting query implementation for [" + name + "]");
        
        if (_logger.isDebugEnabled())
            _logger.debug("Registering query [" + name + "]");
        
        _queries.put(name, queryClass);
    } 
    
    public static ArrayList<String> getQueryNames() { 
        ArrayList<String> names = new ArrayList<String>();
        Iterator<String> iter = _queries.keySet().iterator();
        
        while (iter.hasNext()) 
            names.add(iter.next());
        return names;
    }
}
