package com.yahoo.dtf.recorder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;

import com.yahoo.dtf.DTFConstants;
import com.yahoo.dtf.recorder.ConsoleRecorder;
import com.yahoo.dtf.recorder.Event;
import com.yahoo.dtf.recorder.RecorderBase;
import com.yahoo.dtf.recorder.Attribute;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.exception.RecorderException;
import com.yahoo.dtf.logger.DTFLogger;

public class ConsoleRecorder extends RecorderBase  {
    private DTFLogger _logger =  DTFLogger.getLogger(ConsoleRecorder.class);

    public ConsoleRecorder(URI uri, boolean append) {
        this(uri,append,DTFConstants.DEFAULT_ENCODING);
    }
    
    public ConsoleRecorder(URI uri, boolean append, String encoding) {
        super(uri,append,encoding);
    }
    
    public void record(Event event) throws RecorderException {
        StringBuffer result = new StringBuffer("{");
        result.append(event.getName());
        result.append(".start=");
        result.append(event.getStart());
        result.append(", ");
      
        result.append(event.getName());
        result.append(".stop=");
        result.append(event.getStop());
        result.append(", ");

        try { 
            ArrayList props = event.findActions(Attribute.class);
            Iterator elems = props.iterator();
            
            while (elems.hasNext()) { 
                Attribute attribute = (Attribute)elems.next();
                result.append(event.getName());
                result.append(".");
                result.append(attribute.getName());
                result.append("=");
                result.append(attribute.getValue());
                result.append(", ");
            }
        } catch (ParseException e) { 
            throw new RecorderException("Unable to process properties.",e);
        }
       
        _logger.info(result.substring(0,result.length()-2) + "}");
    }

    public void stop() throws RecorderException { }
    public void start() throws RecorderException { }
}
