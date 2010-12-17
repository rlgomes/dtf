package com.yahoo.dtf.streaming;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.util.HashUtil;

/**
 * This abstract class defines the basic DTF InputStream that you can extend 
 * from if you'd like to create your own streaming data. The stream is referenced
 * like so:
 * 
 * ${dtf.stream(type,size,args)}
 * 
 * where args is a continuation of the arguments to this streaming property that
 * will be passed to the constructor of your DTFInputStream implementation. 
 * 
 * This class adds a few features to the DTFInputStream you create to allow DTF
 * to calculate the hash of your data or save your data to a temporary buffer
 * for post process. Obviously you can't save large objects that will result
 * in the Java heap space being completely filled up.
 * 
 * @author rlgomes
 */
public class DTFInputStream extends InputStream {
  
    private static int BUFFER_SIZE = 8*1024;
    private static long BIGSTREAM_SIZE = 1024*1024;
    
    private boolean calcHash = false;
    private boolean savedata = false;
   
    private ByteArrayOutputStream data = new ByteArrayOutputStream();
   
    private long _read = 0;
    private long _size = 0;
    
    private InputStream _source = null;
  
    /*
     * MessageDigest class and buffer used to not update the hashing algorithm
     * on every read but instead closer to every 32KB of read data. 
     */
    private MessageDigest md = null;
    private byte[] _buffer = null;
    private int _unhashedCount = 0;
    
    public DTFInputStream(long size, String[] args) throws ParseException {
        _size = size;
    }
    
    public void setInputStream(InputStream src) { 
        _source = src;
    }

    public long getSize() { return _size; } 
   
    public void setSize(long size) { 
        _size = size;
    }
   
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
            if ( _unhashedCount > 0 ) { 
                md.update(_buffer,0,_unhashedCount);
            } 
            return HashUtil.convertToHex(md.digest());
        } else { 
            throw new ParseException("This DTFInputStream was not set to calculate hash.");
        }
    } 
    
    public void setSaveData(boolean savedata) { 
        this.savedata = savedata;
    }
    
    public String getData() throws ParseException {
        if ( !calcHash ) { 
            return data.toString();
        } else { 
            throw new ParseException("This DTFInputStream was not set to calculate hash.");
        }
    }
    
    @Override
    public int read() throws IOException {
        int result = _source.read();
        
        if ( result != -1 ) {
	        if ( calcHash ) { 
                md.update((byte)result);
	        } else if ( savedata ) { 
	            data.write(result);
	        }
        }
        _read++;
        return result;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int read = _source.read(b, off, len);

        if ( read == -1 ) 
            return -1;
        
        if ( calcHash ) { 
            if ( _unhashedCount + read > _buffer.length ) { 
                md.update(_buffer,0,_unhashedCount);
                _unhashedCount = 0;
            } 
            System.arraycopy(b, 0, _buffer, _unhashedCount, read);
            _unhashedCount+=read;
        } 
        
        if ( savedata ) { 
            data.write(b,0,read);
        }
       
        _read+=read;
        return read;
    }
   
    @Override
    public int read(byte[] buffer) throws IOException {
        return read(buffer, 0, buffer.length);
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
        _unhashedCount = 0;
       
        if ( _source != null )
            _source.close();
    }
    
    /**
     * 
     * @return
     * @throws ParseException
     */
    public String getAsString() throws ParseException { 
        long size = getSize();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
     
        if ( size > BIGSTREAM_SIZE ) { 
            Action.getLogger().warn("Using a DTFStream as a String that is " +  
                                    size + " bytes can be quite inefficient. " + 
                                    "There are better ways of streaming large" + 
                                    " amounts of data in DTF.");
        }
   
        byte[] buffer = new byte[BUFFER_SIZE];
        try {
            int read = 0;
            while ( (read = read(buffer)) != -1 ) { 
                baos.write(buffer,0,read);
            }
            // closing actually resets the underlying buffers and internally
            // maintained counters
            close();
        } catch (IOException e) {
            throw new ParseException("Unable to read InputStream.",e);
        }
        
        return new String(baos.toByteArray());
    }
}
