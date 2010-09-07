package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag getEval
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Gets the result of evaluating the specified JavaScript snippet.
 *               The snippet may have multiple lines, but only the result of 
 *               the last line will be returned. Note that, by default, the 
 *               snippet will run in the context of the "selenium" object 
 *               itself, so this will refer to the Selenium object, and window
 *               will refer to the top-level runner test window, not the window 
 *               of your application.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.google.com">
 *      <open url="/"/>
 *      <waitForPageToLoad timeout="30000"/>
 *      <open url="/finance"/>
 *      <waitForPageToLoad timeout="30000"/>
 *      <getEval script="history.go(-1)"/>
 *      <waitForPageToLoad timeout="30000"/>
 *  </selenium>
 */
public class Geteval extends SeleniumGetStateTag {
  
    private String script = null;
    
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().getEval(getScript());
    }
   
    public String getScript() throws ParseException {
        return replaceProperties(script);
    }
    
    public void setScript(String script) {
        this.script = script;
    }
}
