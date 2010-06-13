package com.yahoo.dtf.distribution;

import com.yahoo.dtf.distribution.Distribution;
import com.yahoo.dtf.exception.DistributionException;

public class List implements Distribution {
   
    private long[] _values = null;
    
    public List(String argument) throws DistributionException { 
        String[] arguments = argument.split(",");
        
        _values = new long[arguments.length];
        int i = 0;
        try { 
            for(i = 0; i < arguments.length;i++) { 
                _values[i] = new Long(arguments[i]).longValue();
            }
        } catch (NumberFormatException e) { 
            throw new DistributionException("Error parsing long [" + 
                                            arguments[i] + "",e);
        }
    }

    public long result(long time) {
        return _values[(int)time % _values.length];
    }
}
