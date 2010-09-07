package com.yahoo.dtf.actions.selenium.commands.base;

import com.yahoo.dtf.actions.selenium.commands.SeleniumLocatorTag;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag answerOnNextPrompt
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Instructs Selenium to return the specified answer string in 
 *               response to the next JavaScript prompt [window.prompt()].
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium url="http://somewhere.com">
 *      <answerOnNextPrompt answer="superduper"/>
 *      <getEval script="window.prompt('Secret code word?', '')"
 *               property="response"/>
 *      <assert><isPromptPresent/></assert>
 *      <assert><eq op1="${response}" op2="superduper"/></assert>
 *  </selenium>
 */
public class Answeronnextprompt extends SeleniumLocatorTag {
  
    /**
     * @dtf.attr answer
     * @dtf.attr.desc the answer to give in response to the prompt pop-up.
     */
    private String answer = null;
    
    @Override
    public void execute() throws DTFException {
        getSelenium().answerOnNextPrompt(getAnswer());
    }
    
    public String getAnswer() throws ParseException {
        return replaceProperties(answer);
    }
    
    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
