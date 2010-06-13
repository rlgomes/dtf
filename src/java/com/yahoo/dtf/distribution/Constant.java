package com.yahoo.dtf.distribution;

import com.yahoo.dtf.distribution.Distribution;
import com.yahoo.dtf.exception.DistributionException;

public class Constant implements Distribution {
   
    private long _constant = -1;
    
    public Constant(String argument) throws NumberFormatException, DistributionException { 
        String[] arguments = argument.split(",");
        
        if (arguments.length != 1) 
            throw new DistributionException("Const function takes 1 argument."); 
        
        _constant = new Long(arguments[0]).longValue();
    }

    public long result(long time) {
        return _constant;
    }
}
