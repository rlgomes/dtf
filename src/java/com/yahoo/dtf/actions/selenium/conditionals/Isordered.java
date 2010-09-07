package com.yahoo.dtf.actions.selenium.conditionals;

import com.yahoo.dtf.actions.conditionals.Condition;
import com.yahoo.dtf.actions.conditionals.Conditional;
import com.yahoo.dtf.actions.selenium.Selenium;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag isOrdered
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Check if these two elements have same parent and are ordered 
 *               siblings in the DOM. Two same elements will not be considered 
 *               ordered.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.google.com">
 *      <open url="/finance"/>
 *      <waitForPageToLoad timeout="30000"/>
 *      <assert>
 *          <isOrdered locator1="q" locator2="//input[@value='Get quotes']"/>
 *      </assert>
 *  </selenium>
 */
public class Isordered extends Selenium implements Conditional {
   
    /**
     * @dtf.attr locator1
     * @dtf.attr.desc an {@dtf.link Element Locator} pointing to the first element.
     */
    private String locator1 = null;

    /**
     * @dtf.attr locator2
     * @dtf.attr.desc an {@dtf.link Element Locator} pointing to the second element.
     */
    private String locator2 = null;
    
    @Override
    public boolean evaluate() throws DTFException {
        if (!getSelenium().isOrdered(getLocator1(), getLocator2()) ) { 
            String msg = getLocator1() + " and " + getLocator2() + 
                         " are not ordered on page.";
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
    
    public String getLocator1() throws ParseException {
        return replaceProperties(locator1);
    }
    
    public void setLocator1(String locator1) {
        this.locator1 = locator1;
    }
    
    public String getLocator2() throws ParseException {
        return replaceProperties(locator2);
    }
    
    public void setLocator2(String locator2) {
        this.locator2 = locator2;
    }
}
