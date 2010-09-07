package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag getAllFields
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Returns the IDs of all fields on the page. If a given field 
 *               has no ID, it will appear as "" in this array. The property 
 *               will contain a comma separated list of these ids.
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.yahoo.com" browser="*firefox">
 *      <open url="/"/>
 *      <getAllFields property="fields"/>
 *  </selenium>
 */
public class Getallfields extends SeleniumGetStateTag {
    
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().getAllFields();
    }
}
