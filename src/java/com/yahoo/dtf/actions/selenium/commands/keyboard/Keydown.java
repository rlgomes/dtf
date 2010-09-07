package com.yahoo.dtf.actions.selenium.commands.keyboard;

import com.yahoo.dtf.actions.selenium.commands.SeleniumLocatorTag;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag keyDown
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               This command simulates holding a specified key sequence down.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.yahoo.com" browser="*firefox">
 *      <open url="/"/>
 *      <keyDown keySequence="w" locator="//input[@name='p']"/>
 *  </selenium>
 */
public class Keydown extends SeleniumLocatorTag {

    /**
     * @dtf.attr keySequence
     * @dtf.attr.desc either be a string("\" followed by the numeric keycode of 
     *                the key to be pressed, normally the ASCII value of that 
     *                key), or a single character. For example: "w", "\119".
     */
    private String keySequence = null;
    
    @Override
    public void execute() throws DTFException {
        getSelenium().keyDown(getLocator(), getKeySequence());
    }
   
    public void setKeySequence(String keySequence) {
        this.keySequence = keySequence;
    }
    
    public String getKeySequence() throws ParseException {
        return replaceProperties(keySequence);
    }
    
}
