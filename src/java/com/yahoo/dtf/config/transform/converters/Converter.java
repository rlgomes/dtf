package com.yahoo.dtf.config.transform.converters;

import com.yahoo.dtf.exception.ParseException;

public interface Converter {

    /**
     * Simply return the data converted into the format that this converter 
     * is suppose to be supporting
     * 
     * @param data
     * @return
     */
    public String convert(String data) throws ParseException;
}
