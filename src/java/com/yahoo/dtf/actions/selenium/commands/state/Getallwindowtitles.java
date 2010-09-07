package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag getAllWindowTitles
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Returns the titles of all windows that the browser knows about.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.yahoo.com" browser="*firefox">
 *      <open url="/"/>
 *      <getAllWindowsTitles property="window.titles"/>
 *  </selenium>
 */
public class Getallwindowtitles extends SeleniumGetStateTag {
    
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().getAllWindowTitles();
    }
}
