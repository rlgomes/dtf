package com.yahoo.dtf.config.transform.converters;

import com.yahoo.dtf.exception.ParseException;

public class ToHexConverter implements Converter {

    @Override
    public String convert(String data) throws ParseException {
        try { 
            Long tmp = Long.valueOf(data);
            return "" + Long.toHexString(tmp);
        } catch ( NumberFormatException e ) { 
            throw new ParseException("[" + data + "] is not an integer.");
        }
    }

}
