package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.actions.selenium.Selenium;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag setContext
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Sets the per-session extension Javascript.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.google.com">
 *      <open url="/"/>
 *      <setExtenionJs extensionJs=""/>
 *  </selenium>
 */
public class Setextensionjs extends Selenium {
 
    /**
     * @dtf.attr extensionJs
     * @dtf.attr.desc the message to be sent to the browser
     */
    private String extensionJs = null;
   
    @Override
    public void execute() throws DTFException {
        getSelenium().setExtensionJs(getExtensionJs());
    }
    
    public String getExtensionJs() throws ParseException {
        return replaceProperties(extensionJs);
    }
    
    public void setExtensionJs(String extensionJs) {
        this.extensionJs = extensionJs;
    }
}
