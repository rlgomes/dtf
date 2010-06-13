package com.yahoo.dtf.distribution;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yahoo.dtf.distribution.Constant;
import com.yahoo.dtf.distribution.Distribution;
import com.yahoo.dtf.distribution.DistributionFactory;
import com.yahoo.dtf.distribution.List;
import com.yahoo.dtf.distribution.Step;
import com.yahoo.dtf.exception.DistributionException;


public class DistributionFactory {

    private static DistributionFactory _instance = null;
    
    private DistributionFactory() { }
    
    public static synchronized DistributionFactory getInstance() { 
        if (_instance == null) 
            _instance = new DistributionFactory();
        
        return _instance;
    }
    
    public Distribution getDistribution(String function) throws DistributionException { 
        Distribution distribution = null;
        
        Pattern pattern = Pattern.compile("([^(]*)(.*)");
        Matcher matcher = pattern.matcher(function);
        
        if (matcher.matches()) { 
            String func = matcher.group(1).toLowerCase();
            String argument = matcher.group(2);

            if ( argument.length() > 2 )
                argument = argument.substring(1,argument.length()-1);
            
            if (func.equals("const")) { 
                // Constant function
                distribution = new Constant(argument);
            } else if (func.equals("step")) { 
                // Step function
                distribution = new Step(argument);
            } else if (func.equals("list")) {
                // List function
                distribution = new List(argument);
            } else if (func.equals("limit")) {
                // Limit function 
                distribution = new Limit(argument);
            } else {
                throw new DistributionException("Unable to parse distribution [" + 
                                            function + "]");
            }
        } else 
            throw new DistributionException("Unable to parse distribution [" + 
                                            function + "]");
        
        return distribution;
    }
}
