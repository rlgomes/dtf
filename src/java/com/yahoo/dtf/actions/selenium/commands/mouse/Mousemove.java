package com.yahoo.dtf.actions.selenium.commands.mouse;

import com.yahoo.dtf.actions.selenium.commands.SeleniumLocatorTag;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag mouseMove
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               This command simulates a mouse move event at the location 
 *               specified by the locator attribute using the 
 *               {@dtf.link Element Locator} syntax.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://someplace.com" browser="*firefox">
 *      <open url="/"/>
 *      <mouseMove locator="//input[@name='test']"/>
 *  </selenium>
 */
public class Mousemove extends SeleniumLocatorTag {
    
    @Override
    public void execute() throws DTFException {
        getSelenium().mouseMove(getLocator());
    }
}
