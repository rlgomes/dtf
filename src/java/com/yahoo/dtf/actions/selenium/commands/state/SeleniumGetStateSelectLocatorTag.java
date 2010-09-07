package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.ParseException;

public abstract class SeleniumGetStateSelectLocatorTag extends SeleniumGetStateTag {
    
    /**
     * @dtf.attr selectLocator
     * @dtf.attr.desc an {@dtf.link Element Locator} identifying a drop-down menu
     */
    private String selectLocator = null;
   
    public String getSelectLocator() throws ParseException {
        return replaceProperties(selectLocator);
    }
    
    public void setSelectLocator(String selectLocator) {
        this.selectLocator = selectLocator;
    }
}
