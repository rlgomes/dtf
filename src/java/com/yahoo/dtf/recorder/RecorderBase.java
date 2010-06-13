package com.yahoo.dtf.recorder;

import java.net.URI;

import com.yahoo.dtf.recorder.Event;
import com.yahoo.dtf.exception.RecorderException;

public abstract class RecorderBase {
  
    private URI _uri = null;
    protected boolean _append = true;
    private String _encoding = null;
    
    public RecorderBase(URI uri, boolean append, String encoding) {
        _uri = uri;
        _append = append;
        _encoding = encoding;
    }
    
    public boolean isAppend() { return _append; }
    public String getEncoding() { return _encoding; } 
    public URI getURI() { return _uri; }

    public abstract void record(Event event) throws RecorderException;
    public abstract void start() throws RecorderException;
    public abstract void stop() throws RecorderException;
}
