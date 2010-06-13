package com.yahoo.dtf.actions.event;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;

/**
 * @dtf.tag select
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc This child tag of query allows for you to define what you want 
 *               to get back from the query. The fields specified in the select
 *               are the only ones you can then refer to in the where tag as 
 *               well as the only fields available afterwards on the cursor. 
 *               This is useful to avoid loading fields that may have binary
 *               blobs that would otherwise hurt the querying performance.
 *               
 * @dtf.tag.example 
 * <query type="txt" 
 *        event="hc.*"  
 *        uri="storage://OUTPUT/perf.db" 
 *        property="obj1">
 *     <select>
 *         <field name="myfield"/>
 *     </select>
 *     <where>
 *         <eq field="myfield" value="${myvalue}"/>
 *     </where>
 * </query>
 */
public class Select extends Action {
    public Select() { }
    public void execute() throws DTFException { }
}
