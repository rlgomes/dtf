package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag getAttribute
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Gets the value of an element attribute.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.yahoo.com" browser="*firefox">
 *      <open url="/"/>
 *      <getAttribute property="search_id"
 *                    attributeLocator="//button[@value='Web Search']@id"/>
 *  </selenium>
 */
public class Getattribute extends SeleniumGetStateTag {

    /**
     * @dtf.attr attributeLocator
     * @dtf.attr.desc an element locator followed by an @ sign and then the 
     *                name of the attribute, e.g. "foo@bar".
     */
    private String attributeLocator = null;
    
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().getAttribute(getAttributeLocator());
    }
    
    public String getAttributeLocator() throws ParseException {
        return replaceProperties(attributeLocator);
    }
    
    public void setAttributeLocator(String attributeLocator) {
        this.attributeLocator = attributeLocator;
    }
}
