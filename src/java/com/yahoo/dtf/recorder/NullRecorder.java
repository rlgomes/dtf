package com.yahoo.dtf.recorder;

import com.yahoo.dtf.recorder.Event;
import com.yahoo.dtf.recorder.RecorderBase;
import com.yahoo.dtf.exception.RecorderException;

public class NullRecorder extends RecorderBase {
    public NullRecorder(boolean append) { super(null,append,null); }
    public void stop() throws RecorderException {}
    public void start() throws RecorderException {}
    public void record(Event counter) throws RecorderException {}
}
