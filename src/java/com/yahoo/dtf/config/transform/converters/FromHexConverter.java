package com.yahoo.dtf.config.transform.converters;

import com.yahoo.dtf.exception.ParseException;

public class FromHexConverter implements Converter {

    @Override
    public String convert(String data) throws ParseException {
        try { 
            return "" + Long.valueOf(data, 16);
        } catch ( NumberFormatException e ) { 
            throw new ParseException("[" + data + "] is not hex.");
        }
    }

}
