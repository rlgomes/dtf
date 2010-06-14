package com.yahoo.dtf.actions.conditionals;

import com.yahoo.dtf.actions.conditionals.Condition;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag isset
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Validates if a property is set or not.
 * 
 * @dtf.tag.example 
 * <if>
 *     <isset property="flagReady"/>
 *     <then>
 *         <log>Ready to go!</log>
 *     </then>
 * </if>
 */
public class Isset extends Condition {
   
    private String property = null;
    
    public Isset() { }
    
    public boolean evaluate() throws DTFException {
        try { 
            replaceProperties("${" + getProperty() + "}");
            return true;
        } catch (DTFException e) { 
            return false;
        }
    }
    
    public String getProperty() throws ParseException { return replaceProperties(property); }
    public void setProperty(String property) { this.property = property; } 
    
    public String explanation() throws DTFException {
        return getProperty() + " is set";
    }
}