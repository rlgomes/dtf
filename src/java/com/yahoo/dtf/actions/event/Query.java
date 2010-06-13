package com.yahoo.dtf.actions.event;

import java.net.URI;
import java.util.ArrayList;

import com.yahoo.dtf.actions.event.Field;
import com.yahoo.dtf.actions.event.Select;
import com.yahoo.dtf.actions.event.Where;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.conditionals.Condition;
import com.yahoo.dtf.exception.ActionException;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.query.Cursor;
import com.yahoo.dtf.query.QueryFactory;
import com.yahoo.dtf.query.QueryIntf;

/**
 * @dtf.tag query
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc This tag is used to query for results that have been previously 
 *               reordered with the record tag. This tag will create a result 
 *               set and then to iterate over that result set you must call the
 *               nextresult tag to make it progress. Not all recorders are 
 *               query-able though, so you should look at the following list for
 *               query-able recorders:
 *               
 * @dtf.tag.example 
 * <query event="test.*"  
 *        uri="storage://OUTPUT/perf.txt" 
 *        cursor="obj1">
 *     <where>
 *         <eq field="myfield" value="myvalue"/>
 *     </where>
 * </query>
 * 
 * @dtf.tag.example 
 * <query event="test.*"  
 *        uri="storage://OUTPUT/perf.txt" 
 *        cursor="obj1">
 *     <where>
 *         <and>
 *             <eq field="field1" value="somevalue"/>
 *             <neq field="field2" value="othervalue"/>
 *         </and>
 *     </where>
 * </query>
 * 
 */
public class Query extends Action {

    /**
     * @dtf.attr uri
     * @dtf.attr.desc Input URI of the previous recorded events.
     * 
     */
    private String uri = null;
    
    /**
     * @dtf.attr type
     * @dtf.attr.desc Identifies the type of query engine that will be used to 
     *                open the specified recorded events at the place specified 
     *                byt the <code>uri</code>.
     *                
     *           <b>Query Types</b>
     *           <table border="1">
     *               <tr>
     *                   <th>Name</th> 
     *                   <th>Description</th> 
     *               </tr>
     *               <tr>
     *                   <td>txt</td> 
     *                   <td>
     *                       Text query engine, able to query the results 
     *                       recorded by the txt recorder.
     *                   </td> 
     *               </tr>
     *          </table>
     */
    private String type = null;
    
    /**
     * @dtf.attr event 
     * @dtf.attr.desc The name of the event to query the underlying cursor on.
     */
    private String event = null;

    /**
     * @dtf.attr cursor
     * @dtf.attr.desc The name of the cursor that will give us the results to 
     *                apply the query constraints on.
     */
    private String cursor = null;
   
    /**
     * @dtf.attr encoding
     * @dtf.attr.desc See {@dtf.link Loadproperties} for more information on the
     *                encoding attribute.
     */
    private String encoding = null;

    public Query() { }
    
    public void execute() throws DTFException {
        if ( getLogger().isDebugEnabled() ) 
            getLogger().debug("Starting query: " + this);
        
        QueryIntf query = getQueryIntf(this);
        addCursor(getCursor(), new Cursor(query));
    }
    
    public static QueryIntf getQueryIntf(Query q) throws ParseException, DTFException { 
        QueryIntf query = QueryFactory.getQuery(q.getType());
        
        Select select = (Select) q.findFirstAction(Select.class);
        ArrayList<Where> children = q.findActions(Where.class);
        Where where =  null;
       
        if (children.size() != 0)
            where = children.get(0);
        
        query.open(q.getUri(),
                   (select == null ? null : select.findActions(Field.class)), 
                   (where == null ? null : (Condition)where.findFirstAction(Condition.class)),
                   q.getEvent(),
                   q.getCursor(),
                   q.getEncoding());
        return query;
    }

    public String getEvent() throws ParseException { return replaceProperties(event); }
    public void setEvent(String event) { this.event = event; }

    public String getType() throws ParseException { return replaceProperties(type); }
    public void setType(String type) { this.type = type; }

    public URI getUri() throws ActionException, ParseException { return parseURI(uri); }
    public void setUri(String uri) { this.uri = uri; }

    public String getCursor() throws ParseException { return replaceProperties(cursor); }
    public void setCursor(String cursor) { this.cursor = cursor; }

    public void setEncoding(String encoding) { this.encoding = encoding; }
    public String getEncoding() throws ParseException { return replaceProperties(encoding); }
}
