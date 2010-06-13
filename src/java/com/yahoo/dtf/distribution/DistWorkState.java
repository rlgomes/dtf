package com.yahoo.dtf.distribution;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.flowcontrol.Sequence;
import com.yahoo.dtf.exception.DTFException;

public class DistWorkState {

    private boolean running = true;
    private Semaphore workerready = null;
    private ArrayList<Sequence> work = null;
    
    public DistWorkState() { 
        workerready = new Semaphore(0);
        work = new ArrayList<Sequence>();
    }

    public synchronized void allDone() { 
        running = false;
        notifyAll();
    }
    
    public void wakeUp(Sequence sequence) throws DTFException { 
        try {
            workerready.acquire();
        } catch (InterruptedException e) {
            throw new DTFException("Unable to acquire semaphore.",e);
        }

        synchronized (this) {
            work.add(sequence);
            notify();
        }
    }

    public Sequence waitForWork() throws DTFException { 
        Action.checkInterruption();
        
        synchronized (this) { 
            if ( !running ) 
                return null;
            
            workerready.release();
            
            try {
                wait();
            } catch (InterruptedException e) {
                throw new DTFException("Interrupted.",e);
            }
            
	        if ( work.size() != 0 ) 
	            return work.remove(0);
	        
	        return null;
        }
    }
}
