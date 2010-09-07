package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.actions.selenium.commands.SeleniumLocatorTag;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag setCursorPosition
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Moves the text cursor to the specified position in the given 
 *               input element or textarea. This method will fail if the 
 *               specified element isn't an input element or textarea.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.google.com">
 *      <open url="/"/>
 *      <setCursorPosition locator="//input[@id='q']" position="-1"/>
 *  </selenium>
 */
public class Setcursorposition extends SeleniumLocatorTag {
 
    /**
     * @dtf.attr position
     * @dtf.attr.desc the numerical position of the cursor in the field; 
     *                position should be 0 to move the position to the beginning
     *                of the field. You can also set the cursor to -1 to move it
     *                to the end of the field.
     */
    private String position = null;
   
    @Override
    public void execute() throws DTFException {
        getSelenium().setCursorPosition(getLocator(), getPosition());
    }
    
    public String getPosition() throws ParseException {
        return replaceProperties(position);
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
}
