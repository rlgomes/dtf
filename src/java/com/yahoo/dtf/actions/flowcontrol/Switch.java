package com.yahoo.dtf.actions.flowcontrol;

import java.util.ArrayList;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag switch
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc The switch tag does what you would expect of a switch tag and
 *               that is give you the possibility to do a determined action 
 *               based on the value of a single property without having to write
 *               multiple nested if/else conditions. So it will make the end 
 *               result of having multiple possible conditions a lot easier to 
 *               read as well as easier to maintain by the testwriter.
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
public class Switch extends Action {

    /**
     * @dtf.attr property
     * @dtf.attr.desc property to test the next case for equality. If none of 
     *                the following case tags match then the default tag will
     *                be executed(if it is present).
     */
    private String property = null;
  
    public Switch() {}
    
    public void execute() throws DTFException {
        ArrayList<Case> cases = findActions(Case.class);
    
        // moving it forward so be very careful in tags like this to get the 
        // value only once.
        String prop = getProperty();
        
        for (int i = 0; i < cases.size(); i++) { 
            Case caseA = cases.get(i);
            if ( caseA.evaluateAndExecute(prop) )  {
                return;
            }
        }

        Default defaultCase = (Default) findFirstAction(Default.class);
        
        if (defaultCase != null) 
            defaultCase.execute();
    }

    public String getProperty() throws ParseException { return replaceProperties(property); }
    public void setProperty(String property) { this.property = property; }
}
