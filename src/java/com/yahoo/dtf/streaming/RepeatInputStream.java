package com.yahoo.dtf.streaming;

import java.io.IOException;

import com.yahoo.dtf.exception.ParseException;

public class RepeatInputStream extends DTFInputStream {

    private int BUFFER_SIZE = 32*1024;
    
    private String _pattern = null;
    private int _read = 0;
    private int _patternLength = 0;
    
    private byte[] _buffer = null;
    
    public RepeatInputStream(long size, String pattern) throws ParseException { 
        // save the signature incase we need to serialize this
        super(size, pattern);
        
        _pattern = pattern;
        _patternLength = _pattern.length();
      
        // for small data streams no need to create buffers that are larger
        // than our original data
        if ( size < BUFFER_SIZE )
            BUFFER_SIZE = (int)size;
        
        _buffer = new byte[BUFFER_SIZE];
        for (int i = 0; i < BUFFER_SIZE; i++) 
            _buffer[i] = (byte)_pattern.charAt(i % _patternLength);
    }
  
    /*
     * Fix first and see performance benefit.
     */
    public int readByte() throws IOException {
        return _buffer[_read++ % BUFFER_SIZE];
    }
    
    public int readBuffer(byte[] buffer, int offset, int length) {
        int totalread = 0;
        int read =  0;
        while ( totalread < length ) { 
            int start = _read % BUFFER_SIZE;
            read = BUFFER_SIZE - start;
            
            if ( totalread + read > length ) 
                read = length - totalread;
            
            System.arraycopy(_buffer,
                             start,
                             buffer,
                             totalread + offset,
                             read);
            _read+=length;
            totalread += read;
        }
        return length;
    }
    
    public String getAsString() {
        if ( getSize() == _buffer.length ) {
            return new String(_buffer);
        } else { 
            return super.getAsString();
        }
    }
}
