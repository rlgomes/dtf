package com.yahoo.dtf.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yahoo.dtf.exception.ParseException;

public class ByteUtil {

    private static final String BYTES_REGEXP = "([0-9]*)((K|M|G)?(b|B))";
    
    private static Pattern pattern = Pattern.compile(BYTES_REGEXP);
    
    private static long KILOBYTE = 1024;
    private static long MEGABYTE = 1024*1024;
    private static long GIGABYTE = 1024*1024*1024;

    /**
     * Utility function to parse byte length expression such as 2GB into the 
     * corresponding byte count.
     * 
     * 2GB = 2,147,483,648 bytes
     * 1MB = 131,072 bytes
     * etc.
     * 
     * This method does take into account that B is byte while b is bit and will
     * do the math accordingly
     * 
     * @param property
     * @param bytes
     * @return
     * @throws ParseException 
     */
    public static long parseBytes(String property, String bytes) throws ParseException {
        Matcher matcher = pattern.matcher(bytes);
        
        if ( !matcher.matches() ) {
            // if its just a number without a suffix then we consider it to be
            // in bytes
            try {
                return new Long(bytes).longValue();
            } catch (NumberFormatException e) {
                throw new ParseException("Unable to parse property [" + property + 
                                         "] as a long",e);
            }
        } else {
            long value = new Long(matcher.group(1));
            String suffix = matcher.group(2);
            
            if ( suffix.equals("b") ) 
                return value / 8;

            if ( suffix.equals("Kb") ) 
                return (value * KILOBYTE) / 8;
            
            if ( suffix.equals("Mb") ) 
                return (value * MEGABYTE) / 8 ;

            if ( suffix.equals("Gb") ) 
                return (value * GIGABYTE) / 8 ;

            if ( suffix.equals("B") ) 
                return value;

            if ( suffix.equals("KB") ) 
                return value * KILOBYTE;
            
            if ( suffix.equals("MB") ) 
                return value * MEGABYTE;

            if ( suffix.equals("GB") ) 
                return value * GIGABYTE;

            throw new ParseException("Property: " + property + " does not respect the expression: "
                                     + BYTES_REGEXP);
        }
    }

}
