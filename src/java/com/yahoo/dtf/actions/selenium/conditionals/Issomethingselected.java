package com.yahoo.dtf.actions.selenium.conditionals;

import com.yahoo.dtf.actions.conditionals.Condition;
import com.yahoo.dtf.actions.conditionals.Conditional;
import com.yahoo.dtf.actions.selenium.Selenium;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag isSomethingSelected
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Determines whether some option in a drop-down menu is selected.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.google.com">
 *      <open url="/finance"/>
 *      <waitForPageToLoad timeout="30000"/>
 *      <type locator="q" value="GOOG"/>
 *      <click locator="//input[@value='Get quotes']"/>
 *      <waitForPageToLoad timeout="30000"/>
 *      <select selectLocator="id=related-select"
 *              optionLocator="Most Recent Annual"/>
 *      <assert>
 *          <isSomethingSelected selectLocator="id=related-select"/>
 *      </assert> 
 *  </selenium>
 */
public class Issomethingselected extends Selenium implements Conditional {
   
    private String selectLocator = null;
    
    @Override
    public boolean evaluate() throws DTFException {
        if (!getSelenium().isSomethingSelected(getSelectLocator())) { 
            String msg = getSelectLocator() + " is not selected on page.";
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
    
    public String getSelectLocator() throws ParseException {
        return replaceProperties(selectLocator);
    }
    
    public void setSelectLocator(String selectLocator) {
        this.selectLocator = selectLocator;
    }
}
