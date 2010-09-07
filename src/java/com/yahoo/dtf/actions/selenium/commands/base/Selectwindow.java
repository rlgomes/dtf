package com.yahoo.dtf.actions.selenium.commands.base;

import com.yahoo.dtf.actions.selenium.commands.SeleniumLocatorTag;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag selectWindow
 * @dtf.skip.index
 *  
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Changes the current Selenium session focus to the window 
 *               identified by the windowID attribute which will then receive
 *               the following executing commands.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.google.com">
 *      <openWindow url="/finance" windowID="test"/>
 *      <selectWindow windowID="test"/>
 *      <waitForPageToLoad timeout="30000"/>
 *  </selenium>
 */
public class Selectwindow extends SeleniumLocatorTag {
    
    /**
     * @dtf.attr windowId
     * @dtf.attr.desc id of the window previously opened with the 
     *                {@dtf.link openWindow} tag.
     */
    private String windowID = null;

    @Override
    public void execute() throws DTFException {
        getSelenium().selectWindow(getWindowID());
    }
    
    public String getWindowID() throws ParseException {
        return replaceProperties(windowID);
    }
    
    public void setWindowID(String windowID) {
        this.windowID = windowID;
    }

}
