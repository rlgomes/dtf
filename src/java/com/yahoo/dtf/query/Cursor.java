package com.yahoo.dtf.query;


import java.util.HashMap;

import com.yahoo.dtf.query.QueryIntf;
import com.yahoo.dtf.exception.QueryException;

public class Cursor {

    private QueryIntf _query = null;
    
    public Cursor(QueryIntf query) { 
        _query = query;
    }
    
    public String getCursorName() { return _query.getProperty(); } 

    public HashMap<String, String> next() throws QueryException { 
        return next(false);
    }
    
    public HashMap<String, String> next(boolean recycle) throws QueryException { 
        return _query.next(recycle);
    }
    
    public void reset() throws QueryException { _query.reset(); }
    public void close() throws QueryException { _query.close(); } 
    public QueryIntf getQuery() { return _query; } 
}
