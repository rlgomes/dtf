package com.yahoo.dtf.range;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.yahoo.dtf.exception.RangeException;

public abstract class Range {
  
    public Range() { 
        
    }
  
    /**
     * returns a boolean that specifies if the Range has anymore 
     * elements to return or if it has reached its end. 
     * @return
     */
    public abstract boolean hasMoreElements();

    /**
     * 
     * @return
     */
    public abstract String nextElement() throws RangeException;
    
    /**
     * Reset the Range back to the original state so that this range 
     * can be used as if it were a new range just created for the first
     * time. 
     */
    public abstract void reset() throws RangeException;
    
    /**
     * size method used to return the size of the Range. 
     * @return 
     */
    public abstract int size();
   
    /**
     * 
     * @param dos
     */
    public abstract void suspendState(DataOutputStream dos) throws RangeException;
   
    /**
     * 
     * @param dis
     */
    public abstract void restoreState(DataInputStream dis) throws RangeException;
}
