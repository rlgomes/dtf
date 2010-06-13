package com.yahoo.dtf.actions.stats;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.ActionException;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.query.Cursor;
import com.yahoo.dtf.recorder.Event;
import com.yahoo.dtf.stats.GenCalcStats;


/**
 * @dtf.tag stats
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc The stats tag is capable of calculating some statistical 
 *               analysis over the events identified in the result set of the 
 *               attribute <code>cursor</code>. The calculated statistics are 
 *               thrown as events to be used by the testcase as it best sees fit.
 *
 * @dtf.event cursor.fieldname
 * @dtf.event.attr avg_val
 * @dtf.event.attr.desc Each field of the specified event that is of a number 
 *                      type will have its average value calculated and recorded
 *                      in this attribute.
 *
 * @dtf.event cursor.fieldname
 * @dtf.event.attr tot_val
 * @dtf.event.attr.desc Each field of the specified event that is of a number 
 *                      type will have its total value calculated and recorded
 *                      in this attribute.
 *
 * @dtf.event cursor.fieldname
 * @dtf.event.attr min_val
 * @dtf.event.attr.desc Each field of the specified event that is of a number 
 *                      type will have its minimum value calculated and recorded
 *                      in this attribute.
 *
 * @dtf.event cursor.fieldname
 * @dtf.event.attr max_val
 * @dtf.event.attr.desc Each field of the specified event that is of a number 
 *                      type will have its maximum value calculated and recorded
 *                      in this attribute.
 * 
 * @dtf.event cursor
 * @dtf.event.attr min_dur
 * @dtf.event.attr.desc The minimum duration of any event in milliseconds 
 *                      recorded will be saved in this attribute.
 * 
 * @dtf.event cursor
 * @dtf.event.attr max_dur
 * @dtf.event.attr.desc The maximum duration of any event in milliseconds 
 *                      recorded will be saved in this attribute.
 * 
 * @dtf.event cursor
 * @dtf.event.attr avg_dur
 * @dtf.event.attr.desc The average duration of any event in milliseconds 
 *                      recorded will be saved in this attribute.
 * 
 * @dtf.event cursor
 * @dtf.event.attr tot_dur
 * @dtf.event.attr.desc The total duration of any event in milliseconds recorded 
 *                      will be saved in this attribute.
 *
 * @dtf.event cursor
 * @dtf.event.attr csv_dur
 * @dtf.event.attr.desc This property contains a CSV contents for all of the 
 *                      latencies for the events sampled including the number
 *                      of times it occurred like so:
 *                     
 *                      <pre> 
 *                      23,455
 *                      50,412
 *                      100,20
 *                      </pre>
 *                     
 *                      Where the first column is the latency (duration in ms)
 *                      and the second column is the number of events that had
 *                      this duration. With this you can easily generate a 
 *                      graph using gnuplot, with the following gnuplot script
 *                      and having outputted the property into a file called
 *                      "latency.data":
 *                     
 *                      <pre>
 *                      set title "Latency graph of some sample data"
 *                     
 *                      set xdata time
 *                      set timefmt "%s"
 *                     
 *                      set terminal png
 *                      set output "graph.png"
 *                     
 *                      set xlabel 'Number of events'
 *                      set ylabel 'Duration (ms)'
 *                     
 *                      plot 'latency.data' using 1:2 with linespoints title 'latencies'
 *                      </pre>
 *                      
 *                      You can also easily import the CSV file into your 
 *                      favorite spreadsheet software and generate any graphs 
 *                      you'd like. 
 *                      
 * @dtf.event cursor
 * @dtf.event.attr tot_occ
 * @dtf.event.attr.desc The total occurrences of this event will be saved in 
 *                      this attribute.
 * 
 * @dtf.event cursor
 * @dtf.event.attr avg_occ
 * @dtf.event.attr.desc The number of occurrences of this event per second over 
 *                      the duration of all events is recorded in this attribute.
 *
 * @dtf.event cursor
 * @dtf.event.attr MONITOR_states
 * @dtf.event.attr.desc Generated only when you have used the monitor attribute
 *                      and will contain a comma separated list of the various
 *                      states that specific field had. the MONITOR name is 
 *                      replaced with the name of the fields you were monitoring.
 * 
 * @dtf.event cursor.FIELD_VALUE
 * @dtf.event.attr max_int
 * @dtf.event.attr.desc this event will be generated for every FIELD in the 
 *                      monitors attribute and it will have the format 
 *                      FIELD_VALUE.max_int being that the VALUE is one of the
 *                      various values that field had. So for each VALUE that
 *                      the FIELD can assume there will be another event 
 *                      generated containing the maximum interval during which
 *                      that VALUE occurred without any changes.
 * 
 * @dtf.event cursor.FIELD_VALUE
 * @dtf.event.attr min_int
 * @dtf.event.attr.desc this event will be generated for every FIELD in the 
 *                      monitors attribute and it will have the format 
 *                      FIELD_VALUE.min_int being that the VALUE is one of the
 *                      various values that FIELD had. So for each VALUE that
 *                      the FIELD can assume there will be another event 
 *                      generated containing the minimum interval during which
 *                      that VALUE occurred without any changes.        
 *
 * @dtf.event cursor.FIELD_VALUE
 * @dtf.event.attr tot_int
 * @dtf.event.attr.desc this event will be generated for every FIELD in the 
 *                      monitors attribute and it will have the format 
 *                      FIELD_VALUE.avg_int being that the VALUE is one of the
 *                      various values that FIELD had. So for each VALUE that
 *                      the FIELD can assume there will be another event 
 *                      generated containing the total amount of time that the
 *                      specified field was in the VALUE state.
 *
 * @dtf.event cursor.FIELD_VALUE
 * @dtf.event.attr avg_int
 * @dtf.event.attr.desc this event will be generated for every FIELD in the 
 *                      monitors attribute and it will have the format 
 *                      FIELD_VALUE.avg_int being that the VALUE is one of the
 *                      various values that FIELD had. So for each VALUE that
 *                      the FIELD can assume there will be another event 
 *                      generated containing the average amount of time that the
 *                      specified field was in the VALUE state.
 *
 * @dtf.tag.example
 * <script>
 *     <query uri="storage://OUTPUT/${recorder.filename}" 
 *            type="${recorder.type}" 
 *            event="dtf.event"
 *            cursor="cursor1">
 *         <where>
 *             <eq op1="runid" op2="${runid}"/>
 *         </where>
 *     </query>
 *                 
 *     <record type="object" uri="property://txtrecorder"> 
 *         <stats cursor="cursor1" event="stats"/>
 *     </record>
 * </script>
 * 
 * @dtf.tag.example
 * <script>
 *     <query uri="storage://OUTPUT/${recorder.filename}" 
 *            type="${recorder.type}" 
 *            event="dtf.event"
 *            cursor="cursor1"/>
 *                 
 *     <record type="object" uri="property://txtrecorder"> 
 *         <stats cursor="cursor1" event="stats"/>
 *     </record>
 * </script>
 */
public class Stats extends Action {

    /**
     * @dtf.attr cursor
     * @dtf.attr.desc Identifies the cursor to use when calculating statistics.
     */
    private String cursor = null;

    /**
     * @dtf.attr event
     * @dtf.attr.desc Specifies the event name to attach the statistics that 
     *                are calculated.
     */
    private String event = null;
    
    /**
     * @dtf.attr monitor
     * @dtf.attr.desc specify a field to monitor the value changes and generate 
     *                statistic about the amount of time spent in each state. 
     *                When you use this attribute there are some new events that
     *                are generated, they are mentioned above. 
     *                
     */
    private String monitor = null;

    public Stats() { }
    
    public void execute() throws DTFException {
        if ( getLogger().isDebugEnabled() )
            getLogger().debug("Starting to analyze " + this);
        
        Cursor cursor = retCursor(getCursor());
        
        if ( cursor == null )
            throw new DTFException("Cursor [" + getCursor() + "] does not exist");
        
        GenCalcStats calcStats = new GenCalcStats(getMonitor());
        LinkedHashMap<String, String> props = 
                                           calcStats.calcStats(cursor);
        Event event = new Event(getEvent());
        Iterator<Entry<String,String>> entries = props.entrySet().iterator();
       
        while ( entries.hasNext() ) {
            Entry<String, String> entry = entries.next();
            event.addAttribute(entry.getKey(), entry.getValue());
        }
        
        getRecorder().record(event);
        if ( getLogger().isDebugEnabled() )
            getLogger().debug("Finished analyzing " + this);
    }

    public String getCursor() throws ParseException { return replaceProperties(cursor); }
    public void setCursor(String cursor) { this.cursor = cursor; }
    
    public String getEvent() throws ActionException, ParseException { return replaceProperties(event); }
    public void setEvent(String event) { this.event = event; }
    
    public String getMonitor() throws ParseException { return replaceProperties(monitor); }
    public void setMonitor(String monitor) { this.monitor = monitor; }
}
