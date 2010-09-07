package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag getXpathCount
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Returns the number of nodes that match the specified xpath, 
 *               eg. "//table" would give the number of tables.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.yahoo.com">
 *      <open url="/finance"/>
 *      <getXpathCount xpath="//input" property="number_of_inputs"/>
 *  </selenium>
 */
public class Getxpathcount extends SeleniumGetStateLocatorTag {
 
    /**
     * @dtf.attr xpath
     * @dtf.attr.desc  the xpath expression to evaluate. do NOT wrap this 
     *                 expression in a 'count()' function; we will do that for
     *                 you.
     */
    private String xpath = null;
    
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().getXpathCount(getXpath());
    }
    
    public String getXpath() throws ParseException {
        return replaceProperties(xpath);
    }
    
    public void setXpath(String xpath) {
        this.xpath = xpath;
    }
}
