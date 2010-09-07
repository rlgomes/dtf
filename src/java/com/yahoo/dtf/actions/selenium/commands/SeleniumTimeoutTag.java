package com.yahoo.dtf.actions.selenium.commands;

import com.yahoo.dtf.actions.selenium.Selenium;
import com.yahoo.dtf.exception.ParseException;

public class SeleniumTimeoutTag extends Selenium {
    
    /**
     * @dtf.attr timeout
     * @dtf.attr.desc the amount of time in milliseconds to wait for a page to
     *                load after which an error will be thrown and the 
     *                current execution would terminate.
     */
    private String timeout = null;
    
    public String getTimeout() throws ParseException {
        return replaceProperties(timeout);
    }
    
    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }
}
