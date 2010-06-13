package com.yahoo.dtf.recorder.internal;

import com.yahoo.dtf.DTFConstants;
import com.yahoo.dtf.recorder.Event;
import com.yahoo.dtf.recorder.Recorder;
import com.yahoo.dtf.recorder.RecorderBase;
import com.yahoo.dtf.config.Config;
import com.yahoo.dtf.exception.RecorderException;

/**
 * Utility class used at certain points in the framework to gaurantee that the 
 * events thrown in another thread can be recorded into the parent thread.
 * 
 * @author rlgomes
 *
 */
public class ConfigRecorder extends RecorderBase {
   
    private Config _config = null;
    
    public ConfigRecorder(Config config) { 
        super(null,false,DTFConstants.DEFAULT_ENCODING);
        _config = config;
    }
    
    public void stop() throws RecorderException {}
    public void start() throws RecorderException {}
    public void record(Event event) throws RecorderException {
        Recorder.eventToConfig(event, _config);
    }
}
