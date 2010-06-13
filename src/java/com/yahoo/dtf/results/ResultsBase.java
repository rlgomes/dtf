package com.yahoo.dtf.results;

import java.net.URI;

import com.yahoo.dtf.results.Result;
import com.yahoo.dtf.exception.ResultsException;

public abstract class ResultsBase {
   
    private boolean _savelogs = false;
  
    private URI _uri = null;
    /**
     *  
     * @param savelogs Identify if you want the log output to be saved to the
     *                 same location as your getURI specifies but with the name
     *                 script-[your_script-id].out, the actual log file name 
     *                 will also saved to the property DTFProperties.DTF_TESTCASE_LOG
     *                 <br/> 
     *                 This is useful if you want to reference the output files
     *                 from your own output format.
     */
    public ResultsBase(URI uri, boolean savelogs) { 
       _savelogs = savelogs; 
       _uri = uri;
    }
    
    public URI getURI() { return _uri; }
    
    public boolean saveLogs() { return _savelogs; }
  
    /**
     * 
     * @param result
     * @throws ResultsException
     */
    public abstract void recordResult(Result result) throws ResultsException;
    
    /**
     * 
     * @throws ResultsException
     */
    public abstract void start() throws ResultsException;
    
    /**
     * 
     * @throws ResultsException
     */
    public abstract void stop() throws ResultsException;
}
