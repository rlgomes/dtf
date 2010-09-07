package com.yahoo.dtf.actions.selenium.conditionals;

import com.yahoo.dtf.actions.conditionals.Condition;
import com.yahoo.dtf.actions.conditionals.Conditional;
import com.yahoo.dtf.actions.selenium.Selenium;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag isTextPresent
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               </p>
 * 
 * @dtf.tag.example
 *  <selenium baseurl="http://www.google.com">
 *      <open url="/finance"/>
 *      <waitForPageToLoad timeout="30000"/>
 *      <assert>
 *          <and>
 *              <isTextPresent pattern="Market"/>
 *              <isTextPresent pattern="Portfolio"/>
 *              <isTextPresent pattern="Stock"/>
 *          </and>
 *      </assert> 
 *  </selenium>
 */
public class Istextpresent extends Selenium implements Conditional {
    
    private String pattern = null;
    
    @Override
    public boolean evaluate() throws DTFException {
        if (!getSelenium().isTextPresent(getPattern()) ) { 
            String msg = getPattern() + " not found on page.";
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
    
    public String getPattern() throws ParseException {
        return replaceProperties(pattern);
    }
    
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
