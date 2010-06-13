package com.yahoo.dtf.actions.flowcontrol;

import java.util.ArrayList;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag catch
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Used within the try tag. This tag is what allows us to define 
 *               what to catch and even specify different behavior for different 
 *               types of errors.
 * 
 * @dtf.tag.example 
 * <try>
 *     <sequence>
 *         <echo>This will naturally succeed.</echo>
 *     </sequence>
 *     <catch exception="com.yahoo.dtf.exception.*">
 *         <fail message="This part should never be executed."/>
 *     </catch>
 * </try>
 *
 * @dtf.tag.example 
 * <try>
 *     <!-- execute your own tags -->
 *     <catch exception="com.yahoo.dtf.exception.MySpecialException">
 *         <fail message="This part should never be executed."/>
 *     </catch>
 * </try>
 */
public class Catch extends Action {

    /**
     * @dtf.attr exception 
     * @dtf.attr.desc Default to nothing which matches any and all exceptions. 
     *                Otherwise defines the regular expression that will match 
     *                with the expected exception name. This name is the full 
     *                package of the exception like so: java.io.IOException
     */
    private String exception = null;
    
    /**
     * @dtf.attr property
     * @dtf.attr.desc Default to nothing. Otherwise if specified this property 
     *                will contain the message from the exception caught.
     */
    private String property = null;
  
    public Catch() {}
    
    public void execute() throws DTFException { }
    
    public boolean matchAndExecute(DTFException e) throws DTFException { 
        /*
         * Trick done here is to be able to figure out what was the 
         * underlying causes of certain exceptions since at certain points
         * of execution you may have a DTFException that wraps an 
         * underlying exception that is meant to be caught.
         */
        ArrayList<String> exceptions = new ArrayList<String>();
        ArrayList<String> messages = new ArrayList<String>();
       
        Throwable aux = e;
        while (aux != null) { 
            exceptions.add(aux.getClass().getName());
            messages.add(aux.getMessage());
            aux = aux.getCause();
        }
        
        if (getException() == null || exceptions.contains(getException())) {
            if (getProperty() != null) { 
                String message = null;
                if ( getException() != null ) { 
	                message = messages.get(exceptions.indexOf(getException()));
                } else { 
                    message = messages.get(0);
                }
                getConfig().setProperty(getProperty(), message);
            }
            executeChildren();
            return true;
        }
        
        return false;
    }

    public String getException() throws ParseException { return replaceProperties(exception); }
    public void setException(String exception) { this.exception = exception; }

    public String getProperty() throws ParseException { return replaceProperties(property); }
    public void setProperty(String property) { this.property = property; }
}
