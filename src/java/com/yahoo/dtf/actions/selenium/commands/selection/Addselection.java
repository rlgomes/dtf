package com.yahoo.dtf.actions.selenium.commands.selection;

import com.yahoo.dtf.actions.selenium.commands.SeleniumLocatorTag;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag addSelection
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Add a selection to the set of selected options in a 
 *               multi-select element using an option locator.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://someplace.com" browser="*firefox">
 *      <open url="/"/>
 *      <addSelection locator="//input[@name='test']" optionLocator="test"/>
 *  </selenium>
 */
public class Addselection extends SeleniumLocatorTag {
   
    /**
     * @dtf.attr optionLocator
     * @dtf.attr.desc an {@dtf.link Option Locator} that identifies which option
     *                to pick from the multi-select element.
     */
    private String optionLocator = null;
    
    @Override
    public void execute() throws DTFException {
        getSelenium().addSelection(getLocator(), getOptionLocator());
    }
    
    public String getOptionLocator() throws ParseException {
        return replaceProperties(optionLocator);
    }
    
    public void setOptionLocator(String optionLocator) {
        this.optionLocator = optionLocator;
    }
}
