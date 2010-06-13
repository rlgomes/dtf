package com.yahoo.dtf.actions.flowcontrol;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag sequence
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc This tag simply aggregates a group of tags and executes them in 
 *               the sequential order by which they appear. Its main use is to 
 *               allow you to better organize your tests when using 
 *               {@dtf.link parallel} and {@dtf.link parallelloop} tags and 
 *               make it easier to read where the parallelization is taking 
 *               place.
 * 
 * @dtf.tag.example 
 * <sequence>
 *     <log> Value from db: ${element.iteration}</log> 
 *     <nextresult property="element"/>
 * </sequence>
 */
public class Sequence extends Action {
   
    private String threadID = null;
    
    public Sequence() { }
    public void execute() throws DTFException { 
        executeChildren(); 
    }
    
    public void setThreadID(String id) { threadID = id; } 
    public String getThreadid() {  return threadID; } 
}
