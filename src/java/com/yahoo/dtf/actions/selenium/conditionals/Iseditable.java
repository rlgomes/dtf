package com.yahoo.dtf.actions.selenium.conditionals;

import com.yahoo.dtf.actions.conditionals.Condition;
import com.yahoo.dtf.actions.conditionals.Conditional;
import com.yahoo.dtf.actions.selenium.commands.SeleniumLocatorTag;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag isEditable
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Determines whether the specified input element is editable, ie
 *               hasn't been disabled. This method will fail if the specified 
 *               element isn't an input element.
 *               </p>
 * 
 * @dtf.tag.example
 *  <selenium baseurl="http://www.google.com">
 *      <open url="/"/>
 *      <waitForPageToLoad timeout="30000"/>
 *      <assert><isEditable locator="q"/></assert>
 *  </selenium>
 */
public class Iseditable extends SeleniumLocatorTag implements Conditional {
    
    @Override
    public boolean evaluate() throws DTFException {
        if (!getSelenium().isEditable(getLocator()) ) { 
            String msg = getLocator() + " not editable on page.";
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
