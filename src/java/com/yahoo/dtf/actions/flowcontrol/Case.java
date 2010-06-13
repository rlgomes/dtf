package com.yahoo.dtf.actions.flowcontrol;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.conditionals.Conditional;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag case
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc The case tag is used in the switch tag and identifies a possible
 *               case to match the property value from the switch tag. If there
 *               is a match then the case children tags will be executed. 
 *               
 * @dtf.tag.example 
 * <switch property="${test}">
 *     <case value="1">
 *          <log>Switch case #1</log>
 *     </case> 
 *     <case value="2">
 *         <log>Switch case #2</log>
 *     </case>
 *     <default>
 *         <log>Default case called</log>
 *     </default>         
 * </switch> 
 */
public class Case extends Action {

    /**
     * @dtf.attr value
     * @dtf.attr.desc the value to match the property from the switch tag with.
     */
    private String value = null;
  
    public Case() {}
    
    public boolean evaluateAndExecute(String prop) throws DTFException { 
        Conditional condition = (Conditional)findFirstAction(Conditional.class);
        
        if (prop != null) { 
            if (getValue().equals(prop)) { 
                executeChildren();
                return true;
            }
        }
        
        if (condition != null && condition.evaluate()) { 
            executeChildren();
            return true;
        }
        
        return false;
    }
    
    public void execute() throws DTFException { }

    public String getValue() throws ParseException { 
        return replaceProperties(value); 
    }
    public void setValue(String value) { this.value = value; }
}
