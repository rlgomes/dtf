package com.yahoo.dtf.actions.selenium.commands.waitfors;

import com.yahoo.dtf.actions.selenium.commands.SeleniumTimeoutTag;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag waitForFrameToLoad
 * @dtf.skip.index
 *  
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Waits for a new frame to load. Selenium constantly keeps track
 *               of new pages and frames loading, and sets a "newPageLoaded" 
 *               flag when it first notices a page load. See waitForPageToLoad 
 *               for more information.
 *               </p>
 */
public class Waitforframetoload extends SeleniumTimeoutTag {
   
    /**
     * @dtf.attr frameAddress
     * @dtf.attr.desc frameAddress from the server side
     */
    private String frameAddress = null;

    @Override
    public void execute() throws DTFException {
        getSelenium().waitForFrameToLoad(getFrameAddress(), getTimeout());
    }
    
    public String getFrameAddress() throws ParseException {
        return replaceProperties(frameAddress);
    }
    
    public void setFrameAddress(String frameAddress) {
        this.frameAddress = frameAddress;
    }
}
