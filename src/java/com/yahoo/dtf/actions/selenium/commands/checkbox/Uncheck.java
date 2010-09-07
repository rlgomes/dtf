package com.yahoo.dtf.actions.selenium.commands.checkbox;

import com.yahoo.dtf.actions.selenium.commands.SeleniumLocatorTag;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag uncheck
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               This command can turn the state of a checkbox to the unchecked
 *               state with just a simple indication of the right location using
 *               the {@dtf.link Element Locator} attribute.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.google.com" browser="*${browser}">
 *      <open url="/finance"/>
 *      <waitForPageToLoad timeout="30000"/>
 *      <for property="i" range="0..6">
 *          <check locator="//input[@name='gf-chart-ticker${i}']"/>
 *          <sleep time="1s"/>
 *      </for>
 *      <for property="i" range="0..6">
 *          <uncheck locator="//input[@name='gf-chart-ticker${i}']"/>
 *          <sleep time="1s"/>
 *      </for>
 *  </selenium>
 */
public class Uncheck extends SeleniumLocatorTag {
    
    @Override
    public void execute() throws DTFException {
        getSelenium().uncheck(getLocator());
    }
}
