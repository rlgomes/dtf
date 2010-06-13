package com.yahoo.dtf.results;

import java.net.URI;

import com.yahoo.dtf.logger.DTFLogger;
import com.yahoo.dtf.results.Result;
import com.yahoo.dtf.results.ResultsBase;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.ResultsException;

public class ConsoleResults extends ResultsBase  {

    private DTFLogger _logger = null;
    
    public ConsoleResults(URI uri, boolean savelogs) {
        super(null,false);
        _logger = Action.getLogger();
    }
   
    @Override
    public URI getURI() { throw new RuntimeException("Method not implemented."); }
    
    public void start() throws ResultsException { }
    public void stop() throws ResultsException { }

    public void recordResult(Result result) throws ResultsException {
        StringBuffer message = new StringBuffer();
        
        if (result.isTestSuite()) { 
            message.append("Testsuite: ");
            message.append(result.getName());
            message.append((result.isPassResult() ? " passed." : " failed. "));
                    
        } else if (result.isTestCase()) { 
            message.append("Testcase: ");
            message.append(result.getName());
            message.append((result.isPassResult() ? " passed." : " failed."));
        }
        
        message.append(" ");
        
        if (result.getNumPassed() != 0) 
            message.append(result.getNumPassed() + " passed");
        
        if (result.getNumFailed() != 0) 
            message.append(", " + result.getNumFailed() + " failures");
        
        if (result.getNumSkipped() != 0)  
            message.append(", " + result.getNumSkipped() + " skipped"); 
        
        message.append(" testcases.");
                           
        if (message.length() != 0)
            _logger.info(message.toString());
    }
}
