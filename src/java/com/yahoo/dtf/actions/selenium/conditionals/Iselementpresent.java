package com.yahoo.dtf.actions.selenium.conditionals;

import com.yahoo.dtf.actions.conditionals.Condition;
import com.yahoo.dtf.actions.conditionals.Conditional;
import com.yahoo.dtf.actions.selenium.commands.SeleniumLocatorTag;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag isElementPresent
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Verifies that the specified element is somewhere on the page.
 *               </p>
 * 
 * @dtf.tag.example
 *  <selenium baseurl="http://www.google.com">
 *      <open url="/"/>
 *      <waitForPageToLoad timeout="30000"/>
 *      <assert><isElementPresent locator="q"/></assert>
 *  </selenium>
 * 
 */
public class Iselementpresent extends SeleniumLocatorTag implements Conditional {
    
    @Override
    public boolean evaluate() throws DTFException {
        if (!getSelenium().isElementPresent(getLocator()) ) { 
            String msg = getLocator() + " not found on page.";
            registerContext(Condition.ASSERT_EXP_CTX, msg); 
            return false;
        }
        
        return true;
    }
    
    /**
     * By registering the context ASSERT_EXP_CTX you can set the message to 
     * be returned explaining the failure to assert a condition.
     */
    public String explanation() throws DTFException {
        return getContext(Condition.ASSERT_EXP_CTX).toString();
    }
}
