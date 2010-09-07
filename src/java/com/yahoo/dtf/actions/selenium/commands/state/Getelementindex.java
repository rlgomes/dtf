package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag getElementIndex
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Get the relative index of an element to its parent (starting 
 *               from 0). The comment node and empty text node will be ignored.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://someplace.com">
 *      <open url="/"/>
 *      <getElementIndex locator="//button[@name='Search']"
 *                       property="index"/>
 *  </selenium>
 */
public class Getelementindex extends SeleniumGetStateLocatorTag {
   
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().getElementIndex(getLocator());
    }
}
