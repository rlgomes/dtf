package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.ParseException;

public abstract class SeleniumGetStateLocatorTag extends SeleniumGetStateTag {
    
    private String locator = null;
   
    public String getLocator() throws ParseException {
        return replaceProperties(locator);
    }
    
    public void setLocator(String locator) {
        this.locator = locator;
    }
}
