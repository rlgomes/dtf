package com.yahoo.dtf.actions.selenium.commands.base;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag openWindow
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               opens a new window with the url specified and with a specific
 *               windowID to be used. With this windowID you can then manipulate
 *               different windows at the same time.
 *               </p>
 * 
 * @dtf.tag.example 
 * <selenium baseurl="http://www.google.com" browser="*firefox">
 *     <openWindow url="/" windowID="w1"/>
 * </selenium>
 */
public class Openwindow extends Open {
   
    /**
     * @dtf.attr windowID
     * @dtf.attr.desc the window id to be used when opening this new window.
     */
    private String windowID = null;

    @Override
    public void execute() throws DTFException {
        getSelenium().openWindow(getUrl(),getWindowID());
    }
    
    public String getWindowID() throws ParseException {
        return replaceProperties(windowID);
    }
    
    public void setWindowID(String windowID) {
        this.windowID = windowID;
    }
}
