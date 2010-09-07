package com.yahoo.dtf.actions.selenium.commands.selection;

import com.yahoo.dtf.actions.selenium.commands.SeleniumLocatorTag;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag removeAllSelections
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Remove all selections from the set of selected options in a 
 *               multi-select element using an option locator.  
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://someplace.com" browser="*firefox">
 *      <open url="/"/>
 *      <removeAllSelections locator="//input[@name='test']"/>
 *  </selenium>
 */
public class Removeallselections extends SeleniumLocatorTag {
   
    @Override
    public void execute() throws DTFException {
        getSelenium().removeAllSelections(getLocator());
    }
}
