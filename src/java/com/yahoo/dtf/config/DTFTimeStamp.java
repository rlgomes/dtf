package com.yahoo.dtf.config;

import com.yahoo.dtf.config.DynamicProperty;
import com.yahoo.dtf.util.TimeUtil;

public class DTFTimeStamp implements DynamicProperty {
    
    public static final String DTF_TIMESTAMP = "dtf.timestamp";

    public String getValue(String args) {
        return TimeUtil.getTimeStamp();
    }
}
