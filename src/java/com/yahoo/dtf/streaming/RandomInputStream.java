package com.yahoo.dtf.streaming;

import java.io.IOException;

import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.util.DTFRandom;

/** 
 * @dtf.feature Random Stream Type
 * @dtf.feature.group DTF Properties
 * 
 * @dtf.feature.desc
 * <p>
 * The random stream type generates a random sequence of bytes of the size that
 * you choose with the optional seed you specify. The actual random data 
 * follows the details explained in the {@dtf.link Generating Random Data} 
 * section. 
 * </p>
 * <p>
 * Using the random streaming property is as simple as referencing the property
 * like so:
 * </p>
 * <pre>
 * ${dtf.stream(random,1024,1234)}
 * </pre>
 * <p>
 * The previous property defines a random stream of data that has 1024 bytes in 
 * length and has a random seed of 1234. This pseudo random data is generated
 * following some rules as explained earlier in this document and can always
 * be re-generated using the same seed. This means you only have to store the
 * size & seed to be able to do data validation later on and not have to save 
 * the data itself.
 * </p>
 * 
 */
public class RandomInputStream extends DTFInputStream {

    private DTFRandom _random = null;
  
    private int _read = 0;
    private long _seed = 0;
    private long _size = 0;
    
    public RandomInputStream(long size, String[] args) throws ParseException { 
        super(size, args);
     
        if ( args.length > 0 ) {
            try { 
                _seed = new Long(args[0]);
            } catch (NumberFormatException e ) { 
                throw new ParseException("Random input argument should be a long not [" + 
                                         args + "]");
            }
        }
        
        _random = new DTFRandom(_seed);
        _size = size;
    }

    @Override
    public int read() throws IOException {
        if ( _read >= getSize() )
            return -1;
            
        _read++;
        return _random.nextInt();
    }
   
    @Override
    public int read(byte[] buffer, int offset, int length) {
        if ( _read >= getSize() ) 
            return -1;

        int diff = (int)(_size - _read);
        int onlyread = length;
        
        if ( diff < onlyread ) 
            onlyread = diff;
        
        byte[] bytes = new byte[length];
        _random.nextBytes(bytes);
        System.arraycopy(bytes,
                         0,
                         buffer,
                         offset,
                         onlyread);
        _read+=onlyread;
        return onlyread;
    }
    
    @Override
    public int read(byte[] buffer) throws IOException {
        int length = buffer.length;
        
        if ( buffer.length > (_size - _read) ) 
            length = (int)(_size - _read);
        
        return read(buffer,0,length);
    }

    public String getAsString() {
        byte[] bytes = new byte[(int)getSize()];
        _random.nextBytes(bytes);
        return new String(bytes);
    }
    
    @Override
    public synchronized void reset() throws IOException {
        _read = 0;
    }
}
