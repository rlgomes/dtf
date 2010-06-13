package com.yahoo.dtf.results;

import java.net.URI;

import com.yahoo.dtf.results.Result;
import com.yahoo.dtf.results.ResultsBase;
import com.yahoo.dtf.exception.ResultsException;

public class TestSuiteResults extends ResultsBase  {

    private Result _result = null;
    
    public TestSuiteResults(Result result) { 
        super(null,false);
        _result = result;
    } 

    @Override
    public URI getURI() { throw new RuntimeException("Method not implemented."); }

    public void start() throws ResultsException { }
    public void stop() throws ResultsException { }

    public void recordResult(Result result) throws ResultsException {
        _result.addResult(result);
    }
}
