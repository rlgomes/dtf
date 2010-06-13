package com.yahoo.dtf.util.streams;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Utility OutputStream Throttling class used to throttle the amount of data 
 * being sent by an OutputStream. Currently it accepts the bytes per second
 * directly as an argument or you can specify your own implementation of a 
 * Throttler implementation that can vary over time or do something else that 
 * you may require.
 * 
 */
public class ThrottledOutputStream extends OutputStream {
   
    private OutputStream _os = null;
    private Throttler _throttler = null;
    
    public ThrottledOutputStream(OutputStream os, long bps) {
        _throttler = new ConstThrottler(bps);
        _os = os;
    }

    public ThrottledOutputStream(OutputStream os, Throttler throttler) {
        _throttler = throttler;
        _os = os;
    }
    
    @Override
    public void write(int b) throws IOException {
        _throttler.update(1);
        _os.write(b);
    }
    
    @Override
    public void close() throws IOException {
        _throttler.update(-1);
        _os.close();
    }
}
