package com.yahoo.dtf.actions.selenium.commands.state;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag getExpression
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               </p>
 * 
 * @dtf.tag.example 
 *  <selenium baseurl="http://www.google.com">
 *      <open url="/"/>
 *      <getExpression property="a" expression=""/>
 *  </selenium>
 */
public class Getexpression extends SeleniumGetStateTag {
  
    /**
     * @dtf.attr expression
     * @dtf.attr.desc the value to return.
     */
    private String expression = null;
    
    @Override
    public Object getValue() throws DTFException {
        return getSelenium().getEval(getExpression());
    }
   
    public String getExpression() throws ParseException {
        return replaceProperties(expression);
    }
    
    public void setExpression(String expression) {
        this.expression = expression;
    }
}
