package com.yahoo.dtf.actions.event;

import java.net.URI;

import com.yahoo.dtf.actions.reference.Referencable;
import com.yahoo.dtf.exception.ActionException;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.recorder.RecorderBase;
import com.yahoo.dtf.recorder.RecorderFactory;


/**
 * @dtf.tag record
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc This tag is used to record DTF events thrown within the code 
 *               and also thrown from the test case using the event tag. These 
 *               events can be recorded to the following output formats: XML, 
 *               Text file. Then the query tag can be used to get data back out 
 *               of the recorded medium for later to be used by other test cases 
 *               or the same one.
 * 
 * @dtf.tag.example 
 * <record uri="storage://OUTPUT/dtf.internals.txt" 
 *         type="txt" 
 *         event="dtf.perf.*">
 *     <local> 
 *         <testscript uri="storage://INPUT/parallel.xml"/> 
 *     </local>
 * </record>
 * 
 * @dtf.tag.example
 * <record type="txt" uri="storage://OUTPUT/recorder2.txt">
 *     <for property="index" range="1,2,3,4,5,6,7,8,9,10">
 *         <event name="dtf.echo.outter">
 *             <local>
 *                 <echo>Testing...</echo>
 *             </local>
 *         </event>
 *      </for>
 *      <record type="txt" uri="storage://OUTPUT/recorder3.txt">
 *          <for property="index" range="1,2,3,4,5,6,7,8,9,10">
 *              <event name="dtf.echo.inner">
 *                  <local>
 *                      <echo>Testing...</echo>
 *                  </local>
 *              </event>
 *          </for>
 *      </record>
 * </record>
 */
public class Record extends Referencable {

    /**
     * @dtf.attr uri
     * @dtf.attr.desc output URI used by all the record types except the 
     *                console recorder.
     */
    private String uri = null;
    
    /**
     * @dtf.attr type
     * @dtf.attr.desc Identifies the type of recorder to instantiate to record
     *                events that are thrown child tags of this recorder tag.
     *                
     *           <b>Recorder Types</b>
     *           <table border="1">
     *               <tr>
     *                   <th>Name</th> 
     *                   <th>Description</th> 
     *               </tr>
     *               <tr>
     *                   <td>txt</td> 
     *                   <td>
     *                       Text recorder will record events to a file in a human readable format like so: 
     *                       <pre>
     *                       dtf.perf.action.component.start=1166719862514 
     *                       dtf.perf.action.component.stop=1166719862690
     *                       </pre>
     *                   </td> 
     *               </tr>
     *               <tr>
     *                   <td>console</td> 
     *                   <td>
     *                       Outputs in the same format as the txt type but 
     *                       instead of to a file it will output the 
     *                       results to the STDOUT of the process from 
     *                       where you execute your test case.
     *                            
     *                       Output on screen would look like this: 
     *                       <pre>
     *                       INFO  27/12/2006 00:12:31 Echo - Testing...
     *                       INFO  27/12/2006 00:12:31 ConsoleRecorder - dtf.echo.start=1167208531453 
     *                       INFO  27/12/2006 00:12:31 ConsoleRecorder - dtf.echo.stop=1167208531453
     *                       </pre>
     *                   </td>
     *               </tr>
     *               <tr>
     *                   <td>object</td> 
     *                   <td>
     *                       <b>DEPRECATED: there is no reason to use this 
     *                       anymore since all of the events are available as 
     *                       a property immediately after their execution.</b>
     *                       <br/> 
     *                       <br/> 
     *                       Will output the results of an event to an in memory
     *                       property that can be accessed after the event to 
     *                       get important information about the event.
     *                   </td>
     *               </tr>
     *               <tr>
     *                   <td>stats</td> 
     *                   <td>
     *                      This recorder will calculate your statistics as they
     *                      are being thrown within the framework. It will give 
     *                      you the same statistics that you'd get using the 
     *                      {@dtf.link Stats} tag but they're immediately available after all
     *                      actions within the recorder are done executing. The
     *                      {@dtf.link Stats} are recorded using the event name followed by 
     *                      the usual properties for each stat calculated with 
     *                      the {@dtf.link Stats} tag (see the {@dtf.link Stats} for more
     *                      information on the available statistics).
     *                      <br/> 
     *                      <br/> 
     *                      <b>Remember:</b> if you want to be able to record the same
     *                      events to a file just wrap this record tag with 
     *                      another and you'll have the recording of events to a
     *                      file along with the immediate calculation of
     *                      statistics.
     *                   </td>
     *               </tr>
     *          </table>
     */
    private String type = null;
    
    /**
     * @dtf.attr event
     * @dtf.attr.desc prefix of the events you intend to recorder.
     */
    private String event = null;
    
    /**
     * @dtf.attr append
     * @dtf.attr.desc This value indicates if the events should be appended to
     *                the existing output file or start the recording from 
     *                scratch eliminating any previous results. Accepted values
     *                are "true" and "false".
     */
    private String append = null;
   
    /**
     * @dtf.attr encoding
     * @dtf.attr.desc See {@dtf.link Loadproperties} for more information on the
     *                encoding attribute.
     */
    private String encoding = null;
    
    public Record() { }
    
    public void execute() throws DTFException {
        /*
         * Make the object recorder be debugging logging because this is usually
         * used in loops to grab events generated by other tags and can be very
         * spamy if we always log as info.
         */
        if ( getLogger().isDebugEnabled() ) { 
            getLogger().debug("Starting recorder: " + this);
        }
       
        RecorderBase recorder = RecorderFactory.getRecorder(getType(),
                                                            getUri(),
                                                            getAppend(),
                                                            getEncoding());
        pushRecorder(recorder, getEvent());
        try { 
            executeChildren();
        } finally { 
            // If there's an exception we still need to close up the recorder
            popRecorder();
        }
        
        if ( getLogger().isDebugEnabled() ) { 
            getLogger().debug("Stopping recorder: " + this);
        }
    }

    public String getEvent() throws ParseException { return replaceProperties(event); }
    public void setEvent(String event) { this.event = event; }

    public String getType() throws ParseException { return replaceProperties(type); }
    public void setType(String type) { this.type = type; }

    public URI getUri() throws ActionException, ParseException { return parseURI(uri); }
    public void setUri(String uri) { this.uri = uri; }

    public boolean getAppend() throws ParseException { 
        if (append == null)  
            return true;
        return Boolean.valueOf(replaceProperties(append)).booleanValue();
    }
    
    public void setAppend(String append) { this.append = append; }
    
    public void setEncoding(String encoding) { this.encoding = encoding; }
    public String getEncoding() throws ParseException { return replaceProperties(encoding); }
}
