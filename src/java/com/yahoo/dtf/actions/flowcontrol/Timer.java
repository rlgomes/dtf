package com.yahoo.dtf.actions.flowcontrol;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.BreakException;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.InterruptionException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.util.TimeUtil;

/**
 * @dtf.tag for
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc All of the direct children of this tag are executed over and 
 *               over until the value of interval has been hit. The value of the
 *               interval is explain in the interval attribute below. 
 *
 * @dtf.tag.example 
 * <timer interval="2s">
 *     <component id="${agent}">
 *         <echo>Echo on ${agent} of loop ${loop}.</echo>
 *     </component>
 * </timer>
 *
 * @dtf.tag.example 
 * <timer interval="2m">
 *     <log>on loop #${loop}.</log>
 * </timer>
 */
public class Timer extends Action {

    /**
     * @dtf.attr interval
     * @dtf.attr.desc The interval of time during which all underlying children
     *                will be re-executed. This time can be defined with the 
     *                follow suffixes: 
     *                <table border="1">
     *                  <tr>
     *                      <th>Value</th> 
     *                      <th>Description</th> 
     *                  </tr>
     *                  <tr>
     *                      <td>s</td>
     *                      <td>Seconds</td>
     *                  </tr>
     *                  <tr>
     *                      <td>h</td>
     *                      <td>Hours</td>
     *                  </tr>
     *                  <tr>
     *                      <td>d</td>
     *                      <td>Days</td>
     *                  </tr>
     *                  <tr>
     *                      <td>m</td>
     *                      <td>Months</td>
     *                  </tr>
     *                </table>
     */
    private String interval = null;
    
    /**
     * @dtf.attr property 
     * @dtf.attr.desc property is assigned the value of the number of times the
     *                timer has looped.
     */
    private String property = null;
    
    public Timer() { }

    public void execute() throws DTFException { 
        long interval = TimeUtil.parseTime("interval",getInterval());
        long start = System.currentTimeMillis();
        long loop = 0;
       
        try { 
	        while (System.currentTimeMillis() - start < interval) {
	            loop++;
	            
	            if (getProperty() != null) 
	                getConfig().setProperty(getProperty(), ""+loop);
	            
	            executeChildren();
	            checkInterruption();
	        }
        } catch (InterruptionException e) { 
            if ( getLogger().isDebugEnabled() )
                getLogger().debug("execution interrupted.");
        } catch (BreakException e) { 
            // break point
            if ( getLogger().isDebugEnabled() )
                getLogger().debug("break point hit",e);
        }
        
        getLogger().info("Timer took " + (System.currentTimeMillis() - start) + 
                         "ms of " + interval + "ms.");
    }

    public String getInterval() throws ParseException { return replaceProperties(interval); }
    public void setInterval(String interval) { this.interval = interval; }

    public String getProperty() throws ParseException { return replaceProperties(property); }
    public void setProperty(String property) { this.property = property; }
}
