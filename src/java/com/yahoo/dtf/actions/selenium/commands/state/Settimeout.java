package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.actions.selenium.Selenium;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag setTimeout
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Specifies the amount of time that Selenium will wait for 
 *               actions to complete. Actions that require waiting include 
 *               "open" and the "waitFor*" actions. The default timeout is 
 *               30 seconds.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.google.com">
 *      <open url="/"/>
 *      <setTimeout timeout="120000"/>
 *  </selenium>
 */
public class Settimeout extends Selenium {

    /**
     * @dtf.attr timeout
     * @dtf.attr.desc a timeout in milliseconds, after which the action will 
     *                return with an error
     */
    private String timeout = null;
   
    @Override
    public void execute() throws DTFException {
        getSelenium().setTimeout(getTimeout());
    }
    
    public String getTimeout() throws ParseException {
        return replaceProperties(timeout);
    }
    
    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }
}
