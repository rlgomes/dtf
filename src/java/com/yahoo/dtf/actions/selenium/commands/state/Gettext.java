package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag getText
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Gets the text of an element. This works for any element that 
 *               contains text. This command uses either the textContent 
 *               (Mozilla-like browsers) or the innerText (IE-like browsers) of 
 *               the element, which is the rendered text shown to the user.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://someplace.com">
 *      <open url="/"/>
 *      <getText locator="//input[@id='address']" property="address.value"/>
 *  </selenium>
 */
public class Gettext extends SeleniumGetStateLocatorTag {
    
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().getText(getLocator());
    }
}
