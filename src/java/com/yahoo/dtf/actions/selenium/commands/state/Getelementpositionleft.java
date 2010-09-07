package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag getElementPositionLeft
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Retrieves the horizontal position of an element.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://someplace.com">
 *      <open url="/"/>
 *      <getElementPositionLeft locator="//button[@name='Search']"
 *                              property="search.button.left"/>
 *  </selenium>
 */
public class Getelementpositionleft extends SeleniumGetStateLocatorTag {
   
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().getElementPositionLeft(getLocator());
    }
}
