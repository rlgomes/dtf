package com.yahoo.dtf.actions.selenium.commands;

import com.yahoo.dtf.actions.selenium.Selenium;
import com.yahoo.dtf.exception.ParseException;

public class SeleniumLocatorTag extends Selenium {
    
    /**
     * @dtf.attr locator
     * @dtf.attr.desc {@dtf.link Element Locator} to identify the html element.
     */
    private String locator = null;
    
    public String getLocator() throws ParseException {
        return replaceProperties(locator);
    }
    
    public void setLocator(String locator) {
        this.locator = locator;
    }
    
}
