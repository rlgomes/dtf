package com.yahoo.dtf.actions.selenium.commands.base;

import com.yahoo.dtf.actions.selenium.commands.SeleniumLocatorTag;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag selectFrame
 * @dtf.skip.index
 *  
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Changes the focus of the current session to the frame identified
 *               with the {@dtf.link Element Locator} attribute.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="...">
 *      <selectFrame locator="frame2"/>    
 *  </selenium>
 */
public class Selectframe extends SeleniumLocatorTag {
    
    @Override
    public void execute() throws DTFException {
        getSelenium().selectFrame(getLocator());
    }
}
