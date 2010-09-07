package com.yahoo.dtf.actions.selenium.commands.mouse;

import com.yahoo.dtf.actions.selenium.commands.SeleniumLocatorTag;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag dragAndDrop
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               This command simulates the dragging and dropping of an HTML
 *               element. The element is identified by the locator tag while the
 *               movement made is specified by the movementString attribute.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://someplace.com" browser="*firefox">
 *      <open url="/"/>
 *      <dragAndDrop locator="//input[@name='test']" movementString="0,50"/>
 *  </selenium>
 */
public class Draganddrop extends SeleniumLocatorTag {
   
    /**
     * @dtf.attr
     * @dtf.attr.desc offset in pixels from the current location to which the 
     *                element should be moved, e.g., "+70,-300"
     */
    private String movementsString = null;
    
    @Override
    public void execute() throws DTFException {
        getSelenium().dragAndDrop(getLocator(), getMovementsString());
    }
    
    public String getMovementsString() throws ParseException {
        return replaceProperties(movementsString);
    }
    
    public void setMovementsString(String movementsString) {
        this.movementsString = movementsString;
    }
}
