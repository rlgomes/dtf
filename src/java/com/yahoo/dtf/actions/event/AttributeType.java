package com.yahoo.dtf.actions.event;

import com.yahoo.dtf.actions.event.AttributeType;
import com.yahoo.dtf.exception.ParseException;

public class AttributeType {
    
    public final static AttributeType STRING_TYPE   = new AttributeType();
    public final static AttributeType INT_TYPE      = new AttributeType();
    public final static AttributeType LONG_TYPE     = new AttributeType();
    
    public static AttributeType getType(String type) throws ParseException { 
       
        if (type == null) 
            return STRING_TYPE;
        
        type = type.toLowerCase();
        
        if (type.equals("string")) { 
            return STRING_TYPE;
        } else if (type.equals("int")) { 
            return INT_TYPE;
        } else if (type.equals("long")) { 
            return LONG_TYPE;
        }
        
        throw new ParseException("Unable to parse type [" + type + "]");
    }
}
