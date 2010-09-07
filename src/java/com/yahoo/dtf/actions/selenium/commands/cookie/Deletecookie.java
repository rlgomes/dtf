package com.yahoo.dtf.actions.selenium.commands.cookie;

import com.yahoo.dtf.actions.selenium.Selenium;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag deleteCookie
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               This command allows you to remove cookies by name from your 
 *               Selenium session.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.yahoo.com" browser="*firefox">
 *      <open url="/"/>
 *      <waitForPageToLoad timeout="30000"/>
 *      <createCookie nameValuePair="C=D" optionsString="path=/"/>
 *      <deleteCookie name="C=D" path="/"/>
 *  </selenium> 
 */
public class Deletecookie extends Selenium {
    
    /**
     * @dtf.attr name
     * @dtf.attr.desc the name of the cookie to be deleted.
     */
    private String name = null;
   
    /**
     * @dtf.attr path
     * @dtf.attr.desc the path property of the cookie to be deleted.
     */
    private String path = null;
    
    @Override
    public void execute() throws DTFException {
        getSelenium().deleteCookie(getName(),getPath());
    }
    
    public String getName() throws ParseException {
        return replaceProperties(name);
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPath() throws ParseException {
        return replaceProperties(path);
    }
    
    public void setPath(String path) {
        this.path = path;
    }
   
}
