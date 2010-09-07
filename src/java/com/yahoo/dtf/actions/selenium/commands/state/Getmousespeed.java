package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag getMouseSpeed
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Returns the number of pixels between "mousemove" events during
 *               dragAndDrop commands (default=10).
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.google.com">
 *      <open url="/"/>
 *      <getMouseSpeed property="mouse_speed"/>
 *  </selenium>
 */
public class Getmousespeed extends SeleniumGetStateTag {
  
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().getMouseSpeed();
    }
}
