package com.yahoo.dtf.actions.selenium.commands.cookie;

import com.yahoo.dtf.actions.selenium.Selenium;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag createCookie
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               The create cookie command allows you to add new cookies to your
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
public class Createcookie extends Selenium {
   
    /**
     * @dtf.attr nameValuePair
     * @dtf.attr.desc the name,value pair in the format 'name=value'
     */
    private String nameValuePair = null;
    
    /**
     * @dtf.attr optionsString
     * @dtf.attr.desc options for the cookie. Currently supported options 
     *                include 'path' and 'max_age'. the optionsString's format
     *                is "path=/path/, max_age=60". The order of options are 
     *                irrelevant, the unit of the value of 'max_age' is second.
     */
    private String optionsString = null;
    
    @Override
    public void execute() throws DTFException {
        getSelenium().createCookie(getNameValuePair(), getOptionsString());
    }
    
    public String getNameValuePair() throws ParseException {
        return replaceProperties(nameValuePair);
    }
    
    public void setNameValuePair(String nameValuePair) {
        this.nameValuePair = nameValuePair;
    }
    
    public String getOptionsString() throws ParseException {
        return replaceProperties(optionsString);
    }
    
    public void setOptionsString(String optionsString) {
        this.optionsString = optionsString;
    }
   
}
