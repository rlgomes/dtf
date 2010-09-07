package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag getSelectedIds
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Gets all option element IDs for selected options in the 
 *               specified select or multi-select element.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://someplace.com">
 *      <open url="/"/>
 *      <getSelectedIds selectLocator="id=multi-select" property="ids"/>
 *  </selenium>
 */
public class Getselectedids extends SeleniumGetStateSelectLocatorTag {
    
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().getSelectedIds(getSelectLocator());
    }
}
