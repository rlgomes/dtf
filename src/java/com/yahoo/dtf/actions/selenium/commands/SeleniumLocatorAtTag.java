package com.yahoo.dtf.actions.selenium.commands;

import com.yahoo.dtf.exception.ParseException;

public class SeleniumLocatorAtTag extends SeleniumLocatorTag {
   
    /**
     * @dtf.attr coordString
     * @dtf.attr.desc specifies the x,y position (i.e. - 10,20) of the mouse 
     *                event relative to the element returned by the locator.
     */
    private String coordString = null;
    
    public String getCoordString() throws ParseException {
        return replaceProperties(coordString);
    }
    
    public void setCoordString(String coordString) {
        this.coordString = coordString;
    }
    
}
