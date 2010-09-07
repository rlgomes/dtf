package com.yahoo.dtf.actions.selenium.commands.confirmation;

import com.yahoo.dtf.actions.selenium.Selenium;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag chooseOkOnNextConfirmation
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Lets selenium know to automatically accept on the next 
 *               confirmation dialog shown in the current session   
 *               </p>
 */
public class Chooseokonnextconfirmation extends Selenium {
   
    @Override
    public void execute() throws DTFException {
        getSelenium().chooseOkOnNextConfirmation();
    }
}
