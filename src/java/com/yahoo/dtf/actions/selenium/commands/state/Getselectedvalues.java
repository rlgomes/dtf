package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag getSelectedValues
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Gets all option values (value attributes) for selected options 
 *               in the specified select or multi-select element.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://someplace.com">
 *      <open url="/"/>
 *      <getSelectedValues selectLocator="id=multi-select" property="values"/>
 *  </selenium>
 */
public class Getselectedvalues extends SeleniumGetStateSelectLocatorTag {
    
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().getSelectedValues(getSelectLocator());
    }
}
