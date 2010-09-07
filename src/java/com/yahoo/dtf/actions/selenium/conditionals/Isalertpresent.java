package com.yahoo.dtf.actions.selenium.conditionals;

import com.yahoo.dtf.actions.conditionals.Condition;
import com.yahoo.dtf.actions.conditionals.Conditional;
import com.yahoo.dtf.actions.selenium.Selenium;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag isAlertPresent
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               This function returns true if an alert has occured and false 
 *               otherwise.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://somewhere.com">
 *      <chooseOkOnNextConfirmation/>
 *      <runScript script="window.alert('alerte rouge!')"/>
 *      <assert><isAlertPresent/></assert>
 *      <getAlert property="alert.msg"/>
 *      <assert><eq op1="${alert.msg}" op2="alerte rouge!"/></assert>
 *  </selenium>
 */
public class Isalertpresent extends Selenium implements Conditional {
    
    @Override
    public boolean evaluate() throws DTFException {
        if (!getSelenium().isAlertPresent() ) { 
            String msg = "alert not not present.";
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
