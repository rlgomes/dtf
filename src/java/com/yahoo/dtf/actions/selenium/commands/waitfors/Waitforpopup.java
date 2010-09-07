package com.yahoo.dtf.actions.selenium.commands.waitfors;

import com.yahoo.dtf.actions.selenium.commands.SeleniumTimeoutTag;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag waitForPopup
 * @dtf.skip.index
 *  
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Waits for a popup window to appear and load up.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.yahoo.com">
 *      <open url="/"/>
 *      <openWindow url="http://www.yahoo.com/finance" windowID="other"/>
 *      <waitForPopUp windowID="other" timeout="30000"/>
 *  </selenium>
 */
public class Waitforpopup extends SeleniumTimeoutTag {
   
    /**
     * @dtf.attr windowID
     * @dtf.attr.desc the JavaScript window "name" of the window that will 
     *                appear (not the text of the title bar) If unspecified, 
     *                or specified as "null", this command will wait for the 
     *                first non-top window to appear (don't rely on this if you 
     *                are working with multiple popups simultaneously).
     */
    private String windowID = null;
    
    @Override
    public void execute() throws DTFException {
        getSelenium().waitForPopUp(getWindowid(), getTimeout());
    }
   
    public String getWindowid() throws ParseException {
        return replaceProperties(windowID);
    }
    
    public void setWindowid(String windowID) {
        this.windowID = windowID;
    }
}
