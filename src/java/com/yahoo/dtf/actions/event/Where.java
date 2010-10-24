package com.yahoo.dtf.actions.event;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag where
 * @dtf.skip.index
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc This child tag of query allows us to define the exact 
 *               conditions of the query that we want to run against the 
 *               collection of events stored by a previous recorder.
 *               
 * @dtf.tag.example 
 * <query type="txt" 
 *        event="hc.*"  
 *        uri="storage://OUTPUT/perf.db" 
 *        property="obj1">
 *     <where>
 *         <eq field="myfield" value="myvalue"/>
 *     </where>
 * </query>
 * 
 * @dtf.tag.example 
 * <query type="txt" 
 *        event="hc.*"  
 *        uri="storage://OUTPUT/perf.db" 
 *        property="obj1">
 *     <where>
 *         <and>
 *             <eq field="field1" value="somevalue"/>
 *             <neq field="field2" value="othervalue"/>
 *         </and>
 *     </where>
 * </query>
 */
public class Where extends Action {
    public Where() { }
    public void execute() throws DTFException { }
}
