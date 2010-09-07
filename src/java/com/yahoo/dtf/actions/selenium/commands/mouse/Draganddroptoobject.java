package com.yahoo.dtf.actions.selenium.commands.mouse;

import com.yahoo.dtf.actions.selenium.commands.SeleniumLocatorTag;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag dragAndDropToObject
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               This command will drag and drop an element to another element
 *               using nothing but the {@dtf.link Element Locator} strings
 *               specified.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://someplace.com" browser="*firefox">
 *      <open url="/"/>
 *      <dragAndDropToObject locatorOfObjectToBeDragged="//input[@name='pic1']"
 *                           locatorOfDragDestinationObject="//input[@name='trash']"/>
 *  </selenium>
 */
public class Draganddroptoobject extends SeleniumLocatorTag {

    /**
     * @dtf.attr locatorOfObjectToBeDragged
     * @dtf.attr.desc an element to be dragged.
     */
    private String locatorOfObjectToBeDragged = null;
    
    /**
     * @dtf.attr locatorOfDragDestinationObject
     * @dtf.attr.desc an element whose location (i.e., whose top left corner) 
     *                will be the point where locatorOfObjectToBeDragged is 
     *                dropped.
     */
    private String locatorOfDragDestinationObject = null;
   
    @Override
    public void execute() throws DTFException {
        getSelenium().dragAndDropToObject(getLocatorOfObjectToBeDragged(),
                                          getLocatorOfDragDestinationObject());
    }

	public String getLocatorOfObjectToBeDragged() throws ParseException {
	    return replaceProperties(locatorOfObjectToBeDragged);
	}
	
	public void setLocatorOfObjectToBeDragged(String locatorOfObjectToBeDragged) {
	    this.locatorOfObjectToBeDragged = locatorOfObjectToBeDragged;
	}

    public String getLocatorOfDragDestinationObject() throws ParseException {
        return replaceProperties(locatorOfDragDestinationObject);
    }
    
    public void setLocatorOfDragDestinationObject(String locatorOfDragDestinationObject) {
        this.locatorOfDragDestinationObject = locatorOfDragDestinationObject;
    }

}
