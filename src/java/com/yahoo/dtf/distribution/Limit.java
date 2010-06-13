package com.yahoo.dtf.distribution;

import com.yahoo.dtf.distribution.Distribution;
import com.yahoo.dtf.exception.DistributionException;

public class Limit implements Distribution {
  
    private long _limit = 0;
    
    private Distribution _distribution = null;
    
    public Limit(String argument) throws DistributionException {
        int lastindex = argument.lastIndexOf(',');
        if ( lastindex == -1 ) 
            throw new DistributionException("Limit function should have 2 arguments: limit(function,limit_amount).");
        
        String limit = argument.substring(lastindex+1);
        _limit = Long.valueOf(limit);
        
        String func = argument.substring(0,lastindex);
        _distribution = DistributionFactory.getInstance().getDistribution(func);
    }

    public long result(long time) {
        long value = _distribution.result(time);
        if ( value > _limit ) value = _limit;
        return value;
    }
}
