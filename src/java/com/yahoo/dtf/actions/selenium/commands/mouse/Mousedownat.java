package com.yahoo.dtf.actions.selenium.commands.mouse;

import com.yahoo.dtf.actions.selenium.commands.SeleniumLocatorAtTag;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag mouseDownAt
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               This command simulates a mouse down event at the location 
 *               specified by the locator attribute using the 
 *               {@dtf.link Element Locator} syntax. The location of the click 
 *               can also be influenced by using the coordString to identify the
 *               specific position within the element identified by the locator.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.google.com" browser="*firefox">
 *      <open url="/"/>
 *      <mouseDownAt locator="//input[@value='q']" coordString="0,0"/>
 *  </selenium>
 */
public class Mousedownat extends SeleniumLocatorAtTag {
    
    @Override
    public void execute() throws DTFException {
        getSelenium().mouseDownAt(getLocator(), getCoordString());
    }
}
