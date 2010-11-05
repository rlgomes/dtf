package com.yahoo.dtf.actions.basic;

import com.yahoo.dtf.DTFConstants;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.results.Result;

/**
 * @dtf.tag script
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc This tag is the root of all DTF testcases and should always 
 *               have the name attribute set to a unique identifier that can 
 *               be easily used to identify your test.
 * 
 * @dtf.tag.example 
 * <script name="mytestcase">
 *      <info>
 *          <author>
 *              <name>Some Authore</name>
 *             <email>author@server.com</email>
 *         </author>
 *         <description>DTF test.</description>
 *      </info>
 * </script>
 * 
 */
public class Script extends Action {

    /**
     * @dtf.attr name
     * @dtf.attr.desc The name attribute is used to specify a unique name for 
     *                the testcase being defined.
     */
    private String name = null;

    public Script() {}
    
    public void execute() throws DTFException { 
        getConfig().setProperty(DTFConstants.SCRIPT_ID,getName());
        
        Result result = new Result(getName());
        result.setTestcase();
        result.start();
        
        try {
            executeChildren();
            result.stop();
            result.setPassResult();
        } catch (DTFException e) { 
            result.stop();
            result.setFailResult(e);
            throw e;
        } finally { 
            if (result.getStop() == -1)
                result.stop();
            
            getResults().recordResult(result);

            if ( getLogger().isDebugEnabled() ) { 
	            getLogger().debug(getFilename() + " took " + 
	                              result.getDurationInMilliSeconds() + "ms");
            }
        }
    }
   
    public String getName() throws ParseException { return replaceProperties(name); }
    public void setName(String name) { this.name = name; }
}
