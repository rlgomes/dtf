package com.yahoo.dtf.actions.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag urlencode
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc This tag is used to encode URL paths so that they can be 
 *               correctly used in the http_tags. Only use this tag on the path
 *               part of a URL and do not encode the http:/hostname part.
 * 
 * @dtf.tag.example 
 * 
 */
public class Urlencode extends Action {
  
    /**
     * @dtf.attr.name source
     * @dtf.attr.desc data source to url encode.
     */
    private String source = null;

    /**
     * @dtf.attr.name result
     * @dtf.attr.desc property name to put the result of encoding the source
     *                attribute.
     */
    private String result = null;
    
    public void execute() throws DTFException {
        try {
            getConfig().setProperty(getResult(), URLEncoder.encode(getSource(),"UTF-8"), true);
        } catch (UnsupportedEncodingException e) {
            throw new DTFException("Unable to encode [" + getSource() + "]",e);
        }
    }
    
    public String getSource() throws ParseException { 
        return replaceProperties(source);
    }
    public void setSource(String source) { this.source = source; } 

    public String getResult() throws ParseException { 
        return replaceProperties(result);
    }
    public void setResult(String result) { this.result = result; }
}
