package com.yahoo.dtf.actions.selenium.commands.browser;

import com.yahoo.dtf.actions.selenium.Selenium;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag windowMaximize
 * @dtf.skip.index
 *  
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               This command will simply try to maximize the currently focused
 *               window.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.yahoo.com" browser="*${browser}">
 *      <open url="/finance"/>
 *      <waitForPageToLoad timeout="30000"/>
 *      <maximizeWindow/>
 *  </selenium>
 */
public class Windowmaximize extends Selenium {
    
    @Override
    public void execute() throws DTFException {
        getSelenium().windowMaximize();
    }
}
