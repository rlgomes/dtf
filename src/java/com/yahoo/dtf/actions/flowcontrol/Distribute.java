package com.yahoo.dtf.actions.flowcontrol;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.properties.Property;
import com.yahoo.dtf.distribution.DistWorkState;
import com.yahoo.dtf.distribution.DistWorker;
import com.yahoo.dtf.distribution.Distribution;
import com.yahoo.dtf.distribution.DistributionFactory;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.InterruptionException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.range.Range;
import com.yahoo.dtf.range.RangeFactory;
import com.yahoo.dtf.recorder.Event;
import com.yahoo.dtf.state.DTFState;
import com.yahoo.dtf.util.ThreadUtil;
import com.yahoo.dtf.util.TimeUtil;

/**
 * @dtf.tag distribute
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc The distribute tag allows you to efficiently distribute action
 *               execution over time. This tag will simulate execution 
 *               distributions and record events when desired distributions were
 *               not achieved during the execution of underlying actions. When
 *               you use the id attribute you'll be able to get events back from
 *               this tag during its execution as to what interval of time failed
 *               to met the requested work goal.
 *               <br/><br/>
 *               Distribute can be used to create load patterns because the func
 *               attribute allows you to define precisely what type of load 
 *               you want this tag to generate. The currently available functions
 *               are defined below and more can be added upon request.
 *               <br/><br/>
 * 
 * @dtf.event WORKER_ID
 * @dtf.event.attr workunit
 * @dtf.event.attr.desc this counts the number of units of work that were done 
 *                      and also lets you know for each event which unit of 
 *                      work was being handled. 
 *
 * @dtf.event WORKER_ID
 * @dtf.event.attr workdone
 * @dtf.event.attr.desc this field will contain the exact amount of work that 
 *                      was done during the unit of time identified by the 
 *                      workunit field.
 *
 * @dtf.event WORKER_ID
 * @dtf.event.attr workgoal
 * @dtf.event.attr.desc this field will contain the exact amount of work that 
 *                      was the goal during the unit of time identified by the 
 *                      workunit field.
 * 
 * @dtf.tag.example 
 * <distribute id="dist1" 
 *             property="iter" 
 *             timer="5m" 
 *             range="1..5" 
 *             func="const(20)"
 *             unit="1m">
 *      <log>Executing action from thread ${dist1.worker}!</log>
 * </distribute> 
 *
 * @dtf.tag.example 
 * <distribute id="dist1" 
 *             property="iter" 
 *             timer="5m" 
 *             range="1..32" 
 *             func="list(10,20,50,20,10)">
 *      <log>Executing action from thread ${dist1.worker}!</log>
 * </distribute> 
 */
public class Distribute extends Action {

    /**
     * @dtf.attr workers
     * @dtf.attr.desc number of worker threads to use to try to guarantee the 
     *                distribution specified. This value is under the control
     *                of the user and should be used wisely. If you start too 
     *                many threads you can hose the system you're running the
     *                distribution on, but if you don't start enough then you
     *                can't meet your distribution requirements.
     *                
     *                @deprecated use the range attribute instead and be aware 
     *                            that this attribute will be removed in a future
     *                            release.
     */
    @Deprecated
    private String workers = null;

    /**
     * @dtf.attr range
     * @dtf.attr.desc Similar to the way ranges are used in the {@dtf.link for}
     *                and {@dtf.link parallelloop} tags. Ranges are used here to
     *                identify the number of underlying threads used and assign
     *                them a unique id based on the ids in the range.
     */
    private String range = null;
    
    /**
     * @dtf.attr property
     * @dtf.attr.desc the property to save the current iteartion value in. This
     *                is useful for generate unique identifiers in each of the
     *                underlying executions. This property is guaranteed to be
     *                unique throughout the whole run even with parallelized 
     *                access to the value. The iteration property always starts
     *                at 1.
     */
    private String property = null;
    
    /**
     * @dtf.attr iterations
     * @dtf.attr.desc the number of iterations to run for. Once reached the test
     *                will come to an end even if there is a value for the timer
     *                property.
     */
    private String iterations = null;
    
    /**
     * @dtf.attr timer
     * @dtf.attr.desc time based execution can be achieved with this property, 
     *                just put a value in the format of 1s, 2h or 3d (3 days) 
     *                and the distribute tag will keep executing the underlying 
     *                action till at least that much time has gone by.
     */
    private String timer = null;
    
    /**
     * @dtf.attr func
     * @dtf.attr.desc the actual distribution function to use during the 
     *                execution of this test. The currently available 
     *                distribution functions are:
     *                <br/>
     *                <b>Func Types</b>
     *                <table border="1">
     *                    <tr>
     *                        <th>Type</th> 
     *                        <th>Description</th> 
     *                        <th>Example</th> 
     *                    </tr>
     *                    <tr>
     *                        <td>const</td> 
     *                        <td>defines a constant distribution where the 
     *                            first argument to this function is the number
     *                            of operations to guarantee for each unit of
     *                            time.</td>
     *                        <td>
     *                          <ul>
     *                              <li>const(10) would try to execute 10 
     *                                  operations per unit of time specified 
     *                                  with the attribute unit.
     *                              </li>
     *                          </ul>
     *                        </td>
     *                    </tr>
     *                    <tr>
     *                        <td>step</td> 
     *                        <td>defines a step function for the distribution, 
     *                            where the first argument is the starting 
     *                            value, the second argument is the stepping size
     *                            and the 3rd argument is the duration of each
     *                            step.</td>
     *                        <td>
     *                           <ul>
     *                              <li>step(1,1,10) would start by executing 1 
     *                                  operation per unit of time and then up
     *                                  by 1 operation every 10 units of time. 
     *                              </li>
     *                              <li>step(0,10,1) would start by executing 0 
     *                                  operation per unit of time and then up
     *                                  by 10 operation every 1 units of time. 
     *                              </li>
     *                          </ul>
     *                        </td>
     *                    </tr>
     *                    <tr>
     *                        <td>list</td> 
     *                        <td>defines a list of the values, where each value
     *                            is the exact amount of operations per unit of
     *                            time to do in each unit of time. The values of 
     *                            this list are used once per unit of time till
     *                            we reach the end and then we just jump back to
     *                            the start of the list.</td>
     *                        <td>
     *                          <ul>
     *                              <li>list(0,20,25,20) would try to execute
     *                                  0 operations for the unit of time 0 and 
     *                                  then 20 at time 1, 25 at time 2 and then
     *                                  20 at unit of time 3, followed by
     *                                  starting from the beginning of the list.
     *                              </li>
     *                          </ul>
     *                        </td>
     *                    </tr>
     *                    <tr>
     *                        <td>limit</td> 
     *                        <td>this function will limit the underlying 
     *                            distribution function to a certain number so 
     *                            that it can't go over that.</td>
     *                        <td>
     *                          <ul>
     *                              <li>limit(step(0,20,1),60) would basically 
     *                                  start at 0 operations per unit of time 
     *                                  and then increases by 20 operations 
     *                                  every unit of time until we hit 60 at
     *                                  which will just stay constant at 60 
     *                                  operations per unit of time. 
     *                              </li>
     *                          </ul>
     *                        </td>
     *                    </tr>
     *                </table>
     *                
     */
    private String func = null;
    
    /**
     * @dtf.attr unit 
     * @dtf.attr.desc the units to measure the current execution in, the units 
     *                can be specified in the same foramt you would specify the
     *                timer property using the syntax 1s (1 second), 2d (2 days)
     *                and can't be smaller than 1 second because of precision
     *                requirements.
     */
    private String unit = null;
   
    /**
     * @dtf.attr id 
     * @dtf.attr.desc the id attribute is used to generate the events being 
     *                thrown about the work that was and wasn't completed during
     *                the execution of this distribution. Those events are 
     *                defined in the previous event section. The id also makes 
     *                the property ${[your distribute id].worker} available and
     *                allows you to identify during each execution which worker 
     *                (i.e. thread) was handling the request....
     */
    private String id = null;

    public Distribute() { }

    public void execute() throws DTFException {
        int workerCount = getWorkers(); 
        Range range = null;
        
        if ( workerCount == -1 ) { 
            // range should be defined
            if ( getRange() == null ) 
                throw new ParseException("range attribute must be set.");
            
            range = RangeFactory.getRange(getRange());
            workerCount = range.size();
        }

        Sequence children = new Sequence();
        children.addActions(children());
        
        DistWorker[] workers =  new DistWorker[workerCount];
        DistributionFactory df = DistributionFactory.getInstance();
        Distribution dist = null;
        String id = getId();
        long iterations = getIterations();
        String property = getProperty();

        long interval = -1;
        
        if (getTimer() != null)
            interval = TimeUtil.parseTime("timer",getTimer());
        
        long unitWork = -1;

        if (getUnit() != null)
            unitWork = TimeUtil.parseTime("unit",getUnit());

        if (getFunc() != null)
            dist = df.getDistribution(getFunc());
        
        DistWorkState dstate = new DistWorkState();

        if ( range != null ) {
            int count = 0;
            while ( range.hasMoreElements() ) {
                String wid = range.nextElement();
	            DTFState state = getState().duplicate();
	          
	            if (id != null)  
	                state.getConfig().setProperty(id + ".worker", "" + wid, true);
	            
	            workers[count++] = new DistWorker(children, dstate, state);
            }
        } else { // obviously at this point we have workerCount set
	        for (int i = 0 ; i < workerCount; i++)  {
	            DTFState state = getState().duplicate();
	           
	            if (id != null)  
	                state.getConfig().setProperty(id + ".worker", "" +i, true);
	            
	            workers[i] = new DistWorker(children, dstate,  state);
	        }
        }
        
        for (int i = 0 ; i < workerCount; i++)
            workers[i].start();

        long counter = 0;
        long currentTime = 0;
        long start = System.currentTimeMillis();
        
        while ((iterations == -1 || counter < iterations) && 
               (interval == -1 || System.currentTimeMillis() - start < interval)) {

            if (dist != null) {
                long workDone = 0;
                long elapsedTime = 0;
                long workGoal = dist.result(currentTime);
                long unitStart = System.currentTimeMillis();
                
                while ( workDone < workGoal && elapsedTime < unitWork && 
                        (iterations == -1 || counter < iterations) && 
                        (interval == -1 || (System.currentTimeMillis() - start) < interval)) {
                    Sequence sequence = new Sequence();
                   
                    if ( property != null ) {
                        sequence.addAction(new Property(property,
                                                        ""+counter,
                                                        true));
                    }
                    
                    dstate.wakeUp(sequence);
                    
                    workDone++;
                    counter++;
                    elapsedTime = (System.currentTimeMillis() - unitStart);
                }
                
                if (id != null) {
                    Event event = new Event(id);
                    event.addAttribute("workunit", currentTime);
                    event.addAttribute("workdone", workDone);
                    event.addAttribute("workgoal", workGoal);
                    event.start();
                    event.stop();
                    Action.getRecorder().record(event);
                }
                
                if ( elapsedTime < unitWork )
                    ThreadUtil.pause(unitWork-elapsedTime);
                
                currentTime++;
            } else { 
                Sequence sequence = new Sequence();
                   
                if ( property != null ) 
                    sequence.addAction(new Property(property, "" + counter, true));
                
                dstate.wakeUp(sequence);
                counter++;
            }
        }
        
        dstate.allDone();
        /*
         * Wait for all underlying threads to terminate and log all of them 
         * except the last exception which we can throw to any parent tag to 
         * handle the error.
         */
        DTFException lastexception = null;
        boolean interrupted = false; 
        for (int index = 0; index < workers.length; index++) {
            try { 
                workers[index].waitFor();
            } catch (InterruptionException e) { 
                if ( getLogger().isDebugEnabled() ) {
                    getLogger().debug("Thread interrupted [" + 
                                      workers[index].getName() + "]",e);
                }
                interrupted = true;
            } catch (DTFException e) { 
                if ( lastexception != null ) 
                    getLogger().error("Child failed.", lastexception);
                
                lastexception = e;
            }
        }

        if ( interrupted && getLogger().isDebugEnabled() ) 
            getLogger().debug("Execution interrupted.");
        
        if ( lastexception != null ) 
            throw lastexception;
    }

    public String getProperty() throws ParseException { return replaceProperties(property); }
    public void setProperty(String property) { this.property = property; }

    @Deprecated 
    public int getWorkers() throws ParseException { return toInt("workers", replaceProperties(workers),-1); }
    /**
     * @deprecated use the attribute range to replace this, like so range="0..workers"
     */
    @Deprecated
    public void setWorkers(String workers) { this.workers = workers; }
    
    public String getRange() throws ParseException { return replaceProperties(range); }
    public void setRange(String range) throws ParseException { this.range = range; }

    public String getTimer() throws ParseException { return replaceProperties(timer); }
    public void setTimer(String timer) { this.timer = timer; }
    
    public String getUnit() throws ParseException { return replaceProperties(unit); }
    public void setUnit(String unit) { this.unit = unit; }
    
    public long getIterations() throws ParseException { return toLong("iterations", replaceProperties(iterations),-1); }
    public void setIterations(String iterations) { this.iterations = iterations; }

    public String getFunc() throws ParseException { return replaceProperties(func); }
    public void setFunc(String func) { this.func = func; }

    public String getId() throws ParseException { return replaceProperties(id); }
    public void setId(String id) { this.id = id; }
}
