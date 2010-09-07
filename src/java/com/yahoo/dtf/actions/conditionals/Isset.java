package com.yahoo.dtf.actions.conditionals;

import com.yahoo.dtf.actions.conditionals.Condition;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag isset
 * @dtf.skip.index
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
        String property = getProperty();
        try { 
            replaceProperties("${" + property + "}");
            return true;
        } catch (DTFException e) { 
            registerContext(ASSERT_EXP_CTX, property + " is set");
            return false;
        }
    }
    
    public String getProperty() throws ParseException { return replaceProperties(property); }
    public void setProperty(String property) { this.property = property; } 
}
