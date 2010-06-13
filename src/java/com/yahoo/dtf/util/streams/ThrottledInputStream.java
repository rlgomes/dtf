package com.yahoo.dtf.util.streams;

import java.io.IOException;
import java.io.InputStream;

/**
 * Utility InputStream Throttling class used to throttle the amount of data 
 * being read from an InputStream. Currently it accepts the bytes per second
 * directly as an argument or you can specify your own implementation of a 
 * Throttler implementation that can vary over time or do something else that 
 * you may require.
 * 
 */
public class ThrottledInputStream extends InputStream {
   
    private InputStream _is = null;
    private Throttler _throttler = null;
    
    public ThrottledInputStream(InputStream is, long bps) {
        _throttler = new ConstThrottler(bps);
        _is = is;
    }

    public ThrottledInputStream(InputStream is, Throttler throttler) {
        _throttler = throttler;
        _is = is;
    }
    
    @Override
    public int read() throws IOException {
        _throttler.update(1);
        return _is.read();
    }
    
    @Override
    public void close() throws IOException {
        _throttler.update(-1);
        _is.close();
    }
}
