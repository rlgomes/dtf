package com.yahoo.dtf.streaming;

import java.io.IOException;

import com.yahoo.dtf.exception.ParseException;

/**
 * Used internally to represent a simple string as an InputStream to some of the
 * code when it is looking for DTFInputStreams but there's just a text node.
 * 
 * @author rlgomes
 */
public class StringInputStream extends DTFInputStream {

    private byte[] value = null;
    private int _read = 0;
    
    public StringInputStream(String value) throws ParseException { 
        this(value.length(),new String[]{value});
    }
    
    public StringInputStream(long size, String[] args) throws ParseException { 
        super(size,args);
        
        if ( args.length > 0 )
            this.value = args[0].getBytes();
        
        setSize(value.length);
    }
   
    @Override
    public int read() throws IOException {
        if ( _read >= getSize() )
            return -1;
        
        return (byte)value[_read++];
    }
   
    @Override
    public int read(byte[] buffer, int offset, int length)
            throws IOException {
        if ( _read >= getSize()  )
            return -1;
      
        int i = 0; 
        
        for (i = offset; i < length && _read < getSize() ; i++) 
            buffer[i] = (byte)value[_read++];
        
        return i;
    }
   
    
    @Override
    public int read(byte[] buffer) throws IOException {
        return read(buffer, 0, buffer.length);
    }
    
    @Override
    public synchronized void reset() throws IOException {
        _read = 0;
    }
}
