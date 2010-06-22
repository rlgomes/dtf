package com.yahoo.dtf.actions.event;

import java.net.URI;
import java.util.HashMap;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.conditionals.Condition;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.query.QueryFactory;
import com.yahoo.dtf.query.QueryIntf;
import com.yahoo.dtf.recorder.RecorderBase;
import com.yahoo.dtf.recorder.RecorderFactory;
import com.yahoo.dtf.util.CLIUtil;

/**
 * @dtf.tag filter
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc This tag is used to filter elements from an existing event 
 *               file. You need to specify the srcuri and dsturi then with the
 *               select child tag you can specify which fields from the events
 *               to actually filter along and with the where child tag you can
 *               specify the exact conditions that have to be matched in order 
 *               for the event to make the filter and be written to the dsturi.
 *               
 * @dtf.tag.example 
 * <filter srcuri="storage://OUTPUT/unfiltered.txt"
 *         dsturi="storage://OUTPUT/filtered.txt"
 *         event="write.event">
 *     <select>
 *         <field name="recordid" />
 *         <field name="hash" />
 *     </select>
 *     <where>
 *         <gt op2="recordid" op1="5"/>
 *     </where>
 * </filter>
 * 
 * @dtf.tag.example 
 * <filter srcuri="storage://OUTPUT/unfiltered.txt"
 *         dsturi="storage://OUTPUT/filtered.txt"
 *         event="write.event">
 *     <where>
 *         <eq op2="publish_date" op1="123415123"/>
 *     </where>
 * </filter>
 * 
 */
public class Filter extends Action {

    /**
     * @dtf.attr srcuri
     * @dtf.attr.desc The source URI to read the events from.
     */
    private String srcuri = null;

    /**
     * @dtf.attr dsturi
     * @dtf.attr.desc The destination URI to write the events that have matched
     *                the where clause specified.
     */
    private String dsturi = null;
    
    /**
     * @dtf.attr type
     * @dtf.attr.desc The type of the query and recorder to use for both reading
     *                and writing events from those two previously specified
     *                URI's.
     */
    private String type = null;
   
    /**
     * @dtf.attr event
     * @dtf.attr.desc The name of the event to filter on. This is optional and
     *                when not present then the filtering allows all events to 
     *                be matched against the where clause.
     */
    private String event = null;

    /**
     * @dtf.attr encoding
     * @dtf.attr.desc The encoding to use when reading/writing the events being
     *                handled by this filter.
     */
    private String encoding = null;
    
    public Filter() { }
    
    public void execute() throws DTFException {
        QueryIntf query = QueryFactory.getQuery(getType());
        Select select = (Select)findFirstAction(Select.class);
        Where where = (Where) findFirstAction(Where.class);
        
        query.open(getSrcuri(),
                   (select == null ? null : select.findActions(Field.class)),
                   (where == null ? null : (Condition)where.findFirstAction(Condition.class)),
                   getEvent(),
                   null,
                   "UTF-8");
       
        URI dest = getDstcuri();
        RecorderBase output = RecorderFactory.getRecorder(getType(),
                                                          dest,
                                                          false,
                                                          getEncoding());
       
        output.start();
        HashMap<String,String> map = null;
        com.yahoo.dtf.recorder.Event event = null;
        while ( (map = query.next(false)) != null ) { 
            event = CLIUtil.hashMapToEvent(map);
            output.record(event);
        }
        output.stop();
    }

    public URI getSrcuri() throws ParseException { return parseURI(srcuri); }
    public void setSrcuri(String srcuri) { this.srcuri = srcuri; }

    public URI getDstcuri() throws ParseException { return parseURI(dsturi); }
    public void setDsturi(String dsturi) { this.dsturi = dsturi; }

    public String getType() throws ParseException { return replaceProperties(type); }
    public void setType(String type) { this.type = type; }

    public String getEvent() { return event; }
    public void setEvent(String event) { this.event = event; }

    public String getEncoding() { return encoding; }
    public void setEncoding(String encoding) { this.encoding = encoding; }
}
