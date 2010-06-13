package com.yahoo.dtf.actions.http.cookies;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag cookie
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc 
 * 
 * @dtf.tag.example 
 * 
 */
public class Cookie extends Action {
  
    /**
     * @dtf.attr.name name
     * @dtf.attr.desc the name of the cookie being specified.
     */
    private String name = null;

    /**
     * @dtf.attr.name value
     * @dtf.attr.desc the value of the cookie being specified.
     */
    private String value = null;
   
    /**
     * @dtf.attr.name domain
     * @dtf.attr.desc 
     */
    private String domain = null;

    /**
     * @dtf.attr.name path
     * @dtf.attr.desc 
     */
    private String path = null;

    /**
     * @dtf.attr.name comment
     * @dtf.attr.desc 
     */
    private String comment = null;

    /**
     * @dtf.attr.name version
     * @dtf.attr.desc 
     */
    private String version = null;

    /**
     * @dtf.attr.name secure
     * @dtf.attr.desc 
     */
    private String secure = null;

    /**
     * @dtf.attr.name expirydate
     * @dtf.attr.desc 
     */
    private String expirydate = null;
    
    
    public void execute() throws DTFException {
        // nothing to do.
    }
    
    public String getName() throws ParseException {  return replaceProperties(name); }
    public void setName(String name) { this.name = name; }

    public String getValue() throws ParseException {  return replaceProperties(value); }
    public void setValue(String value) { this.value = value; }

    public String getDomain() throws ParseException {  return replaceProperties(domain); }
    public void setDomain(String domain) { this.domain = domain; }

    public String getPath() throws ParseException {  return replaceProperties(path); }
    public void setPath(String path) { this.path = path; }

    public String getComment() throws ParseException { return replaceProperties(comment); }
    public void setComment(String comment) { this.comment = comment; }

    public int getVersion() throws ParseException { return toInt("version",version); }
    public void setVersion(String version) { this.version = version; }

    public boolean getSecure() throws ParseException { return toBoolean("secure",secure); }
    public void setSecure(String secure) { this.secure = secure; }
   
    public Date getExpirydate() throws ParseException { 
        if ( expirydate == null ) 
            return null;
        
        try {
            SimpleDateFormat sdf = 
                           new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            return sdf.parse(replaceProperties(expirydate));
        } catch (java.text.ParseException e) {
            throw new ParseException("Invalid date format.",e);
        } 
    }
    public void setExpirydate(String expirydate) { this.expirydate = expirydate; }
}
