package com.yahoo.dtf.actions.selenium.commands.base;

import com.yahoo.dtf.actions.selenium.Selenium;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag open
 * @dtf.skip.index
 *  
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Selenium open command that will send the browser to the newly
 *               identified url.
 *               </p>
 * 
 * @dtf.tag.example 
 * <selenium baseurl="http://www.google.com" browser="*firefox">
 *     <open url="/"/>
 * </selenium>
 */
public class Open extends Selenium {

    /**
     * @dtf.attr url
     * @dtf.attr.desc the url to open in the currently focused browser window.
     */
    private String url = null;

    @Override
    public void execute() throws DTFException {
        getSelenium().open(getUrl());
    }
    
    public String getUrl() throws ParseException {
        return replaceProperties(url);
    }
    
    public void setUrl(String url) {
        this.url = url;
    }

}
