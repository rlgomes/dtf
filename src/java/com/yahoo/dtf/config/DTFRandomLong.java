package com.yahoo.dtf.config;

import java.util.Random;

import com.yahoo.dtf.config.DynamicProperty;

public class DTFRandomLong implements DynamicProperty {
    
    public static final String DTF_RANDOMLONG = "dtf.randomLong";
    
    private static Random rand = new Random(System.currentTimeMillis());

    public String getValue(String args) {
        if ( args != null ) {
            int index = args.indexOf(',');
            if ( index == -1 ) { 
                Long upper = new Long(args);
                return "" + rand.nextLong() % upper;
            } else {
                Long lower = new Long(args.substring(0,index));
                Long upper = new Long(args.substring(index+1));
                return "" + (lower + (Math.abs(rand.nextLong()) % (upper - lower)));
            }
        } else {
            return "" + rand.nextLong();
        }
    }
}
