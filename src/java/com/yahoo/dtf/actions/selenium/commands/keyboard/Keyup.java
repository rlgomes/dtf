package com.yahoo.dtf.actions.selenium.commands.keyboard;

import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag keyUp
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               This command simulates releasing a specified key sequence.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.yahoo.com" browser="*firefox">
 *      <open url="/"/>
 *      <keyUp keySequence="w" locator="//input[@name='p']"/>
 *  </selenium>
 */
public class Keyup extends Keydown {

    @Override
    public void execute() throws DTFException {
        getSelenium().keyUp(getLocator(), getKeySequence());
    }
}
