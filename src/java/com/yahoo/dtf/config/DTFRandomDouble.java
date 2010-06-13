package com.yahoo.dtf.config;

import java.util.Random;

import com.yahoo.dtf.config.DynamicProperty;

public class DTFRandomDouble implements DynamicProperty {
    
    public static final String DTF_RANDOMDOUBLE = "dtf.randomDouble";

    private static Random rand = new Random(System.currentTimeMillis());
    
    public String getValue(String args) {
        if ( args != null ) {
            int index = args.indexOf(',');
            if ( index == -1 ) { 
                Double upper = new Double(args);
                return "" + rand.nextDouble()*upper;
            } else {
                Double lower = new Double(args.substring(0,index));
                Double upper = new Double(args.substring(index+1));
                return "" + (lower + (rand.nextDouble()*(upper - lower)));
            }
        } else {
            return "" + rand.nextDouble();
        }
    }
}
