package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag getValue
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Gets the (whitespace-trimmed) value of an input field (or 
 *               anything else with a value parameter). For checkbox/radio 
 *               elements, the value will be "on" or "off" depending on whether 
 *               the element is checked or not.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.yahoo.com">
 *      <open url="/finance"/>
 *      <getValue locator="//input[@id='some_text_field1']" property="myvalue"/>
 *  </selenium>
 */
public class Getvalue extends SeleniumGetStateLocatorTag {
    
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().getValue(getLocator());
    }
}
