package com.yahoo.dtf.results;

import java.net.URI;

import com.yahoo.dtf.results.Result;
import com.yahoo.dtf.results.ResultsBase;
import com.yahoo.dtf.exception.ResultsException;

public class NullResults extends ResultsBase  {

    public NullResults() {  super(null,false); } 

    @Override
    public URI getURI() { throw new RuntimeException("Method not implemented."); }

    public void start() throws ResultsException { }
    public void stop() throws ResultsException { }

    public void recordResult(Result result) throws ResultsException { }
}
