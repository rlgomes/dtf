package com.yahoo.dtf.config.transform;

import com.yahoo.dtf.exception.ParseException;

/**
 * Remember to keep your class stateless, because instead of having hundreds of
 * instantiated classes of your transformer we only have a single implementation
 * and feed it the current data and expression to handle.
 * 
 * If for some reason you require state remember to use the same 
 * Action.getState.registerContext() or registerGlobalContext() for your needs.
 */
public interface Transformer {
    public String apply(String data, String expression) throws ParseException;
}
