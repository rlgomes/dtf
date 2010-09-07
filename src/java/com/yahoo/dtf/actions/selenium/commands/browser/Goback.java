package com.yahoo.dtf.actions.selenium.commands.browser;

import com.yahoo.dtf.actions.selenium.Selenium;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag goBack
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               This command allows you to go back in your browsers history to
 *               the previously loaded page.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.yahoo.com" browser="*${browser}">
 *      <open url="/finance"/>
 *      <waitForPageToLoad timeout="30000"/>
 *      <goBack/>
 *  </selenium>
 */
public class Goback extends Selenium {
    
    @Override
    public void execute() throws DTFException {
        getSelenium().goBack();
    }
}
