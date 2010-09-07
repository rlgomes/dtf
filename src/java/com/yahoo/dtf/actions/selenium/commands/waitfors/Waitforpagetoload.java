package com.yahoo.dtf.actions.selenium.commands.waitfors;

import com.yahoo.dtf.actions.selenium.commands.SeleniumTimeoutTag;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag waitForPageToLoad
 * @dtf.skip.index
 *  
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Simple command that can wait for the currently loading page to
 *               finish loading before proceeding with the rest of the 
 *               Selenium interactions.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.yahoo.com" browser="*${browser}">
 *      <open url="/"/>
 *      <waitForPageToLoad timeout="30000"/>
 *  </selenium>
 */
public class Waitforpagetoload extends SeleniumTimeoutTag {
   

    @Override
    public void execute() throws DTFException {
        getSelenium().waitForPageToLoad(getTimeout());
    }
}
