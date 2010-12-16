package com.yahoo.dtf.streaming;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.pangu.PanguGen;
import org.pangu.tree.GenInfo;
import org.pangu.tree.decorators.PanguDecorator;

import com.yahoo.dtf.exception.ParseException;

public abstract class PanguInputStream extends DTFInputStream {

    private PanguGen gen = null;
    private String _xsd = null;
    private long _seed = 0;

    private PanguGenerateThread _thread = null;
    
    private PipedInputStream pis = null;
    
    private PanguDecorator _decorator = null;
    
    public PanguInputStream(long size,
                            String[] args,
                            PanguDecorator decorator) throws ParseException { 
        super(size,args);
        
        if ( args.length > 0 ) 
            _xsd = args[0];
        
        if ( _xsd == null ) 
            throw new ParseException("No XSD supplied");
        
        if ( args.length > 1 ) {
            String seed = args[1];
            _seed = new Long(seed);
        }
        
        _decorator = decorator;
        _thread = PanguGenPool.checkout(_xsd);
        init();
    }
    
    private void init() throws ParseException { 
        try { 
            GenInfo gi = new GenInfo(_decorator, getSize(), _seed);
            pis = new PipedInputStream(8*1024);
            PipedOutputStream pos = new PipedOutputStream(pis);
            _thread.newtask(gi, pos, pis);
        } catch (IOException e) {
            throw new ParseException("Error parsing XSD.",e);
        }
    }
    
    @Override
    public int read() throws IOException {
        return pis.read();
    }
    
    @Override
    public int read(byte[] buffer, int offset, int length) 
           throws IOException { 
        return pis.read(buffer,offset,length);
    }
    
    @Override
    public int read(byte[] b) throws IOException {
        return pis.read(b);
    }
    
    @Override
    public void close() throws IOException {
        PanguGenPool.checkin(_xsd, _thread);
    }
    
    @Override
    public synchronized void reset() throws IOException {
        try {
            init();
        } catch (ParseException e) {
            throw new IOException("Error resetting.",e);
        }
    }
}
