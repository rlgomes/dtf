package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag getAlert
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Retrieves the message of a JavaScript alert generated during 
 *               the previous action, or fail if there were no alerts. Getting 
 *               an alert has the same effect as manually clicking OK. If an 
 *               alert is generated but you do not get/verify it, the next 
 *               Selenium action will fail.
 *               
 *               <b>NOTE:</b> under Selenium, JavaScript alerts will NOT pop up
 *               a visible alert dialog.
 *               <b>NOTE:</b> Selenium does NOT support JavaScript alerts that
 *               are generated in a page's onload() event handler. In this case
 *                a visible dialog WILL be generated and Selenium will hang 
 *                until someone manually clicks OK.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://someplace.com" browser="*firefox">
 *      <open url="/"/>
 *      <getAlert property="alertmsg"/>
 *  </selenium>
 */
public class Getalert extends SeleniumGetStateTag {
    
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().getAlert();
    }
}
