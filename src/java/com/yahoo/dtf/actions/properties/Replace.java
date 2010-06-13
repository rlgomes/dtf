package com.yahoo.dtf.actions.properties;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag replace
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc the replace tag allows you to use regular expression to replace
 *               very specific parts of other strings. The regular expression 
 *               language is specific to Java's Pattern matching classes and 
 *               you can find more information ont he specific regular expression
 *               syntax here <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/regex/Pattern.html">Java Regular Expression</a> 
 *               You are also able to use group so that you can then replace the
 *               original expression with just one of the group using the $ sign, 
 *               have a look at the examples below for a better understanding.
 *
 * @dtf.tag.example 
 * <replace source="${test}" destination="newtest" expression=":" with=","/>
 *
 * @dtf.tag.example 
 * <replace source="${email}" destination="user" expression="([^@]+)@(.+)" with="$1"/>
 *
 * @dtf.tag.example 
 * <replace source="${email}" destination="host" expression="([^@]+)@(.+)" with="$2"/>
 */
public class Replace extends Action {

    /**
     * @dtf.attr source
     * @dtf.attr.desc Source value to apply replace on.
     * 
     */
    private String source = null;
  
    /**
     * @dtf.attr destination
     * @dtf.attr.desc The name of the property to record the result in.
     * 
     */
    private String destination = null;
   
    /**
     * @dtf.attr expression
     * @dtf.attr.desc The regular expression to use when matching.
     * 
     */
    private String expression = null;
    
    /**
     * @dtf.attr with
     * @dtf.attr.desc The new string to replace the match with.
     * 
     */
    private String with = null;

    public void execute() throws DTFException {
        String result = getSource().replaceAll(getExpression(), getWith());
        getConfig().setProperty(getDestination(), result);
    }

    public String getExpression() throws ParseException { return replaceProperties(expression); }
    public void setExpression(String expression) { this.expression = expression; }

    public String getSource() throws ParseException { return replaceProperties(source); }
    public void setSource(String source) { this.source = source; }

    public String getDestination() throws ParseException { return replaceProperties(destination); }
    public void setDestination(String destination) { this.destination = destination; }

    public String getWith() throws ParseException { return replaceProperties(with); }
    public void setWith(String with) { this.with = with; }
}
