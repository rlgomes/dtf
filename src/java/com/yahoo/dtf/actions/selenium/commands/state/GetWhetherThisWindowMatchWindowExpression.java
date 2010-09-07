package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag getWhetherThisWindowMatchWindowExpression
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Determine whether currentWindowString plus target identify the 
 *               window containing this running code. This is useful in proxy 
 *               injection mode, where this code runs in every browser frame 
 *               and window, and sometimes the selenium server needs to identify
 *               the "current" window. In this case, when the test calls 
 *               selectWindow, this routine is called for each window to figure
 *               out which one has been selected. The selected window will 
 *               return true, while all others will return false. 
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.yahoo.com">
 *      <open url="/finance"/>
 *      <getWhetherThisWindowMatchWindowExpression />
 *  </selenium>
 */
public class GetWhetherThisWindowMatchWindowExpression extends SeleniumGetStateLocatorTag {
    
    /**
     * @dtf.attr currentWindowString
     * @dtf.attr.desc starting window
     */
    private String currentWindowString = null;
    
    /**
     * @dtf.attr target
     * @dtf.attr.desc new window (which might be relative to the current one, 
     *                e.g., "_parent")
     */
    private String target = null;
    
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().
            getWhetherThisWindowMatchWindowExpression(getCurrentWindowString(),
                                                      getTarget());
    }
    
    public String getCurrentWindowString() throws ParseException {
        return replaceProperties(currentWindowString);
    }
    
    public void setCurrentWindowString(String currentWindowString) {
        this.currentWindowString = currentWindowString;
    }
    
    public String getTarget() throws ParseException {
        return replaceProperties(target);
    }
    
    public void setTarget(String target) {
        this.target = target;
    }
}
