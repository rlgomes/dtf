package com.yahoo.dtf.stats;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class EventCalc {
    
    private HashMap<String, PropCalc> map = new HashMap<String, PropCalc>();

    private long min = -1;
    private long max = -1;

    private long start = -1;
    private long stop = -1;

    private long minDuration = -1;
    private long maxDuration = -1;
    
    private long accDuration = 0;

    private long occurences = 0;
    
    private HashMap<Long, Long> durations = new HashMap<Long, Long>();

    public void setStart(long start) {
        occurences++;
        this.start = start;
        
        if (min == -1 || start < min)
            min = start;

        calcDuration();
    }
    
    public long getStart() { return start; } 
    
    public void setStop(long stop) {
        this.stop = stop;
        
        if (max == -1 || stop > max)
            max = stop;
        
        calcDuration();
    }

    long currentStart = 0;
    public void calcDuration() {
        if (start != -1 && stop != -1) {
            long duration = stop - start;

            if (minDuration == -1 || duration < minDuration)
                minDuration = duration;

            if (maxDuration == -1 || duration > maxDuration)
                maxDuration = duration;

            accDuration += duration;
            currentStart = start;
            start = -1;
            stop = -1;
           
            Long count = durations.get(duration);
           
            if ( count == null ) 
                count = 1L;
            else 
                count++;
            
            durations.put(duration, count);
        }
    }
    
    public Map<Long, Long> getDurations() { 
        Map<Long, Long> sortedMap = new TreeMap<Long, Long>(durations);
        return sortedMap;
    }

    public long duration() {
        return (max - min);
    }

    public void addProp(String key, long value) {
        PropCalc prop = (PropCalc) map.get(key);

        if (prop == null) {
            prop = new PropCalc();
            map.put(key, prop);
        }

        prop.addResult(value);
    }

    /*
     * Variables used to keep track of which state the current monitored 
     * property is in.
     */
    private String lastvalue = null;
    private long laststart = 0;
   
    private HashMap<String, Long> maxIntervals = new HashMap<String, Long>();
    private HashMap<String, Long> minIntervals = new HashMap<String, Long>();
    private HashMap<String, Long> totIntervals = new HashMap<String, Long>();
    private HashMap<String, Long> occIntervals = new HashMap<String, Long>();

    public void addMonitorProp(String key, String value) {
        if (lastvalue == null) {
            lastvalue = value;
            laststart = currentStart;
        } else { 
            if (!lastvalue.equals(value)) {
                // There was a change in the monitored property.
                
                if (!maxIntervals.containsKey(value)) {
                    maxIntervals.put(value, Long.valueOf(0));
                    minIntervals.put(value, Long.valueOf(Long.MAX_VALUE));
                    totIntervals.put(value, Long.valueOf(0));
                    occIntervals.put(value, Long.valueOf(0));
                }
                
                long maxinterval = maxIntervals.get(value);
                long mininterval = minIntervals.get(value);
                long totinterval = totIntervals.get(value);
                long occinterval = occIntervals.get(value);
                
                long interval = (currentStart - laststart);
               
                totinterval += interval;
                totIntervals.put(value, totinterval);
                
                occinterval++;
                occIntervals.put(value, occinterval);
               
                if (interval > maxinterval)
                    maxIntervals.put(value, interval);

                if (interval < mininterval)
                    minIntervals.put(value, interval);
                
                lastvalue = value;
                laststart = currentStart;
            }
        }
    }
    
    public Iterator<String> getMonitorKeys() { 
        return maxIntervals.keySet().iterator();
    }
    
    public long getMaxInterval(String key) { 
        return maxIntervals.get(key);
    }

    public long getMinInterval(String key) { 
        return minIntervals.get(key);
    }

    public long getTotInterval(String key) { 
        return totIntervals.get(key);
    }

    public long getAvgInterval(String key) { 
        long occinterval = occIntervals.get(key);
        
        if (occinterval == 0)
            return 0;
        
        return totIntervals.get(key)/occinterval;
    }

    public HashMap<String, PropCalc> getProps() {
        return map;
    }

    public long getMaxDuration() {
        return maxDuration;
    }

    public long getMinDuration() {
        return minDuration;
    }

    public long getOccurences() {
        return occurences;
    }
    
    public long getAvgDuration() { 
        if (occurences == 0)
            return 0;
        
        return accDuration/occurences;
    }
}