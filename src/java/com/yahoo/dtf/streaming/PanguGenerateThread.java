package com.yahoo.dtf.streaming;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.pangu.PanguException;
import org.pangu.PanguGen;
import org.pangu.tree.GenInfo;

public class PanguGenerateThread extends Thread {

    private PanguGen gen = null;
    private GenInfo gi = null;
    private PipedOutputStream os = null;
    private PipedInputStream is = null;
    
    private boolean alive = true;
    
    private Object mutex = new Object();
    
    public PanguGenerateThread(PanguGen gen) {
        this.gen = gen;
    }
    
    public void newtask(GenInfo gi,
                        PipedOutputStream os,
                        PipedInputStream is) {
        synchronized(mutex) { 
	        this.gi = gi;
	        this.os = os;
	        this.is = is;
        
            mutex.notify();
        }
    }
    
    public void cancel() { 
        alive = false;
        synchronized(mutex) { 
            mutex.notify();
        }
    }
    
    @Override
    public void run() {
        while ( alive ) { 
	        synchronized(mutex) { 
	            if ( gi != null ) { 
			        try {
			            gen.generate(os, gi);
			        } catch (PanguException e) {
			            throw new RuntimeException("Error generating data.",e);
			        }
			        gi = null;
	            } 
	            try { mutex.wait(); } catch (InterruptedException e) { }
	        }
        }
    }
}
