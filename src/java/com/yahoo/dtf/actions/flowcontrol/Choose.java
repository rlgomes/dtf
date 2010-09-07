package com.yahoo.dtf.actions.flowcontrol;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag choose
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc child tag of the choices tag that groups a bunch of actions and
 *               associates a probablility with the execution of this group of
 *               actions. 
 * 
 * @dtf.tag.example 
 * <distribute workers="3" iterations="1..10">
 *     <choices>
 *         <choose howoften="25%">
 *             <event name="test.event2"/>
 *         </choose>
 *         <choose howoften="25%">
 *             <event name="test.event1"/>
 *         </choose>
 *         <choose howoften="50%">
 *             <event name="test.event3"/>
 *         </choose>
 *     </choices>
 * </distribute>
 *
 */
public class Choose extends Action {

    /**
     * @dtf.attr howoften
     * @dtf.attr.desc describes in a percentage how often this action will be 
     *                executed.
     */
    private String howoften = null;
    
    public void execute() throws DTFException {
        executeChildren();
    }
    
    public String getHowoften() throws ParseException { 
        return replaceProperties(howoften);
    }
    public void setHowoften(String howoften) { this.howoften = howoften; }
}
