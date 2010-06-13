package com.yahoo.dtf.util;


public class NumberUtil {

    private static String numbers = "0123456789";
    
    private static boolean[] NUMBERS = new boolean[256];
    
    static {
        for (int i = 0; i < NUMBERS.length; i++) { 
            NUMBERS[i] = numbers.indexOf((char)i) != -1;
        }
    }

    /**
     * Best way to detect that the string being passed is a long value, the 
     * NumberUtilSuite compares using regular expressions and new Long(value) 
     * against this method and both lose by more than 5x in terms of
     * performance.
     *  
     * @param string
     * @return
     */
    public static boolean isLong(String string) {
        for(int i = 0; i < string.length(); i++) { 
            char c = string.charAt(i);
            
            if ( c > NUMBERS.length || !NUMBERS[c] )
                return false;
        }
        
        return (string.length() != 0);
    }
    
    /**
     * Best way to detect that the string being passed is a double value, the 
     * NumberUtilSuite compares using regular expressions and new Double(value) 
     * against this method and both lose by more than 5x in terms of
     * performance.
     *  
     * @param string
     * @return
     */
    public static boolean isDouble(String string) {
        for(int i = 0; i < string.length(); i++) { 
            char c = string.charAt(i);
            if ( c > NUMBERS.length || (!NUMBERS[c] && c != '.') )
                return false;
        }
        
        return (string.length() != 0);
    }
}
