package com.yahoo.dtf.actions.function;

import java.util.ArrayList;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.properties.Property;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.FunctionException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.exception.ReturnException;

/**
 * @dtf.tag call
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Execute functions that were defined in this test case or
 *               imported using the {@dtf.link Import} tag. Any properties 
 *               created/altered within a function will not be visible after 
 *               this function has terminated its execution.
 *
 * @dtf.tag.example 
 * <sequence>
 *     <function name="func3">
 *         <param name="nomore" type="optional"/>  
 *         <local><echo>In func3</echo></local>
 *     </function>
 *     
 *     <call function="func3"/>
 *     
 *     <call function="func3">
 *         <property name="nomore" value="true"/>
 *     </call>
 * </sequence>
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
 * 
 */
public class Call extends Action {

    /**
     * @dtf.attr function
     * @dtf.attr.desc The unique name of the function to call from this point 
     *                in the XML testcase.
     */
    private String function = null;

    /**
     * @dtf.attr result
     * @dtf.attr.desc the property to store the return value from this function.
     */
    private String result = null;
    
    public void execute() throws DTFException {
        Function function = (Function)getFunctions().getFunction(getFunction());
        
        if (function == null) 
            throw new FunctionException("Unable to find function: " + getFunction());

        ArrayList<Property> properties = findActions(Property.class);
        
        /*
         * This is necessary so we can trace where the function got called from
         * otherwise the line reporting would be just of the function in the 
         * original XML file and not of the call made from any other testcase
         * using this same function.
         */
        try { 
	        function.executeFunction(properties);
        } catch (ReturnException e) { 
            /*
             * Using a special exception to return a value anywhere from the 
             * underlying function.
             */
            String value = e.getReturnValue();
	        if (value != null) { 
	            if (getResult() != null) { 
	                getConfig().setProperty(getResult(), value, true);
	            }
	        }
	    } catch (DTFException e) { 
	        throw new DTFException("Error calling function [" +
	                               getFunction() + "]",e);
	    }
    }

    public String getFunction() throws ParseException { return replaceProperties(function); }
    public void setFunction(String function) { this.function = function; }

    public String getResult() throws ParseException { return replaceProperties(result); }
    public void setResult(String result) { this.result = result; }
}
