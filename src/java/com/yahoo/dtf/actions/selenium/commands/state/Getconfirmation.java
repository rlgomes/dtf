package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag getConfirmation
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Retrieves the message of a JavaScript confirmation dialog 
 *               generated during the previous action. By default, the confirm 
 *               function will return true, having the same effect as manually
 *               clicking OK. This can be changed by prior execution of the 
 *               chooseCancelOnNextConfirmation command. If an confirmation is 
 *               generated but you do not get/verify it, the next Selenium 
 *               action will fail.
 *               <b>NOTE:</b> under Selenium, JavaScript confirmations will 
 *               NOT pop up a visible dialog.
 *               <b>NOTE:</b> Selenium does NOT support JavaScript confirmations
 *               that are generated in a page's onload() event handler. In this
 *               case a visible dialog WILL be generated and Selenium will hang
 *               until you manually click OK.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://someplace.com" browser="*firefox">
 *      <open url="/"/>
 *      <getConfirmation property="confirmation.msg"/>
 *  </selenium>
 */
public class Getconfirmation extends SeleniumGetStateTag {
    
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().getConfirmation();
    }
}
