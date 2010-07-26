package com.yahoo.dtf.streaming;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.util.HashUtil;

public abstract class DTFInputStream extends InputStream {
  
    private static int BUFFER_SIZE = 8*1024;
    private static long BIGSTREAM_SIZE = 1024*1024;
    
    private boolean calcHash = false;
    private boolean savedata = false;
   
    private byte[] data = null;
    
    private long _read = 0;
    private long _size = 0;

    private int i = 0;
  
    /*
     * MessageDigest class and buffer used to not update the hashing algorithm
     * on every read but instead closer to every 32KB of read data. 
     */
    private MessageDigest md = null;
    private byte[] _buffer = null;
    private int _count = 0;
    
    public DTFInputStream(long size, String args) throws ParseException {
        // save the original value that generated this stream, this way when we
        // print this stream or transfer it to another component we'll send the
        // original value used to generate data and not stream the data itself.
        _size = size;
    }

    /**
     * Return the next byte in from this InputStream or return -1 if we've 
     * reached the end of stream.
     * 
     * @return
     * @throws IOException
     */
    public abstract int readByte() throws IOException;

    /**
     * Return the number of bytes read and place them in the byte[] buffer that 
     * was passed as an argument. Make sure to fill the buffer completely for 
     * best efficiency otherwise make sure to return the exact amount of bytes 
     * you really placed in the buffer.
     * 
     * @return
     * @throws IOException
     */
    public abstract int readBuffer(byte[] buffer, int offset, int length) throws IOException;

    public long getSize() { return _size; } 
   
    public void setHashAlgorithm(String algorithm) throws ParseException { 
        try {
            /*
             * Only initialize the hashing buffer if we're actually going to be 
             * using some hash algorithm
             */
            _buffer = new byte[32*1024];
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new ParseException("Unable to set hashing algorithm.",e);
        }
    }
    
    public void setCalcHash(boolean calcHash) throws ParseException { 
        if ( calcHash && md == null )
            setHashAlgorithm("SHA-1");
        
        this.calcHash = calcHash;
    }
    
    public String getHash() throws ParseException { 
        if ( calcHash ) { 
            if ( _count > 0 ) { 
                md.update(_buffer,0,_count);
            } 
            return HashUtil.convertToHex(md.digest());
        } else { 
            throw new ParseException("This DTFInputStream was not set to calculate hash.");
        }
    } 
    
    public void setSaveData(boolean savedata) { this.savedata = savedata; }
    
    public String getData() throws ParseException {
        if ( !calcHash ) { 
            if ( _size == 0 )
                return "";
            else 
                return new String(data);
        } else { 
            throw new ParseException("This DTFInputStream was not set to calculate hash.");
        }
    }
   
    public final int read() throws IOException {
        if ( _read >= _size )
            return -1;

        int result = readByte();
        
        if ( result != -1 ) {
	        if ( calcHash ) { 
                md.update((byte)result);
	        } else if ( savedata ) { 
	            if ( data == null ) 
	                data = new byte[(int)getSize()];
	            
	            data[i++] = (byte)result;
	        }
        }
        _read++;
        return result;
    }
    
    public int read(byte[] b) throws IOException {
        if ( _read >= _size )
            return -1;
       
        int onlyread = b.length;
       
        int diff = (int)(_size - _read);
        
        if ( diff < onlyread ) 
            onlyread = diff;

        int read = readBuffer(b, 0, onlyread);
        
        if ( calcHash ) { 
            if ( _count + read > _buffer.length ) { 
                md.update(_buffer,0,_count);
                _count = 0;
            } 
            System.arraycopy(b, 0, _buffer, _count, read);
            _count+=read;
        } else if ( savedata ) { 
            if ( data == null ) 
                data = new byte[(int)getSize()];
            
            System.arraycopy(b, 0, data, 0, read);
        }
        
        _read+=read;
        return read;
    }
    
    /*
     * XXX: this way we'll catch where the toString is being done and fix it 
     *      here to return the DTFStream signature for serialization to the other
     *      components.
     */
    public String toString() {
        throw new RuntimeException("This shouldn't be called.");
    }
   
    @Override
    public void close() throws IOException {
        _read = 0;
        _count = 0;
        i = 0;
        super.close();
        reset();
    }
    
    /**
     * 
     * @return
     * @throws ParseException
     */
    public String getAsString() throws ParseException { 
        long size = getSize();
        StringBuffer result = new StringBuffer((int)size);
     
        if ( size > BIGSTREAM_SIZE ) { 
            Action.getLogger().warn("Using a DTFStream as a String that is " +  
                                    size + " bytes can be quite inefficient. " + 
                                    "There are better ways of streaming large" + 
                                    " amounts of data in DTF.");
        }
   
        byte[] buffer = new byte[BUFFER_SIZE];
        try {
            int read = 0;
            while ( (read = super.read(buffer)) != -1 ) { 
                result.append(new String(buffer, 0, read));
            }
            // closing actually resets the underlying buffers and internally
            // maintained counters
            close();
        } catch (IOException e) {
            throw new ParseException("Unable to read InputStream.",e);
        }
        
        return result.toString();
    }
}
