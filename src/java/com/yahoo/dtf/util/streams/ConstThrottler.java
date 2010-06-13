package com.yahoo.dtf.util.streams;

public class ConstThrottler extends Throttler {
   
    private long _maxbps = 0;
    
    private long _lasttime = -1;
    private long _bytecount = 0;
    
    public ConstThrottler(long bps) {
        _maxbps = bps;
    }
    
    public void update(int bytecount) { 
        long time = System.currentTimeMillis();
       
        if ( _lasttime == -1 ) _lasttime = time;
         
        // only apply logic once we've at least gone through 128 bytes or we're 
        // done
        if ( _bytecount > 128 || bytecount == -1) { 
	        // +1  is easier than checking elapsed for 0 and then making it 1
	        long elapsed = time - _lasttime + 1; 
	           
	        long bps = _bytecount * 1000L / elapsed;
	        if ( bps > _maxbps ) {
	            long sleeptime = _bytecount * 1000L / _maxbps;
	                   
	            try {
	                Thread.sleep(sleeptime);
	            } catch (InterruptedException ignore) { }
	                    
	            _bytecount = 0;
	            _lasttime = System.currentTimeMillis();
	        }
        }
        
        _bytecount += bytecount;
    }
}
