package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag getElementHeight
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Retrieve the height of the element specified by the locator
 *               attribute.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://someplace.com">
 *      <open url="/"/>
 *      <getElementHeight locator="//button[@name='Search']"
 *                        property="search.button.height"/>
 *  </selenium>
 */
public class Getelementheight extends SeleniumGetStateLocatorTag {
   
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().getElementHeight(getLocator());
    }
}
