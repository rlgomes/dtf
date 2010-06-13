package com.yahoo.dtf.util.streams;

import java.io.InputStream;
import java.io.OutputStream;

import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.util.ByteUtil;

public abstract class Throttler {
   
    /**
     * Method gets the amount of bytes about to be moved and can make the 
     * decision to block for a small period of time in order to respect whatever
     * the throttling goal is. 
     *  
     * @param bytecount
     */
    public abstract void update(int bytecount);
    
    public static InputStream wrapInputStream(InputStream is, String bandwidth)
                  throws ParseException {
        long bps = ByteUtil.parseBytes("bandwidth", bandwidth);
        return new ThrottledInputStream(is, bps);
    }

    public static OutputStream wrapOutputStream(OutputStream os, String bandwidth)
                  throws ParseException {
        long bps = ByteUtil.parseBytes("bandwidth", bandwidth);
        return new ThrottledOutputStream(os, bps);
    }
}
