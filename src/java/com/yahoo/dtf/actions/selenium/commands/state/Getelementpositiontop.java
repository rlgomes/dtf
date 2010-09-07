package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag getElementPositionTop
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Retrieves the vertical position of an element.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://someplace.com">
 *      <open url="/"/>
 *      <getElementPositionTop locator="//button[@name='Search']"
 *                             property="search.button.top"/>
 *  </selenium>
 */
public class Getelementpositiontop extends SeleniumGetStateLocatorTag {
   
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().getElementPositionTop(getLocator());
    }
}
