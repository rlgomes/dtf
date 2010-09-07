package com.yahoo.dtf.actions.selenium.conditionals;

import com.yahoo.dtf.actions.conditionals.Condition;
import com.yahoo.dtf.actions.conditionals.Conditional;
import com.yahoo.dtf.actions.selenium.commands.SeleniumLocatorTag;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag isChecked
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Gets whether a toggle-button (checkbox/radio) is checked. 
 *               Fails if the specified element doesn't exist or isn't a 
 *               toggle-button.
 *               </p>
 * 
 * @dtf.tag.example
 *  <selenium baseurl="http://somewhere.com">
 *      <for property="i" range="0..6">
 *          <check locator="//input[@name='gf-chart-ticker${i}']"/>
 *          <assert>
 *              <isChecked locator="//input[@name='gf-chart-ticker${i}']"/>
 *          </assert>
 *      </for>
 *      <for property="i" range="0..6">
 *          <uncheck locator="//input[@name='gf-chart-ticker${i}']"/>
 *          <assert>
 *              <not>
 *                  <isChecked locator="//input[@name='gf-chart-ticker${i}']"/>
 *              </not>
 *          </assert>
 *      </for> 
 *  </selenium>
 */
public class Ischecked extends SeleniumLocatorTag implements Conditional {
    
    @Override
    public boolean evaluate() throws DTFException {
        if (!getSelenium().isChecked(getLocator()) ) { 
            String msg = getLocator() + " is not checked.";
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
