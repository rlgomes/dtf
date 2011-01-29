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
 * @dtf.tag.desc This tag defines the cookie to be used for a specific HTTP 
 *               operation. You have the ability to the define the name, value
 *               and then a few other important attributes.
 * 
 * @dtf.tag.example
 *  <http_put uri="http://127.0.0.1:80/echo-data">
 *      <cookie name="cookie1" value="value1" path="/someplace" />
 *      <cookie name="cookie2" value="value2" path="/anotherplace" />
 *      <header name="header1" value="value1"/>
 *      <entity value="SOME DATA FROM THE CLIENT"/>
 *  </http_put> 
 */
public class Cookie extends Action {
  
    /**
     * @dtf.attr.name name
     * @dtf.attr.desc The name of the cookie being specified.
     */
    private String name = null;

    /**
     * @dtf.attr.name value
     * @dtf.attr.desc The value of the cookie being specified.
     */
    private String value = null;
   
    /**
     * @dtf.attr.name domain
     * @dtf.attr.desc The cookie domain and path define the scope of the cookie,
     *                they tell the client that they should only be sent back 
     *                to the server for the given domain and path. If not 
     *                specified, they default to the domain and path of the 
     *                object that was requested. 
     */
    private String domain = null;

    /**
     * @dtf.attr.name path
     * @dtf.attr.desc The cookie domain and path define the scope of the cookie,
     *                they tell the client that they should only be sent back 
     *                to the server for the given domain and path. If not 
     *                specified, they default to the domain and path of the 
     *                object that was requested.
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
     * @dtf.attr.desc Just specify if the cookie is secure and should only be
     *                used on HTTPS connections. 
     */
    private String secure = null;

    /**
     * @dtf.attr.name expirydate
     * @dtf.attr.desc This identifies the date after which the HTTP client should
     *                delete this cookie and no longer send it with any HTTP 
     *                request.
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
