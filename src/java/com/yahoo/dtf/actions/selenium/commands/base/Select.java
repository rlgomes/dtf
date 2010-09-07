package com.yahoo.dtf.actions.selenium.commands.base;

import com.yahoo.dtf.actions.selenium.Selenium;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag select
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               The selector command instructs selenium to pick a specific 
 *               option from a drop down menu. The drop down selection is done
 *               using an {@dtf.link Element Locator} while the exact option to
 *               pick is done using the {@dtf.link Option Locator}.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.google.com">
 *      <open url="/finance" windowID="test"/>
 *      <waitForPageToLoad timeout="30000"/>
 *      <type locator="q" value="GOOG"/>
 *      <click locator="//input[@value='Get quotes']"/>
 *      <waitForPageToLoad timeout="30000"/>        
 *      
 *      <log>Toggling between a few drop down options</log> 
 *      <select selectLocator="id=related-select"
 *              optionLocator="Most Recent Annual"/>
 *      <sleep time="1s"/>
 *      <select selectLocator="id=related-select"
 *              optionLocator="Most Recent Quarter"/>
 *      <sleep time="1s"/>
 *      <select selectLocator="id=related-select"
 *              optionLocator="Most Recent Annual"/>
 *  </selenium>
 */
public class Select extends Selenium {
    
    /**
     * @dtf.attr selectLocator
     * @dtf.attr.desc {@dtf.link Element Locator} to identify the drop down.
     */
    private String selectLocator = null;

    /**
     * @dtf.attr optionLocator
     * @dtf.attr.desc {@dtf.link Option Locator} to identify the exact option 
     *                to choose.
     */
    private String optionLocator = null;

    @Override
    public void execute() throws DTFException {
        getSelenium().select(getSelectLocator(), getOptionlocator());
    }
    
    public String getSelectLocator() throws ParseException {
        return replaceProperties(selectLocator);
    }
    
    public void setSelectLocator(String selectLocator) {
        this.selectLocator = selectLocator;
    }
    
    public String getOptionlocator() throws ParseException {
        return replaceProperties(optionLocator);
    }
    
    public void setOptionlocator(String optionLocator) {
        this.optionLocator = optionLocator;
    }

}
