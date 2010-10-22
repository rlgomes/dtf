package com.yahoo.dtf.streaming;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import com.yahoo.dtf.exception.ParseException;

public class StringInputStream extends DTFInputStream {

    private int _count = 0;
    private byte[] bytes = null;
    
    public StringInputStream(String value) throws ParseException { 
        this(value.length(),
             value,
             Charset.defaultCharset().displayName());
    }

    public StringInputStream(String value,String encoding)
           throws ParseException { 
        this(value.length(),value,encoding);
    }

    public StringInputStream(long size, String value, String encoding)
           throws ParseException { 
        super(size,value);
        try {
            this.bytes = value.getBytes(encoding);
            setSize(bytes.length);
        } catch (UnsupportedEncodingException e) {
            throw new ParseException("Error decoding",e);
        }
    }
    
    public int readByte() throws IOException {
        return bytes[_count++];
    }
    
    public int readBuffer(byte[] buffer, int offset, int length)
            throws IOException {
       
        for (int i = offset; i < length; i++) 
            buffer[i] = bytes[_count++];
        
        return length;
    }
    
    @Override
    public synchronized void reset() throws IOException {
        _count = 0;
    }
}
