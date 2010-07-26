package com.yahoo.dtf.config.transform;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.feature String Encoding
 * @dtf.feature.group Transformers
 * @dtf.feature.desc <p>
 *                   This is the DTF string encoding transformer and it can be 
 *                   used to apply encoding functions to the properties to apply
 *                   certain types of encoding to the property values.
 *                   </p> 
 *                   <p>The following are the availble encoding functions 
 *                   currently implemented.</p>
 *                   <p>
 *                   <b>xpath-escape</b><br/>
 *                   Used to escape XPATH expressions that may contain special 
 *                   characters such as " and '. This then allows you to use 
 *                   that expression without worries within the application of 
 *                   an XPATH expression.
 *                   </p> 
 *                   <p> 
 *                   <b>url-encode</b><br/>
 *                   Used to encode any string into a URL safe format, mainly 
 *                   converting characters such as spaces, slashes into the 
 *                   right format so that they don't break URL parsing.
 *                   </p> 
 *                   <p> 
 *                   <b>url-decode</b><br/>
 *                   Used to do the inverse of url-encode.
 *                   </p>
 *                   
 * @dtf.example
 * <log>${prop1:apply:url-encode}</log>                  
 */
public class ApplyTransformer implements Transformer {

    public String apply(String data, String expression) throws ParseException {
        if ( expression.equalsIgnoreCase("xpath-escape") ) { 
            return forXPath(data);
        } else  if ( expression.equalsIgnoreCase("url-encode") ) { 
            try { 
                return URLEncoder.encode(data,"UTF-8");
            } catch (UnsupportedEncodingException e) { 
                throw new ParseException("Unable to url encode.",e);
            }
        } else  if ( expression.equalsIgnoreCase("url-decode") ) { 
            try { 
                return URLDecoder.decode(data,"UTF-8");
            } catch (UnsupportedEncodingException e) { 
                throw new ParseException("Unable to url decode.",e);
            }
        } else { 
            throw new ParseException("Unsupported conversion [" + expression + "]");
        }
    }
    
    private static int indexOf(String string, char[] chars) { 
        for (int i = 0; i < chars.length; i++) { 
            int index = string.indexOf(chars[i]);
            if ( index != -1 ) 
                return index;
        }
        return -1;
    }
   
    // wacky things that have to be done to protect strings with quotes in 
    // xpath!
    private static String forXPath(String string) {
        StringBuffer returnString = null;
        String searchString = string;

        char[] quoteChars = new char[] { '\'', '"' };

        int quotePos = indexOf(searchString, quoteChars);
        if (quotePos == -1) {
            returnString = new StringBuffer("'" + searchString + "'");
        } else {
            returnString = new StringBuffer("concat(");
            while (quotePos != -1) {
                String subString = searchString.substring(0, quotePos);
                returnString.append("'" + subString + "', ");
                if (searchString.charAt(quotePos) == '\'') {
                    returnString.append("\"'\", ");
                } else {
                    // must be a double quote
                    returnString.append("'\"', ");
                }
                searchString = searchString.substring(quotePos + 1);
                quotePos = indexOf(searchString, quoteChars);
            }
            returnString.append("'" + searchString + "')");
        }
        return returnString.toString();
    }
}
