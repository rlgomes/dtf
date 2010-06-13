package com.yahoo.dtf.config;

import com.yahoo.dtf.config.DynamicProperty;
import com.yahoo.dtf.util.TimeUtil;

public class DTFDateStamp implements DynamicProperty {
    
    public static final String DTF_DATESTAMP = "dtf.datestamp";

    public String getValue(String args) {
        return TimeUtil.getDateStamp();
    }
}
