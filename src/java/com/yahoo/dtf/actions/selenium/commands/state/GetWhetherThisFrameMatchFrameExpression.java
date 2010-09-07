package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag getWhetherThisFrameMatchFrameExpression
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Determine whether current/locator identify the frame containing
 *               this running code. This is useful in proxy injection mode, 
 *               where this code runs in every browser frame and window, and 
 *               sometimes the selenium server needs to identify the "current" 
 *               frame. In this case, when the test calls selectFrame, this 
 *               routine is called for each frame to figure out which one has 
 *               been selected. The selected frame will return true, while all 
 *               others will return false.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.yahoo.com">
 *      <open url="/finance"/>
 *      <getWhetherThisFrameMatchFrameExpression />
 *  </selenium>
 */
public class GetWhetherThisFrameMatchFrameExpression extends SeleniumGetStateLocatorTag {
   
    /**
     * @dtf.attr currentFrameString
     * @dtf.attr.desc starting frame
     */
    private String currentFrameString = null;
    
    /**
     * @dtf.attr target
     * @dtf.attr.desc new frame (which might be relative to the current one).
     */
    private String target = null;
    
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().
            getWhetherThisFrameMatchFrameExpression(getCurrentFrameString(),
                                                    getTarget());
    }
    
    public String getCurrentFrameString() throws ParseException {
        return replaceProperties(currentFrameString);
    }
    
    public void setCurrentFrameString(String currentFrameString) {
        this.currentFrameString = currentFrameString;
    }
    
    public String getTarget() throws ParseException {
        return replaceProperties(target);
    }
    
    public void setTarget(String target) {
        this.target = target;
    }
}
