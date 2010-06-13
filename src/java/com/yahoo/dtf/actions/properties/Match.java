package com.yahoo.dtf.actions.properties;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yahoo.dtf.actions.conditionals.Condition;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag match
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc This conditional tag can be used where you would use any of the
 *               other conditional tags such as eq, neq, etc. The only difference
 *               is that this tag allows you validate that a given property
 *               respects a specified regular expression. The regular expression
 *               syntax can be found at <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/regex/Pattern.html">Java Regular Expression</a>.
 *
 * @dtf.tag.example 
 * <if>
 *     <match source="user@host" expression="[^@]+@.+"/>
 *     <then>
 *         <log>Using user authentication method</log>
 *     </then>
 * </if>
 *
 * @dtf.tag.example 
 * <if>
 *     <match source="http://user:password@host/path" 
 *            expression="(h|H)(t|T)(p|P)(s|S)?:/{1,3}[^:]:+[^@]+@[^/]/.+"/>
 *     <then>
 *         <log>Validated it looks like any http/https url</log>
 *     </then>
 * </if>
 *
 * @dtf.tag.example 
 * <if>
 *     <match source="http://user:password@host/path" 
 *            expression="http\:\/" 
 *            partial="true"/>
 *     <then>
 *         <log>Looks like an HTTP url</log>
 *     </then>
 * </if>
 *
 */
public class Match extends Condition {

    /**
     * @dtf.attr source
     * @dtf.attr.desc The value to validate that respects the specified regular
     *                expression.
     * 
     */
    private String source = null;
  
    /**
     * @dtf.attr expression
     * @dtf.attr.desc The regular expression to use when validating the source 
     *                attribute.
     * 
     */
    private String expression = null;
    
    /**
     * @dtf.attr insensitive
     * @dtf.attr.desc specifies if we should run with case insensitive on or off,
     *                value should be true or false and the default is false.
     */
    private String insensitive = null;
    
    /**
     * @dtf.attr partial
     * @dtf.attr.desc If specified, will succeed if a substring matches
     */
    private String partial = null;

    public boolean evaluate() throws DTFException {
        Pattern myPattern;
        Matcher myMatcher;

        /*
         * XXX: easy optimization here if we keep a HashMap of the hash code of 
         *      the getExpression() string to do a lookup for the Pattern object
         *      and not have to compile a new Pattern object every time.
         */
        if (getInsensitive()) {
            myPattern = Pattern.compile(getExpression().toLowerCase());
            myMatcher = myPattern.matcher(getSource().toLowerCase());
        } else {
            myPattern = Pattern.compile(getExpression());
            myMatcher = myPattern.matcher(getSource());
        }
        
        if ( getPartial() ) {
            return myMatcher.find();
        } else {
            return myMatcher.matches();
        }
    }
    
    public String getOp1() throws ParseException { return getSource(); }
    public String getOp2() throws ParseException { return getExpression(); }
    
    public void setOp1(String op1) { setSource(op1); }
    public void setOp2(String op2) { setExpression(op2); }
   
    public String getExpression() throws ParseException { return replaceProperties(expression); }
    public void setExpression(String expression) { this.expression = expression; }

    public String getSource() throws ParseException { return replaceProperties(source); }
    public void setSource(String source) { this.source = source; }

    public boolean getInsensitive() throws ParseException { return toBoolean("insensitive",insensitive); }
    public void setInsensitive(String insensitive) { this.insensitive = insensitive; }
    
    public boolean getPartial() throws ParseException { return toBoolean("partial",partial); }
    public void setPartial(String partial) { this.partial = partial; }
    
    public String explanation() throws DTFException {
        return "\"" + getSource() + "\" matches the regular expression \"" + 
               getExpression() + "\"";
    }
}
