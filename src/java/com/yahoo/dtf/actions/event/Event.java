package com.yahoo.dtf.actions.event;

import com.yahoo.dtf.actions.reference.Referencable;
import com.yahoo.dtf.actions.util.ActionContexts;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag event
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc This tag is used to throw events in a DTF test case, and allows 
 *               you to measure the amount of time or the number of times a 
 *               certain block of your test case is executed over the course of 
 *               the test case. This is useful for measuring the performance of 
 *               a certain part of the test case and it will automatically allow 
 *               you to have event information for calculating what the maximum, 
 *               minimum or averages of a certain operation are.
 *               
 *               There are already some events thrown within the DTF code itself 
 *               for testing of the actual framework. These events are usually 
 *               prefixed by the  prefix, which makes it simple to filter 
 *               out of the other tests being executed.
 * 
 * @dtf.tag.example 
 * <event name="dtf.echo">
 *    <echo>This block of actions is being measured by a a counter</echo>
 *    <property name="iteration" value="${index}"/>
 * </event>
 */
public class Event extends Referencable {

    /**
     * @dtf.attr name
     * @dtf.attr.desc Identifies the name of the the event to be thrown here in 
     *                the test case.
     */
    private String name = null;
    
    
    public Event() { }

    public void execute() throws DTFException {
        com.yahoo.dtf.recorder.Event event = 
                                    new com.yahoo.dtf.recorder.Event(getName());
        registerContext(ActionContexts.EVENT_CONTEXT, event);
        event.start();
        executeChildren();
        event.stop();
        unRegisterContext(ActionContexts.EVENT_CONTEXT);
        getRecorder().record(event);
    }

    public String getName() throws ParseException { return replaceProperties(name); }
    public void setName(String name) { this.name = name; }
}
