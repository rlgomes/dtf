package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.actions.selenium.Selenium;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag setContext
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Writes a message to the status bar and adds a note to the 
 *               browser-side log.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.google.com">
 *      <open url="/"/>
 *      <setContext context="step #3"/>
 *  </selenium>
 */
public class Setcontext extends Selenium {
 
    /**
     * @dtf.attr context
     * @dtf.attr.desc the message to be sent to the browser
     */
    private String context = null;
   
    @Override
    public void execute() throws DTFException {
        getSelenium().setContext(getContext());
    }
    
    public String getContext() throws ParseException {
        return replaceProperties(context);
    }
    
    public void setContext(String context) {
        this.context = context;
    }
}
