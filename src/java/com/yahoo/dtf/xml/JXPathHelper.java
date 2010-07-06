package com.yahoo.dtf.xml;

import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.jxpath.JXPathContext;

/**
 * JXPath helper functions.
 * 
 * @author rlgomes
 */
public abstract class JXPathHelper {

    /**
     * Register a HashMap of namespace mappings into the given JXPathContext
     * object.
     * 
     * @param ctx
     * @param map
     */
    public static void registerNamespaces(JXPathContext ctx, 
                                          HashMap<String, String> map) { 
        for (Entry<String,String> entry : map.entrySet() ) { 
            ctx.registerNamespace(entry.getKey(), entry.getValue());
        } 
    }
   
    /**
     * Register a HashMap of namespace mapping expression into the given 
     * JXPathContext object by parsing it with the parseNamespaceMap function 
     * and then registering the namespaces with the registerNamespaces function..
     *  
     * @param ctx
     * @param expr
     */
    public static void registerNamespaces(JXPathContext ctx,
                                          String expr) { 
        HashMap<String, String> mapping = parseNamespaceMap(expr);
        registerNamespaces(ctx, mapping);
    }
    
    /**
     * Parses namespace mapping expressions in the the format:
     *
     * ns1=>urn:dtf:test,ns2=>yaddah
     * 
     * and returns a HashMap that can be used to register namespaces using the
     * other helper method registerNamespaces.
     * 
     * @param expr
     * @return
     */
    public static HashMap<String, String> parseNamespaceMap(String expr) { 
        HashMap<String, String> result = new HashMap<String, String>();
        
        String[] maps = expr.split(",");
                
        for ( String map : maps ) { 
            String[] nsMap = map.split("=>");
            result.put(nsMap[0], nsMap[1]);
        }
        
        return result;
    }
}
