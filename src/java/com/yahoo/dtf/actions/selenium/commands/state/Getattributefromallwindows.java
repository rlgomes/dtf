package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag getAttributeFromAllWindows
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Returns every instance of some attribute from all known windows.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.yahoo.com" browser="*firefox">
 *      <open url="/"/>
 *      <getAttributeFromAllWindows property="search_id" attributeName="id"/>
 *  </selenium>
 */
public class Getattributefromallwindows extends SeleniumGetStateTag {

    /**
     * @dtf.attr attributeName
     * @dtf.attr.desc name of an attribute on the windows.
     */
    private String attributeName = null;
    
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().getAttributeFromAllWindows(getAttributeName());
    }
    
    public String getAttributeName() throws ParseException {
        return replaceProperties(attributeName);
    }
    
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
}
