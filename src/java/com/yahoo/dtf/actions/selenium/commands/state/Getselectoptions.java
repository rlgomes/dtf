package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag getSelectOptions
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Gets all option labels in the specified select drop-down.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://someplace.com">
 *      <open url="/"/>
 *      <getSelectOptions selectLocator="id=drop-down1" property="options"/>
 *  </selenium>
 */
public class Getselectoptions extends SeleniumGetStateSelectLocatorTag {
    
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().getSelectOptions(getSelectLocator());
    }
}
