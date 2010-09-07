package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.actions.selenium.Selenium;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag setMouseSpeed
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Configure the number of pixels between "mousemove" events 
 *               during dragAndDrop commands (default=10). Setting this value 
 *               to 0 means that we'll send a "mousemove" event to every single 
 *               pixel in between the start location and the end location; that 
 *               can be very slow, and may cause some browsers to force the 
 *               JavaScript to timeout.
 *               </p>
 *               <p>
 *               If the mouse speed is greater than the distance between the two
 *               dragged objects, we'll just send one "mousemove" at the start
 *               location and then one final one at the end location.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.google.com">
 *      <open url="/"/>
 *      <setMouseSpeed pixels="15"/>
 *  </selenium>
 */
public class Setmousespeed extends Selenium {

    /**
     * @dtf.attr pixels
     * @dtf.attr.desc the number of pixels between "mousemove" events
     */
    private String pixels = null;
   
    @Override
    public void execute() throws DTFException {
        getSelenium().setMouseSpeed(getPixels());
    }
    
    public String getPixels() throws ParseException {
        return replaceProperties(pixels);
    }
    
    public void setPixels(String pixels) {
        this.pixels = pixels;
    }
}
