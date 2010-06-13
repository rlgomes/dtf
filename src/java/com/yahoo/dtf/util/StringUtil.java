package com.yahoo.dtf.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    /**
     * 
     * @param str1
     * @param str2
     * @return
     */
    public static boolean equals(String str1, String str2) {
        if (str1 == null || str2 == null)
            return true;
        else
            return str1.equals(str2);
    }
   
    /**
     * 
     * @param str1
     * @param str2
     * @return
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null || str2 == null)
            return true;
        else
            return str1.equalsIgnoreCase(str2);
    }
   
    /**
     * Natural comparison for strings containing numbers and alpha numeric 
     * sequences. This is really useful for ordering files in a directory but
     * even more useful for comparing arbitrary strings that can be alphanumeric
     * or just a number inside a string.
     *  
     * @param str1
     * @param str2
     * @return
     */
    private static Pattern num = Pattern.compile("(\\-?[0-9]*)(\\.?[0-9]+)(.*)");
    
    public static int naturalCompare(String str1, String str2) {
        /*
         * Match numbers with . and , in them.
         */
        
        while (str1 != null && str1.length() != 0 &&
               str2 != null && str2.length() != 0) {
            
            Matcher num1 = num.matcher(str1);
            Matcher num2 = num.matcher(str2);
            
            if (num1.matches() && num2.matches()) {
                String n1 = "";
                String n2 = "";
                
                for(int i = 1; i <= num1.groupCount()-1; i++)
                    if ( num1.group(i) != null )
                        n1 += num1.group(i);

                for(int i = 1; i <= num2.groupCount()-1; i++)
                    if ( num2.group(i) != null )
                        n2 += num2.group(i);
                
                Double d1 = new Double(n1);
                Double d2 = new Double(n2);

                if (d1.equals(d2)) {
                    str1 = num1.group(num1.groupCount());
                    str2 = num2.group(num2.groupCount());
                } else {
                    return (d1 < d2 ? -1 : 1);
                }
            } else {
                if (str1.equals(str2)) {
                    return 0;
                } else {
                    return str1.compareTo(str2);
                }
            }
        }
        
        if (str1 == null) {
            if (str2 == null) {
                return 0;
            } else {
                return -1;
            }
        } else {
            if (str2 == null) {
                return -1;
            } else {
                return str1.compareTo(str2);
            }
        }
    } 
   
    /**
     * 
     * @param value
     * @param length
     * @param paddingChar
     * @return
     */
    public static String padString(String value, int length, char paddingChar) {
        if (value.length() <= length) {
            int padlength = length - value.length();
            
            for (int i = 0; i < padlength; i++)
                value = paddingChar + value;
            
            return value;
        }
        return value;
    }
   
    /**
     * 
     * @param source
     * @param pattern
     * @param replace
     * @return
     */
    public static String replace(String source, String pattern, String replace) {
        if (source != null) {
            final int len = pattern.length();
            StringBuffer sb = new StringBuffer();
            int found = -1;
            int start = 0;

            while ((found = source.indexOf(pattern, start)) != -1) {
                sb.append(source.substring(start, found));
                sb.append(replace);
                start = found + len;
            }

            sb.append(source.substring(start));

            return sb.toString();
        } else
            return "";
    }

    private static Pattern isANumber = Pattern.compile("[0-9]+\\.[0-9]+|[0-9]+");
    public static boolean isNumber(String number) { 
        if ( isANumber.matcher(number).matches() ) {
            return true;
        } else { 
            return false;
        }
    }
   
    /**
     * 
     * @param string
     * @return
     */
    public static String capitalize(String string) {
        string = string.toLowerCase();
        String firstLetter = string.substring(0, 1).toUpperCase();
        return firstLetter + string.substring(1);
    }
}
