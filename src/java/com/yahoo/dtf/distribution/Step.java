package com.yahoo.dtf.distribution;

import com.yahoo.dtf.distribution.Distribution;
import com.yahoo.dtf.exception.DistributionException;

public class Step implements Distribution {
   
    private long _start;
    private long _stepSize;
    private long _stepDuration;
    
    public Step(String argument) throws DistributionException {
        String[] arguments = argument.split(",");
        
        if (arguments.length != 3) 
            throw new DistributionException("Step function takes 3 argument."); 
        
        _start           = new Long(arguments[0]).longValue();
        _stepSize        = new Long(arguments[1]).longValue();
        _stepDuration    = new Long(arguments[2]).longValue();
    }

    public long result(long time) {
        return _start + (time/_stepDuration) * _stepSize; 
    }
}
