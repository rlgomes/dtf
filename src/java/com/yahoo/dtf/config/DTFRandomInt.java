package com.yahoo.dtf.config;

import java.util.Random;

import com.yahoo.dtf.config.DynamicProperty;
import com.yahoo.dtf.exception.ParseException;

public class DTFRandomInt implements DynamicProperty {
    
    public static final String DTF_RANDOMINT = "dtf.randomInt";

    private static Random rand = new Random(System.currentTimeMillis());

    public String getValue(String args) throws ParseException {
        if ( args != null ) {
            int index = args.indexOf(',');
            if ( index == -1 ) { 
                Integer upper = new Integer(args);
                return "" + rand.nextInt(upper);
            } else {
	            Integer lower = new Integer(args.substring(0,index));
	            Integer upper = new Integer(args.substring(index+1));
	            return "" + (lower + (rand.nextInt(upper - lower)));
            }
        } else {
            return "" + rand.nextInt();
        }
    }
}
