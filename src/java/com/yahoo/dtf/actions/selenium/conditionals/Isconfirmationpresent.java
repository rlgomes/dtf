package com.yahoo.dtf.actions.selenium.conditionals;

import com.yahoo.dtf.actions.conditionals.Condition;
import com.yahoo.dtf.actions.conditionals.Conditional;
import com.yahoo.dtf.actions.selenium.Selenium;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag isConfirmationPresent
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Has confirm() been called?
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://somewhere.com">
 *      <chooseOkOnNextConfirmation/>
 *      <runScript script="window.confirm('Are you sure?')"/>
 *      <assert><isConfirmationPresent/></assert>
 *      <getConfirmation property="confirm.msg"/>
 *      <assert><eq op1="${confirm.msg}" op2="Are you sure?"/></assert>
 *  </selenium>
 */
public class Isconfirmationpresent extends Selenium implements Conditional {
    
    @Override
    public boolean evaluate() throws DTFException {
        if (!getSelenium().isConfirmationPresent() ) { 
            String msg = "confirmation not not present.";
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
