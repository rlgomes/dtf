package com.yahoo.dtf.actions.selenium.conditionals;

import com.yahoo.dtf.actions.conditionals.Condition;
import com.yahoo.dtf.actions.conditionals.Conditional;
import com.yahoo.dtf.actions.selenium.Selenium;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag isPromptPresent
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Has a prompt occurred?
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://somewhere.com">
 *      <answerOnNextPrompt answer="superduper"/>
 *      <getEval script="window.prompt('Secret code word?', '')"
 *               property="response"/>
 *      <assert><isPromptPresent/></assert>
 *      <assert><eq op1="${response}" op2="superduper"/></assert>
 *  </selenium>
 */
public class Ispromptpresent extends Selenium implements Conditional {
    
    @Override
    public boolean evaluate() throws DTFException {
        if (!getSelenium().isPromptPresent() ) { 
            String msg = "prompt not not present.";
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
