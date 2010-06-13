package com.yahoo.dtf.streaming;

import java.io.IOException;

import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.util.DTFRandom;

public class RandomInputStream extends DTFInputStream {

    private DTFRandom _random = null;
  
    private int _read = 0;
    private long _seed = 0;
    
    public RandomInputStream(long size, String args) throws ParseException { 
        super(size, args);
       
        try { 
            _seed = new Long(args);
        } catch (NumberFormatException e ) { 
            throw new ParseException("Random input argument should be a long not [" + 
                                     args + "]");
        }
        _random = new DTFRandom(_seed);
    }

    public int readByte() throws IOException {
        return _random.nextInt();
    }
    
    public int readBuffer(byte[] buffer, int offset, int length) {
        byte[] bytes = new byte[length];
        _random.nextBytes(bytes);
        System.arraycopy(bytes,
                         0,
                         buffer,
                         offset,
                         length);
        _read+=length;
        return length;
    }

    public String getAsString() {
        byte[] bytes = new byte[(int)getSize()];
        _random.nextBytes(bytes);
        return new String(bytes);
    }
}
