package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.actions.selenium.Selenium;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag setSpeed
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Set execution speed (i.e., set the millisecond length of a 
 *               delay which will follow each selenium operation). By default, 
 *               there is no such delay, i.e., the delay is 0 milliseconds.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.google.com">
 *      <open url="/"/>
 *      <setSpeed speed="500"/>
 *  </selenium>
 */
public class Setspeed extends Selenium {

    /**
     * @dtf.attr value
     * @dtf.attr.desc the number of milliseconds to pause after operation.
     */
    private String value = null;
   
    @Override
    public void execute() throws DTFException {
        getSelenium().setSpeed(getValue());
    }
    
    public String getValue() throws ParseException {
        return replaceProperties(value);
    }
    
    public void setValue(String value) {
        this.value = value;
    }
}
