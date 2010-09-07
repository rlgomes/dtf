package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag getPrompt
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Retrieves the message of a JavaScript question prompt dialog 
 *               generated during the previous action. Successful handling of 
 *               the prompt requires prior execution of the answerOnNextPrompt 
 *               command. If a prompt is generated but you do not get/verify it, 
 *               the next Selenium action will fail.
 *               <b>NOTE:</b> under Selenium, JavaScript prompts will NOT pop 
 *               up a visible dialog.
 *               <b>NOTE:</b> Selenium does NOT support JavaScript prompts that 
 *               are generated in a page's onload() event handler. In this case 
 *               a visible dialog WILL be generated and Selenium will hang until
 *               someone manually clicks OK.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://someplace.com">
 *      <open url="/"/>
 *      <getPrompt property="prompt_message"/>
 *  </selenium>
 */
public class Getprompt extends SeleniumGetStateTag {
    
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().getPrompt();
    }
}
