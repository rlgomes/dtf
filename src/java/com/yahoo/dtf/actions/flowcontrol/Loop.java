package com.yahoo.dtf.actions.flowcontrol;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.ParseException;

/**
 * Base class for For and Parallelloop tags
 * 
 * @author rlgomes
 */
public abstract class Loop extends Action {

    /**
     * @dtf.attr property 
     * @dtf.attr.desc This property will contain the value of the element in the
     *                range expression at the time of execution of that specific
     *                element.
     */
    private String property = null;
    
    /**
     * @dtf.attr range
     * @dtf.attr.desc Range expression defining how this loop will behave and 
     *                over which elements it will iterate.
     */
    private String range = null;

    /**
     * @dtf.attr type
     * @dtf.attr.desc There are 3 types of loops that are currently available, 
     *                they are:
     *                
     *                <b>Loop Type</b>
     *                <table border="1">
     *                    <tr>
     *                        <th>Type</th> 
     *                        <th>Description</th> 
     *                    </tr>
     *                    <tr>
     *                        <td>parallel</td> 
     *                       <td>A loop that will take the underlying action and 
     *                            execute it in parallel as many times as there are 
     *                            elements in the specified range expression.</td> 
     *                    </tr>
     *                    <tr>
     *                        <td>sequential</td> 
     *                        <td>A loop that will take the underlying action and 
     *                            execute it in sequence as many times as there are 
     *                            elements in the specified range expression.</td> 
     *                    </tr>
     *                    <tr>
     *                        <td>timer</td> 
     *                        <td>A loop that will take the underlying action and 
     *                            execute it in sequence for as long as the 
     *                            time specified in the range attribute.</td>
     *                    </tr>
     *                </table>
     */
    private String type = null;

    public Loop() { }
   
    public String getProperty() throws ParseException { return replaceProperties(property); }
    public void setProperty(String property) { this.property = property; }

    public String getRange() throws ParseException { return replaceProperties(range); }
    public void setRange(String range) { this.range = range; }

    public String getType() throws ParseException { return replaceProperties(type); }
    public void setType(String type) { this.type = type; }
}
