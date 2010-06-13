package com.yahoo.dtf.config.transform;

import org.apache.commons.jxpath.JXPathContext;

import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.json.JSONException;
import com.yahoo.dtf.json.JSONObject;

public class JPathTransformer implements Transformer {
    
    public String apply(String data, String expression) throws ParseException {
        try { 
            JSONObject json = new JSONObject(data);
            JXPathContext ctx = JXPathContext.newContext(json);
            return ctx.getValue(expression).toString();
        } catch (JSONException e) { 
            throw new ParseException("Error parsing JSON.",e);
        }
    }
}
