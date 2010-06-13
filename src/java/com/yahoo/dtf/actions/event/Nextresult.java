package com.yahoo.dtf.actions.event;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.config.Config;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.NoMoreResultsException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.query.Cursor;
import com.yahoo.dtf.query.QueryIntf;

/**
 * @dtf.tag nextresult
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc This tag is used to move the cursor of a previously executed 
 *               query tag. By moving the cursor we will get new results from 
 *               the collection of previously recorded events that obey the 
 *               previously defined query.
 *               
 * @dtf.tag.example 
 * <try> 
 *     <sequence>
 *         <query uri="storage://OUTPUT/myevents.txt" cursor="cursor1"/>
 *         
 *         <nextresult cursor="cursor1"/>
 *         <while>
 *             <eq op1="true" op2="true"/>
 *             <sequence>
 *                 <nextresult cursor="cursor1"/>
 *                 <log>${cursor1.field}</log>
 *             </sequence>
 *         </while>
 *     </sequence>
 *     <catch exception="com.yahoo.dtf.exceptions.NoMoreResultsException">
 *          <log>All done.</log>
 *     </catch>
 * </try>
 * 
 */
public class Nextresult extends Action {

    private final static String NEXTRESULT_EVENT = "nextresult";
    
    /**
     * @dtf.attr cursor
     * @dtf.attr.desc Identifies the cursor name that will be used to fetch the
     *                next result.
     */
    private String cursor = null;
    
    /**
     * @dtf.attr recycle
     * @dtf.attr.desc If this value is true then when the result set identified 
     *                by the cursor hits the end of its results the cursor is 
     *                reopened. If this value is false once there are no more 
     *                results this tag will throw a NoMoreResultsException.
     */
    private String recycle = null;
    
    public Nextresult() { }

    public void execute() throws DTFException {
        com.yahoo.dtf.recorder.Event event = 
         new com.yahoo.dtf.recorder.Event(NEXTRESULT_EVENT + "." + getCursor());

        Cursor cursor = retCursor(getCursor());
        
        if ( cursor == null ) 
            throw new ParseException("Unable to find cursor [" + getCursor() + "]");
        
        Config config = getConfig();
        HashMap<String,String> map = null;
        
        if ( (map = cursor.next(isRecycle())) == null )
            throw new NoMoreResultsException("No more results in db for this query.");
        
        event.start();
        Iterator<Entry<String,String>> entries  = map.entrySet().iterator();
        while ( entries.hasNext() ) { 
            Entry<String,String> entry = entries.next();
            config.setProperty(entry.getKey(),entry.getValue());
        }
        event.stop();

        QueryIntf query = cursor.getQuery();
        event.addAttribute("resets", query.getResetCount());
        getRecorder().record(event);
    }

    public String getCursor() throws ParseException { return replaceProperties(cursor); }
    public void setCursor(String cursor) { this.cursor = cursor; }

    public boolean isRecycle() throws ParseException { return getRecycle(); }
    public boolean getRecycle() throws ParseException { return toBoolean("recycle",recycle); }
    public void setRecycle(String recycle) { this.recycle = recycle; }
}
