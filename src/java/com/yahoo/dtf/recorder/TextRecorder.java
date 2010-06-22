package com.yahoo.dtf.recorder;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.yahoo.dtf.DTFConstants;
import com.yahoo.dtf.recorder.Event;
import com.yahoo.dtf.recorder.RecorderBase;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.recorder.Attribute;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.exception.RecorderException;
import com.yahoo.dtf.exception.StorageException;
import com.yahoo.dtf.storage.StorageFactory;

public class TextRecorder extends RecorderBase {
 
    private OutputStream _os = null;
    
    public TextRecorder(URI uri,
                        boolean append) throws StorageException { 
        this(uri,append,DTFConstants.DEFAULT_ENCODING);
    }
    
    public TextRecorder(URI uri,
                        boolean append,
                        String encoding) throws StorageException { 
        super(uri,append,encoding);
        StorageFactory sf = Action.getStorageFactory();
        _os = new BufferedOutputStream(sf.getOutputStream(uri,append));
    }
    
    public void record(Event event) throws RecorderException {
        StringBuffer result = new StringBuffer();
        String eventName = event.getName() + ".";
     
        /*
         * If you move the start attributes location you will break the 
         * TXTQuery, so if you need to do so make sure to validate those 
         * changes against the TXTQuery class.
         */
        result.append(eventName);
        result.append("start=");
        result.append(event.getStart());
        result.append("\n");
        
        result.append(eventName);
        result.append("stop=");
        result.append(event.getStop());
        result.append("\n");

        ArrayList<Action> attributes = event.children();
        try {
	        for (int i = 0; i < attributes.size(); i++) { 
	            Attribute attribute = (Attribute)attributes.get(i);
                result.append(eventName);
                result.append(attribute.getName());
                result.append("=");
                result.append(URLEncoder.encode(attribute.getValue(),
                                                getEncoding()));
                result.append("\n");
            }
        } catch (ParseException e) {
            throw new RecorderException("Error writing to recorder.", e);
        } catch (UnsupportedEncodingException e) {
            throw new RecorderException("Error writing to recorder.", e);
        }
        result.append("\n");
      
        try {
            _os.write(result.toString().getBytes(getEncoding()));
        } catch (IOException e) {
            throw new RecorderException("Error writing to recorder.", e);
        }
    }

    public void stop() throws RecorderException {
        try {
            _os.close();
        } catch (IOException e) {
            throw new RecorderException("Error closing the TextAppender.",e);
        }
    }

    public void start() throws RecorderException { }
}
