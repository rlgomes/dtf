package com.yahoo.dtf.distribution;

/**
 * Simple interface that defines what a distribution function must return for
 * a specific unit of time. This function must always return the same value for
 * the same value of time.
 * 
 * @author rlgomes
 */
public interface Distribution {
    public long result(long time);
}
