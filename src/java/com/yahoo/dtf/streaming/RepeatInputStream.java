package com.yahoo.dtf.streaming;

import java.io.IOException;

import com.yahoo.dtf.exception.ParseException;

/** 
 * @dtf.feature Repeat Stream Type
 * @dtf.feature.group DTF Properties
 * 
 * @dtf.feature.desc
 * <p>
 * The repeat stream type generates a sequence of data that repeats the pattern
 * you specified till it has generated the desired size.
 * </p>
 * <p>
 * Using the repeat streaming property is as simple as referencing the property
 * like so:
 * </p>
 * <pre>
 * ${dtf.stream(repeat,1024,ABC)}
 * </pre>
 * <p>
 * The previous property defines a repeat stream of data that has 1024 bytes in 
 * length and will repeat the string ABC until the desired length has been 
 * reached. The data generated can always be re-generated using  the same seed.
 * This means you only have to store the size & seed to be able to do data 
 * validation later on and not have to save the data itself.
 * </p>
 */
public class RepeatInputStream extends DTFInputStream {

    private int BUFFER_SIZE = 32*1024;
    
    private String _pattern = null;
    private int _read = 0;
    private long _size = 0;
    private int _patternLength = 0;
    
    private byte[] _buffer = null;
    
    public RepeatInputStream(long size, String[] args) throws ParseException { 
        // save the signature incase we need to serialize this
        super(size, args);
      
        if ( args.length > 0 ) 
            _pattern = args[0];
        
        _patternLength = _pattern.length();
      
        // for small data streams no need to create buffers that are larger
        // than our original data
        if ( size < BUFFER_SIZE )
            BUFFER_SIZE = (int)size;
        
        _size = size;
        
        _buffer = new byte[BUFFER_SIZE];
        for (int i = 0; i < BUFFER_SIZE; i++) 
            _buffer[i] = (byte)_pattern.charAt(i % _patternLength);
    }
 
    @Override
    public int read() throws IOException {
        if ( _read >= getSize() ) 
            return -1;
            
        return _buffer[_read++ % BUFFER_SIZE];
    }

    @Override
    public int read(byte[] buffer, int offset, int length) {
        if ( _read >= getSize() )
            return -1;

        int diff = (int)(_size - _read);
        int onlyread = length;
        
        if ( diff < onlyread ) 
            onlyread = diff;
            
        int totalread = 0;
        int read =  0;
        while ( totalread < onlyread ) { 
            int start = _read % BUFFER_SIZE;
            read = BUFFER_SIZE - start;
            
            if ( totalread + read > length ) 
                read = length - totalread;
            
            System.arraycopy(_buffer,
                             start,
                             buffer,
                             totalread + offset,
                             read);
            totalread += read;
            _read += read;
        }
        
        return totalread;
    }
    
    @Override
    public int read(byte[] buffer) throws IOException {
        int length = buffer.length;
        if ( buffer.length > (getSize()-_read) ) 
            length = (int)(_size - _read);
        
        return read(buffer,0,length);
    }
    
    public String getAsString() throws ParseException {
        return super.getAsString();
    }
    
    @Override
    public synchronized void reset() throws IOException {
        _read = 0;
    }
}
