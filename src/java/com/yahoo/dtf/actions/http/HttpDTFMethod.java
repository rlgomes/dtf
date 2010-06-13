package com.yahoo.dtf.actions.http;

import org.apache.commons.httpclient.methods.EntityEnclosingMethod;

/**
 * Method is very useful for allowing the user to create his/her own HTTP
 * request that can be used to interact with services such as WEBDAV
 * 
 * @author rlgomes
 */
public class HttpDTFMethod extends EntityEnclosingMethod {

    private String _name = null;
    
    public HttpDTFMethod(String uri, String name) { 
        super(uri);
        _name = name;
    }
  
    public String getName() {
        return _name;
    }

}
