package com.yahoo.dtf.streaming;

import java.io.IOException;

import com.yahoo.dtf.exception.ParseException;

public class StringInputStream extends DTFInputStream {

    private String value = null;
    private int _read = 0;
    private long _size = 0;
    
    public StringInputStream(String value) throws ParseException { 
        this(value.length(),new String[]{value});
    }
    
    public StringInputStream(long size, String[] args) throws ParseException { 
        super(size,args);
        
        if ( args.length > 0 ) 
            this.value = args[0];
        _size = size;
    }
   
    @Override
    public int read() throws IOException {
        if ( _read >= _size )
            return -1;
        
        return value.charAt(_read++);
    }
   
    @Override
    public int read(byte[] buffer, int offset, int length)
            throws IOException {
        if ( _read >= _size )
            return -1;
       
        int i = 0;
        for (i = offset; i < length && _read < _size ; i++) 
            buffer[i] = (byte)value.charAt(_read++);
        
        return i;
    }
    
    @Override
    public int read(byte[] b) throws IOException {
        return read(b,0,b.length);
    }
    
    @Override
    public synchronized void reset() throws IOException {
        _read = 0;
    }
}
