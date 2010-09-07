package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag getCursorPosition
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Retrieves the text cursor position in the given input element
 *               or textarea; beware, this may not work perfectly on all
 *               browsers. Specifically, if the cursor/selection has been 
 *               cleared by JavaScript, this command will tend to return the 
 *               position of the last location of the cursor, even though the 
 *               cursor is now gone from the page. This is filed as SEL-243.
 *               </p>
 *               <p>
 *               This method will fail if the specified element isn't an input
 *               element or textarea, or there is no cursor in the element.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://someplace.com" browser="*firefox">
 *      <open url="/"/>
 *      <getCursorPosition locator="//input[@id='test']" 
 *                         property="cursor.position"/>
 *  </selenium>
 */
public class Getcursorposition extends SeleniumGetStateLocatorTag {
   
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().getCursorPosition(getLocator());
    }
}
