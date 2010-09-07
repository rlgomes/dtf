package com.yahoo.dtf.actions.http.config;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag proxy
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc this tag simply holds the information for proxying for http 
 *               requests.
 * 
 */               
public class Proxy extends Action {
  
    /**
     * @dtf.attr host
     * @dtf.attr.desc
     */
    private String host = null;
    
    /**
     * @dtf.attr port
     * @dtf.attr.desc
     */
    private String port = null;
     
    /**
     * @dtf.attr username
     * @dtf.attr.desc
     */
    private String username = null;
    
    /**
     * @dtf.attr password
     * @dtf.attr.desc
     */
    private String password = null;
 
    @Override
    public void execute() throws DTFException {
        
    }
    
    public String getHost() throws ParseException { return replaceProperties(host); }
    public void setHost(String host) { this.host = host; }

    public int getPort() throws ParseException { return toInt("port",port); }
    public void setPort(String port) { this.port = port; }

    public String getUsername() throws ParseException { return replaceProperties(username); }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() throws ParseException { return replaceProperties(password); }
    public void setPassword(String password) { this.password = password; }
}
