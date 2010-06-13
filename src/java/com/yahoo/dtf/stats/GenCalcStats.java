package com.yahoo.dtf.stats;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.yahoo.dtf.recorder.Attribute;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.QueryException;
import com.yahoo.dtf.exception.StatsException;
import com.yahoo.dtf.query.Cursor;
import com.yahoo.dtf.recorder.Event;
import com.yahoo.dtf.util.NumberUtil;

public class GenCalcStats {

    protected final static String SEP_STR = ".";
    
    protected final static String AVG_VAL = "avg_val";
    protected final static String TOT_VAL = "tot_val";
    
    protected final static String MAX_VAL = "max_val";
    protected final static String MIN_VAL = "min_val";

    protected final static String MIN_DUR = "min_dur";
    protected final static String MAX_DUR = "max_dur";
    protected final static String AVG_DUR = "avg_dur";
    protected final static String TOT_DUR = "tot_dur";
    protected final static String CSV_DUR = "csv_dur";
    
    protected final static String TOT_OCC = "tot_occ";
    protected final static String AVG_OCC = "avg_occ";
    
    protected final static String MAX_INT = "max_int"; 
    protected final static String MIN_INT = "min_int"; 
    protected final static String TOT_INT = "tot_int"; 
    protected final static String AVG_INT = "avg_int"; 

    private DecimalFormat formatter = new DecimalFormat("#0.000");

    private EventCalc ec = new EventCalc();
    private String monitor = null;

    public GenCalcStats(String monitor) { 
        ec = new EventCalc();
        this.monitor = monitor;
    }
    
    protected String formatNumber(String num) {
        return formatter.format(num);
    }

    protected String formatNumber(double num) {
        return formatter.format(num);
    }

    protected String formatNumber(long num) {
        return formatter.format(num);
    }
    
    public void updateStats(HashMap<String,String>  result) { 
        Iterator<Entry<String, String>> fields = result.entrySet().iterator();

        while (fields.hasNext()) {
            Entry<String, String> entry = fields.next();
            String fullpropname = entry.getKey();
            String keyprop = 
                fullpropname.substring(fullpropname.lastIndexOf(".") + 1,
                                       fullpropname.length());
            String propValue = entry.getValue();

            if (monitor != null && monitor.equals(keyprop))
                ec.addMonitorProp(keyprop, propValue);

            // if its a number then lets handle it
            // max long is 19 digits long
            if ( NumberUtil.isLong(propValue) ) {
                long value = new Long(propValue).longValue();

                if (keyprop.equals("start")) {
                    ec.setStart(value);
                    continue;
                }

                if (keyprop.equals("stop")) {
                    ec.setStop(value);
                    continue;
                }
                ec.addProp(keyprop, value);
            }
        }
    }
    
    /*
     * If we allow more than a single thread to execute in parallel in here we 
     * could lose data because the updates to setStart,setStop, etc. would be 
     * lost
     */
    public synchronized void updateStats(Event event) throws DTFException { 
        ArrayList<Action> attribs = event.children();

        ec.setStart(event.getStart());
        ec.setStop(event.getStop());

        for (int i = 0; i < attribs.size(); i++) { 
            Attribute attribute = (Attribute) attribs.get(i);
            String keyprop = attribute.getName();
            String propValue = attribute.getValue();

            if (monitor != null && monitor.equals(keyprop))
                ec.addMonitorProp(keyprop, propValue);

            // if its a number then lets handle it
            // max long is 19 digits long
            if ( NumberUtil.isLong(propValue) ) {
                long value = new Long(propValue).longValue();
                ec.addProp(keyprop, value);
            }
        }
    }
    
    public LinkedHashMap<String, String> getCurrentStats() { 
        double duration = ec.duration() / 1000.0f;
        HashMap<String, PropCalc> props = ec.getProps();
        Iterator<Entry<String,PropCalc>> propKeys = props.entrySet().iterator();

        LinkedHashMap<String, String> properties = 
                                            new LinkedHashMap<String, String>();
        
        while ( propKeys.hasNext() ) {
            Entry<String, PropCalc> entry = propKeys.next();
            String pkey = entry.getKey();
            PropCalc prop = entry.getValue();

            double average = (double) prop.getAccValue() / duration;

            properties.put(pkey + SEP_STR + AVG_VAL, formatNumber(average));
            properties.put(pkey + SEP_STR + TOT_VAL, formatNumber(prop.getAccValue()));
            properties.put(pkey + SEP_STR + MAX_VAL, formatNumber(prop.getMaxValue()));
            properties.put(pkey + SEP_STR + MIN_VAL, formatNumber(prop.getMinValue()));
        }

        properties.put(MIN_DUR, ""+ec.getMinDuration());
        properties.put(MAX_DUR, ""+ec.getMaxDuration());
        properties.put(AVG_DUR, ""+ec.getAvgDuration());
        properties.put(TOT_DUR, ""+ec.duration());
        
        properties.put(TOT_OCC, ""+ec.getOccurences());
        properties.put(AVG_OCC, formatNumber(ec.getOccurences() / duration));
       
        if (monitor != null) { 
            Iterator<String> keys = ec.getMonitorKeys();

            StringBuffer states = new StringBuffer();
            while (keys.hasNext()) { 
                String key = keys.next();
                states.append(key + (keys.hasNext() ? "," : "") );
                
                long minInterval = ec.getMinInterval(key);
                long maxInterval = ec.getMaxInterval(key);
                long totInterval = ec.getTotInterval(key);
                long avgInterval = ec.getAvgInterval(key);
             
                String aux = monitor + "_" + key + SEP_STR;
                properties.put(aux + MIN_INT, formatNumber(minInterval));
                properties.put(aux + MAX_INT, formatNumber(maxInterval));
                properties.put(aux + TOT_INT, formatNumber(totInterval));
                properties.put(aux + AVG_INT, formatNumber(avgInterval));
            }

             properties.put(monitor + "_states", states.toString());
        }
        
        Map<Long, Long> aux = ec.getDurations();
        Iterator<Entry<Long,Long>> durations = aux.entrySet().iterator();
    
        StringBuffer data = new StringBuffer();
        while ( durations.hasNext() ) { 
            Entry<Long, Long> entry = durations.next();
            data.append(entry.getKey() + "," + entry.getValue() + "\n");
        }
        properties.put(CSV_DUR, data.toString());
        
        return properties;
    }

    public LinkedHashMap<String, String> calcStats(Cursor cursor)
            throws StatsException {
        try {
            HashMap<String,String> result = null;
            while ((result = cursor.next(false)) != null) {
                updateStats(result);
            }
        } catch (QueryException e) {
            throw new StatsException("Error with cursor.", e);
        }

        return getCurrentStats();
    }
}
