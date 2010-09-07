package com.yahoo.dtf.actions.selenium.commands.selection;

import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag removeSelection
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Remove a selectiona from the set of selected options in a 
 *               multi-select element using an option locator.  
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://someplace.com" browser="*firefox">
 *      <open url="/"/>
 *      <removeSelection locator="//input[@name='test']" optionLocator="test2"/>
 *  </selenium>
 */
public class Removeselection extends Addselection {
   
    @Override
    public void execute() throws DTFException {
        getSelenium().removeSelection(getLocator(), getOptionLocator());
    }
}
