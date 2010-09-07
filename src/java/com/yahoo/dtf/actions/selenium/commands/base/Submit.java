package com.yahoo.dtf.actions.selenium.commands.base;

import com.yahoo.dtf.actions.selenium.commands.SeleniumLocatorTag;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag submit
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Submit the specified form. This is particularly useful for 
 *               forms without submit buttons, e.g. single-input "Search" forms. 
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://someplace.com">
 *      <open url="/"/>
 *      <submit locator="//form[@id='login']"/>
 *  </selenium>
 */
public class Submit extends SeleniumLocatorTag {
    
    @Override
    public void execute() throws DTFException {
        getSelenium().submit(getLocator());
    }
}
