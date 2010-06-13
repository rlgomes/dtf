package com.yahoo.dtf.debug;

import com.yahoo.dtf.DTFProperties;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.config.Config;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.logger.DTFLogger;
import com.yahoo.dtf.state.DTFState;

/**
 * @dtf.feature DTF Tracing
 * @dtf.feature.group Debugging
 * 
 * @dtf.feature.desc <p>
 *                   The tracing feature built into DTF allows you to easily be
 *                   able to trace all of the actions that are executed at 
 *                   runtime as well as any property resolving that happens at
 *                   runtime will be printed. Turning this on is as easy as 
 *                   setting the property <b>dtf.tracing</b> to true and results 
 *                   in the appearance of the following type of log lines:
 *                   </p>
 *                   <pre>
 *                   Trace           - EXEC main If {} at tests/ut/if.xml:100:9
 *                   </pre>
 *                   <p> 
 *                   The above line shows that the tag If was being executed by
 *                   the thread "main" and that this action came from the line
 *                   100 from the test at tests/ut/if.xml. The {} brackets don't
 *                   contain any properties because the If tag doesn't have any
 *                   properties associated with it. 
 *                   </p>
 *                   <pre>
 *                   Trace           - PROP main If "${test1}" to "true"
 *                   </pre>
 *                   <p>
 *                   The above line shows a property being resolved in the 
 *                   string ${test1} and that the result is true. We currently
 *                   only show the values of the properties that are resolved 
 *                   because this way you can easily tell if a property was used
 *                   by a previous tag or not. 
 *                   </p>
 *                   <p> 
 *                   This feature can be extended to include other information 
 *                   in the near future but for the time being lets keep this 
 *                   simple enough that any new user to DTF can easily understand
 *                   what is going on in their test when things are not running
 *                   as expected.
 *                   </p>
 */
public class Trace {

    private static DTFLogger _logger = DTFLogger.getLogger(Trace.class);
    
    public static boolean isEnabled() throws ParseException { 
        Config config = Action.getState().getConfig();
        return config.getPropertyAsBoolean(DTFProperties.DTF_TRACING, false);
    }
    
    public static void trace(Action action ) { 
        _logger.info("EXEC " + Thread.currentThread().getName() + " " + action
                     + action.getXMLLocation());
    }
    
    private static int MAX_STR_LEN = 1024;
    public static void tracePropertyResolution(String original,
                                               String resolved) { 
        // if there wasn't a change in value then there's no point in printing
        if ( !original.equals(resolved) ) {
            if (original.length() > MAX_STR_LEN ) 
                original = original.substring(0,MAX_STR_LEN);

            if (resolved.length() > MAX_STR_LEN ) 
                resolved = resolved.substring(0,MAX_STR_LEN);
            

            DTFState state = Action.getState();
	        _logger.info("PROP " + Thread.currentThread().getName() + " " +  
	                     state.getAction().getClassName() + " \"" + 
	                     original + "\" to \"" + resolved + "\"");
        }
    }
}
