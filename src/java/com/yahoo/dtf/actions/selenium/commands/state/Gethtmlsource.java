package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag getHtmlSource
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Returns the entire HTML source between the opening and closing 
 *               "html" tags for the currently loaded web page.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.google.com">
 *      <open url="/"/>
 *      <getHtmlSource property="src"/>
 *  </selenium>
 */
public class Gethtmlsource extends SeleniumGetStateTag {
  
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().getHtmlSource();
    }
}
