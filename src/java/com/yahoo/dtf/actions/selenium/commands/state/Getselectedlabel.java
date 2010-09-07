package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag getSelectedLabel
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Gets option label (visible text) for selected option in the 
 *               specified select element.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://someplace.com">
 *      <open url="/"/>
 *      <getSelectedLabel selectLocator="id=list-select" property="label"/>
 *  </selenium>
 */
public class Getselectedlabel extends SeleniumGetStateSelectLocatorTag {
    
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().getSelectedLabel(getSelectLocator());
    }
}
