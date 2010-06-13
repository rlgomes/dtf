package com.yahoo.dtf.actions.flowcontrol;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.conditionals.Conditional;
import com.yahoo.dtf.exception.AssertionException;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag assert
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Asserts that the underlying condition is true otherwise throws
 *               an AssertionException and can attach your own personalized 
 *               message to this assertion otherwise it will generate a simple
 *               explanation based on the conditions you were trying to validate.
 *               
 * @dtf.tag.example
 * <assert><eq op1="0" op2="0"/></assert>
 *  
 * @dtf.tag.example
 * <assert message="Something is seriously wrong!">
 *     <neq op1="${a}" op2="${a}"/>
 * </assert>
 */
public class Assert extends Action {
   
    /**
     * @dtf.attr message
     * @dtf.attr.desc the message to be displayed when this assertion is not 
     *                satisified.
     */
    String message = null;
    
    public Assert() {}
    
    public void execute() throws DTFException {
        Conditional condition = (Conditional)findFirstAction(Conditional.class);
       
        if ( !condition.evaluate() ) { 
            if ( getMessage() != null ) { 
                throw new AssertionException("Assertion failed [" + 
                                             getMessage() + "]");
            } else { 
                throw new AssertionException("Assertion failed [" + 
                                             condition.explanation() + "]");
            }
        }
    }
    
    public void setMessage(String message) { this.message = message; }
    public String getMessage() throws ParseException { return replaceProperties(message); }
}
