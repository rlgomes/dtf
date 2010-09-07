package com.yahoo.dtf.actions.selenium.commands.base;

import com.yahoo.dtf.actions.selenium.Selenium;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag close
 * @dtf.skip.index
 *  
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               </p>
 * 
 * @dtf.tag.example 
 * <selenium baseurl="http://www.google.com" browser="*firefox">
 *     <open url="/"/>
 * </selenium>
 */
public class Close extends Selenium {

    @Override
    public void execute() throws DTFException {
        getSelenium().close();
    }
}
