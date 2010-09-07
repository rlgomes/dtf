package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag getLocation
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Gets the absolute URL of the current page.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.google.com">
 *      <open url="/"/>
 *      <getLocation property="url"/>
 *  </selenium>
 */
public class Getlocation extends SeleniumGetStateTag {
  
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().getLocation();
    }
}
