package com.yahoo.dtf.actions.function;

import com.yahoo.dtf.actions.util.CDATA;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ReturnException;

/**
 * @dtf.tag return
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Return a value to the call tag from within a function. For the
 *               time being this return statement can only be done at the end 
 *               of the function.
 *
 * @dtf.tag.example 
 * <sequence>
 *     <function name="func3">
 *         <param name="flag" type="required"/>  
 *         <!-- function does its thing and sets the succeeded property with a 
 *              boolean value of the success or insuccess of this function -->
 *         <return>${succeeded}</return>
 *     </function>
 *     
 *     <call function="func3" result="result">
 *         <property name="flag" value="true"/>
 *     </call>
 *      
 *     <log>func3 returned ${result}</log>
 * </sequence>
 */
public class Return extends CDATA { 
    public void execute() throws DTFException {
        ReturnException re = new ReturnException("Just returning a value.");
        re.setReturnValue(getCDATA());
        throw re;
    }
}
