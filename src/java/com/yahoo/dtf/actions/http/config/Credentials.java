package com.yahoo.dtf.actions.http.config;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag credentials
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc 
 *               
 * 
 */               
public class Credentials extends Action {
  
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
    public String getUsername() throws ParseException { return replaceProperties(username); }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() throws ParseException { return replaceProperties(password); }
    public void setPassword(String password) { this.password = password; }
}
