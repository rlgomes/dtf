package com.yahoo.dtf.config;

import java.util.Random;

import com.yahoo.dtf.config.DynamicProperty;
import com.yahoo.dtf.exception.ParseException;

public class DTFGaussianLong implements DynamicProperty {
    
    public static final String DTF_GAUSSIANLONG = "dtf.gaussianLong";

    private static Random rand = new Random(System.currentTimeMillis());

    public String getValue(String args) throws ParseException {
        if ( args != null ) {
            int index = args.indexOf(',');
            if ( index == -1 ) { 
                Long upper = new Long(args);
                return "" + (long)nextGaussian()*upper;
            } else {
                Long lower = new Long(args.substring(0,index));
                Long upper = new Long(args.substring(index+1));
                return "" + ((long)(lower + nextGaussian()*(upper - lower)));
            }
        } else {
            return "" + ((long)(Long.MIN_VALUE + nextGaussian()* (Long.MAX_VALUE - Long.MIN_VALUE)));
        }
    }
   
    /**
     * 
     * @return
     */
    public double nextGaussian() { 
        return Math.abs(rand.nextGaussian()) % 1.0;
    }
}
