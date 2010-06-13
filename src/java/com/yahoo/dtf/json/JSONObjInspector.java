package com.yahoo.dtf.json;

import org.apache.commons.jxpath.DynamicPropertyHandler;

public class JSONObjInspector implements DynamicPropertyHandler {
    
    public Object getProperty(Object arg0, String arg1) {
        JSONObject json = (JSONObject) arg0;

        try {
            Object result = json.get(arg1);
            if (result instanceof JSONArray) { 
                JSONArray array = ((JSONArray)result);
                Object[] res = new Object[array.length()];
                for (int i = 0; i < array.length(); i++) { 
                    res[i] = array.get(i);
                }
                return res;
            } else { 
                return result;
            }
        } catch (JSONException e) {
            return null;
        }
    }

    public String[] getPropertyNames(Object arg0) {
        JSONObject json = (JSONObject) arg0;
        
        JSONArray names = json.names();
        String[] result = new String[names.length()];
        
        for (int i = 0; i < names.length(); i++) { 
            try {
                result[i] = names.getString(i);
            } catch (JSONException e) {
                result[i] = null;
            }
        }
        
        return result;
    }

    public void setProperty(Object arg0, String arg1, Object arg2) {
        // don't see any reason to implement this
    }
}
