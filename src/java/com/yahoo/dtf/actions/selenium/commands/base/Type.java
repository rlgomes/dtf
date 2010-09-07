package com.yahoo.dtf.actions.selenium.commands.base;

import com.yahoo.dtf.actions.selenium.commands.SeleniumLocatorTag;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag type
 * @dtf.skip.index
 *  
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               The type command is used to fill in any input element in HTML
 *               that can be identified with the locator 
 *               {@dtf.link Element Locator} attribute. This allows you to 
 *               simulate the filling out of forms or other textual elements
 *               with ease. 
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.yahoo.com" browser="*${browser}">
 *      <open url="/"/>
 *      <waitForPageToLoad timeout="30000"/>
 *      <windowMaximize/>
 *      <type locator="p" value="finance"/>
 *      <click locator="//button[@value='Web Search']"/>
 *      <waitForPageToLoad timeout="30000"/>
 *  </selenium>
 */
public class Type extends SeleniumLocatorTag {
   
    /**
     * @dtf.attr value
     * @dtf.attr.desc the value that will be typed into the input element 
     *                identified by the locator attribute. 
     */
    private String value = null;

    @Override
    public void execute() throws DTFException {
        getSelenium().type(getLocator(), getValue());
    }
    
    public String getValue() throws ParseException {
        return replaceProperties(value);
    }
    
    public void setValue(String value) {
        this.value = value;
    }

}
