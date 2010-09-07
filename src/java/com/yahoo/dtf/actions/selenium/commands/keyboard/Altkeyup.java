package com.yahoo.dtf.actions.selenium.commands.keyboard;

import com.yahoo.dtf.actions.selenium.Selenium;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag altKeyUp
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>This command simulates holding the alt key up.</p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.yahoo.com" browser="*firefox">
 *      <open url="/"/>
 *      <altKeyUp/>
 *  </selenium>
 */
public class Altkeyup extends Selenium {
    
    @Override
    public void execute() throws DTFException {
        getSelenium().altKeyUp();
    }
}
