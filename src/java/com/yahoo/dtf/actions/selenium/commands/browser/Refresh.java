package com.yahoo.dtf.actions.selenium.commands.browser;

import com.yahoo.dtf.actions.selenium.Selenium;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag refresh
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               This command allows you to refresh the the currently focused
 *               window.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.yahoo.com" browser="*${browser}">
 *      <open url="/finance"/>
 *      <waitForPageToLoad timeout="30000"/>
 *      <refresh/>
 *      <waitForPageToLoad timeout="30000"/>
 *  </selenium>
 */
public class Refresh extends Selenium {
    
    @Override
    public void execute() throws DTFException {
        getSelenium().refresh();
    }
}
