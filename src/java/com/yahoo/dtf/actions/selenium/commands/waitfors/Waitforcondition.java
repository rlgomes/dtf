package com.yahoo.dtf.actions.selenium.commands.waitfors;

import com.yahoo.dtf.actions.selenium.commands.SeleniumTimeoutTag;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag watiForCondition
 * @dtf.skip.index
 *  
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Runs the specified JavaScript snippet repeatedly until it 
 *               evaluates to "true". The snippet may have multiple lines, but 
 *               only the result of the last line will be considered.
 *               </p>
 *               <p>
 *               Note that, by default, the snippet will be run in the runner's
 *               test window, not in the window of your application. To get the 
 *               window of your application, you can use the JavaScript snippet 
 *               selenium.browserbot.getCurrentWindow(), and then run your 
 *               JavaScript in there
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.yahoo.com" browser="*${browser}">
 *      <open url="/"/>
 *      <refresh/>
 *      <!-- like a waitForPageToLoad but done with javascript -->
 *      <waitForCondition script="selenium.browserbot.getCurrentWindow().document.getElementById('search-submit')"
 *                        timeout="30000"/>
 *  </selenium>
 */
public class Waitforcondition extends SeleniumTimeoutTag {
    
    /**
     * @dtf.attr script
     * @dtf.attr.desc the javascript to execute in the browser 
     */
    private String script = null;


    @Override
    public void execute() throws DTFException {
        getSelenium().waitForCondition(getScript(), getTimeout());
    }
    
    public String getScript() throws ParseException {
        return replaceProperties(script);
    }
    
    public void setScript(String script) {
        this.script = script;
    }
}
