package com.yahoo.dtf.actions.event;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.event.Attribute.FieldType;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.query.QueryFactory;
import com.yahoo.dtf.query.QueryIntf;
import com.yahoo.dtf.query.TxtQuery;
import com.yahoo.dtf.recorder.RecorderBase;
import com.yahoo.dtf.recorder.RecorderFactory;
import com.yahoo.dtf.recorder.TextRecorder;
import com.yahoo.dtf.storage.StorageIntf;
import com.yahoo.dtf.util.CLIUtil;
import com.yahoo.dtf.util.StringUtil;

/**
 * @dtf.tag sort
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc This tag will sort an already existing recording of events 
 *               by the field specified and will rewrite the file using the 
 *               same previously recorded format. In the process of sorting 
 *               you can also filter out the fields that no longer interest you
 *               and save time in the whole sorting process.
 *               
 * @dtf.tag.example 
 * 
 */
public class Sort extends Action {
   

    /**
     * @dtf.attr input
     * @dtf.attr.desc the uri of the input of events to be read from. 
     */
    private String input = null;

    /**
     * @dtf.attr output
     * @dtf.attr.desc the uri of the output of events to be written to.
     */
    private String output = null;
    
    /**
     * @dtf.attr type
     * @dtf.attr.desc the type of events that were recorded.
     */
    private String type = null;
   
    /**
     * @dtf.attr event
     * @dtf.attr.desc the exact event name if you wish to order (and filter) on
     *                a specific set of events.
     */
    private String event = null;

    /**
     * @dtf.attr encoding
     * @dtf.attr.desc identify any specific character encoding used by this 
     *                event file.
     */
    private String encoding = null;
    
    public Sort() { }
    
    public void execute() throws DTFException {
        int maxevents = 10000;
        
        QueryIntf query = QueryFactory.getQuery(getType());
        final ArrayList<Field> fields = findActions(Field.class);
        Select select = (Select)findFirstAction(Select.class);
        String storage = getInput().getHost();
        
        query.open(getInput(),
                   (select == null ? null : select.findActions(Field.class)),
                   null,
                   null,
                   getEvent(),
                   "UTF-8");
       
        int count = 0, parts = 0;
        HashMap<String,String> map = null;
        
        final ArrayList<String> rFields = new ArrayList<String>();
        final ArrayList<FieldType> rTypes = new ArrayList<FieldType>();
        
        for (Field field : fields) {
            FieldType type = field.getType();
            rTypes.add(type);
        }
       
        /*
         * Comparator function to compare event data
         */
        Comparator<HashMap<String, String>> cmp = 
            new Comparator<HashMap<String,String>>() {
                public int compare(HashMap<String,String> o1, 
                                   HashMap<String,String> o2) {
                    int cnt = 0; 
                    for (String fname : rFields) { 
                        String value1 = o1.get(fname);
                        String value2 = o2.get(fname);
                        FieldType type = rTypes.get(cnt);
                        
                        int cmp = 0;
                        if ( type == null ) { 
                            cmp = StringUtil.naturalCompare(value1, value2);
                        } else if ( type == FieldType.STRING ) {
                            cmp = value1.compareTo(value2);
                        } else if ( type == FieldType.INTEGER ) { 
                            cmp = Integer.parseInt(value1) - Integer.parseInt(value2);
                        } 
                            
                        if ( cmp != 0 ) return cmp;
                        cnt++;
                    }
                    return 0;
                }
            };

        List<HashMap<String, String>> events = 
                                        new ArrayList<HashMap<String,String>>();
        
        final String eventname = getEvent();
        String filename = getInput().getPath();
       
        for (Field field : fields) 
            rFields.add(eventname + "." + field.getName());
        
        while ( (map = query.next(false)) != null ) { 
            int where = Collections.binarySearch(events, map, cmp);
            
            if ( where < 0 ) 
                events.add((-where)-1, map);
            else 
                events.add(where, map);
            
            if ( count > maxevents ) { 
                // enough events to create a secondary file
                String path = "storage://" + storage + "/" +  filename + 
                              ".tmp-" + parts;
                record(path, events);
                events = new  ArrayList<HashMap<String,String>>();
                count = 0;
                parts++;
            }
            count++;
        }
       
        // events left to record
        if ( count > 0 ) { 
            String path = "storage://" + storage + "/" +  filename + ".tmp-" + parts;
            record(path,events);
            events = new  ArrayList<HashMap<String,String>>();
            parts++;
        }
        
        // now the file is partly ordered within the other files we must read 
        // back from each of those files while recreating the newly sorted file
        URI uri;
        try {
            uri = new URI("storage://" + storage + "/" + filename + ".sort_tmp");
        } catch (URISyntaxException e) {
            throw new DTFException("URI parsing issue.",e);
        }

        String sorted_filename = uri.getPath();
        
        RecorderBase output = RecorderFactory.getRecorder(getType(),
                                                          uri,
                                                          false,
                                                          getEncoding());
        
        TxtQuery[] inputs = new TxtQuery[parts];
        for (int i = 0; i < parts; i++) {
            inputs[i] = new TxtQuery();
            try {
                uri = new URI("storage://" + storage + "/" + 
                              filename + ".tmp-" + i);
            } catch (URISyntaxException e) {
                throw new DTFException("URI parsing issue.",e);
            }
            inputs[i].open(uri, null, null, null, null);
        }
     
        boolean available = true;
        HashMap<String, String>[] heads = new HashMap[parts];
        long eventcount = 0;
       
        output.start();
        while ( available ) { 
            available = false;
           
            HashMap<String, String> min = null;
            int index = -1;
            for (int i = 0; i < parts; i++) {
                if (inputs[i] != null) { 
                    if ( heads[i] == null ) { 
                        heads[i] = inputs[i].next(false);
                        
                        if ( heads[i] == null ) {
                            inputs[i].close();
                            inputs[i] = null;
                        } 
                    }
                    
                    if ( heads[i] != null ) { 
                        available = true;
                        if ( min == null || cmp.compare(heads[i], min) < 0 ) { 
                            min = heads[i];
                            index = i;
                        }
                    }
                } 
            }
            
            if ( index != -1 ) {
                heads[index] = null;
                // we have our smallest line lets output it
                com.yahoo.dtf.recorder.Event event = 
                                                    CLIUtil.hashMapToEvent(min);
                output.record(event);
                eventcount++;
            }
        }
        output.stop();

        StorageIntf store = getStorageFactory().getStorage(storage);
        
        for (int i = 0; i < parts; i++) {
            store.delete(filename + ".tmp-" + i);
        }

        // last step move the newly sorted file over the old one.
        store.move(sorted_filename, getOutput().getPath());
    }
    
    private void record(String uripath,
                        List<HashMap<String, String>> events)
            throws DTFException {
        URI uri;
        try {
            uri = new URI(uripath);
        } catch (URISyntaxException e) {
            throw new DTFException("URI parsing issue.",e);
        }
        
        TextRecorder recorder = new TextRecorder(uri, false);
        
        recorder.start();
        for (HashMap<String, String> aux : events) { 
            com.yahoo.dtf.recorder.Event event = 
                                            CLIUtil.hashMapToEvent(aux);
            recorder.record(event);
        }
        recorder.stop();
    }

    public URI getOutput() throws ParseException { return parseURI(output); }
    public void setOutput(String output) { this.output = output; }

    public URI getInput() throws ParseException { return parseURI(input); }
    public void setInput(String input) { this.input = input; }

    public String getType() throws ParseException { return replaceProperties(type); }
    public void setType(String type) { this.type = type; }

    public String getEvent() { return event; }
    public void setEvent(String event) { this.event = event; }

    public String getEncoding() { return encoding; }
    public void setEncoding(String encoding) { this.encoding = encoding; }
}
