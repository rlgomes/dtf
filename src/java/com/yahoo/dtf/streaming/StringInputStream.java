package com.yahoo.dtf.streaming;

import java.io.IOException;

import com.yahoo.dtf.exception.ParseException;

public class StringInputStream extends DTFInputStream {

    private String value = null;
    private int _count = 0;
    
    public StringInputStream(String value) throws ParseException { 
        this(value.length(),value);
    }
    
    public StringInputStream(long size, String value) throws ParseException { 
        super(size,value);
        this.value = value;
    }
    
    public int readByte() throws IOException {
        return value.charAt(_count++);
    }
    
    public int readBuffer(byte[] buffer, int offset, int length)
            throws IOException {
       
        for (int i = offset; i < length; i++) 
            buffer[i] = (byte)value.charAt(_count++);
        
        return length;
    }
    
    @Override
    public synchronized void reset() throws IOException {
        _count = 0;
    }
}
