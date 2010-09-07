package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag getSelectedIndex
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Gets option index (option number, starting at 0) for selected 
 *               option in the specified select element.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://someplace.com">
 *      <open url="/"/>
 *      <getSelectedIndex selectLocator="id=list-select" property="idx"/>
 *  </selenium>
 */
public class Getselectedindex extends SeleniumGetStateSelectLocatorTag {
    
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().getSelectedIndex(getSelectLocator());
    }
}
