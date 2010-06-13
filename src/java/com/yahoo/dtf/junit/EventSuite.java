package com.yahoo.dtf.junit;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.JUnit4TestAdapter;
import junit.framework.TestCase;

import com.yahoo.dtf.DTFConstants;
import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.recorder.Attribute;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.StorageException;
import com.yahoo.dtf.query.QueryFactory;
import com.yahoo.dtf.query.QueryIntf;
import com.yahoo.dtf.recorder.Event;
import com.yahoo.dtf.recorder.RecorderBase;
import com.yahoo.dtf.recorder.RecorderFactory;
import com.yahoo.dtf.storage.StorageFactory;

/**
 * Event Suite takes care of validating that any recorder or query created is 
 * valid and behaves in accordance with the way the recorder/query tags are 
 * suppose to work with eachother.
 * 
 * The tests involve validating that any event recorded can always be read back
 * as well as the performance of recording small events vs large events.
 * 
 * @author rlgomes
 */
public class EventSuite extends DTFJUnitTest {

    @BeforeClass
    public static void startUpNode() throws StorageException { 
        System.setProperty("dtf.node.type", "dtfa");
        System.setProperty("dtf.node.name", "dummy");
        try {
            DTFNode.init();
        } catch (DTFException e) {
            e.printStackTrace();
            TestCase.fail("Unable to initialize DTF teststack.");
        }
        
        checkAndCreateJunitStorage();
    }

    private static void checkAndCreateJunitStorage() throws StorageException { 
        StorageFactory sf = Action.getStorageFactory();
        String tmp = System.getProperty("java.io.tmpdir");
       
        if ( !sf.checkStorage("JUNIT") ) { 
            String junitoutput = tmp + File.separatorChar + "junit";
	        Action.getStorageFactory().createStorage("JUNIT",junitoutput,false);
        }
    }
 
    /**
     * Validate that for all recorders that have equivalent query mechanism
     * that you can store and retrieve an event correctly.
     * 
     * @throws DTFException
     */
    @Test(timeout=600000)
    public void simpleRecordAndQuery() throws DTFException { 
        ArrayList<String> recorders = RecorderFactory.getRecorderNames();
        ArrayList<String> queries = QueryFactory.getQueryNames();
      
        try { 
	        for (int r = 0; r < recorders.size(); r++) { 
	            String name = recorders.get(r);
	            
	            // skip the console and object recorders.
	            if ( name.equals("object") || name.equals("console") ) 
	                continue;
	           
	            // skip those that don't have the query counterpart ready.
	            if ( !queries.contains(name) ) 
	                continue;
	            
	            URI uri = new URI("storage://JUNIT/events_" + name + ".txt");
	            getLogger().info("Validating recorder [" + name + "]");
	            RecorderBase recorder = 
                    RecorderFactory.getRecorder(name,
                                                uri,
                                                false,
                                                DTFConstants.DEFAULT_ENCODING);
	            recorder.start();
	            try { 
	                for (int i = 0; i < 1000; i++) { 
		                Event event = new Event("test.event");
			            event.start();
			            event.stop();
			            event.addAttribute("iteration", i);
			            recorder.record(event);
	                }
	            } finally { 
	                recorder.stop();
	            }

	            QueryIntf query = QueryFactory.getQuery(name);
	            query.open(uri, null, null, "test.event", null);
		            try { 

		                for (int i = 0; i < 1000; i++) { 
		                    HashMap<String, String> result = query.next(false);
		                    long iteration = Long.valueOf(result.get("test.event.iteration"));
		                   
		                    if ( iteration != i ) 
		                        throw new DTFException("Events should be returned in the same order :(");
		                }
		            
		            if ( query.next(false) != null ) 
		                throw new DTFException("There shouldn't be any more results.");
		            
	            } finally { 
	                query.close();
	            }
	        }
        } catch (URISyntaxException e) { 
            throw new DTFException("Bad URI.",e);
        }
    }
    
    @Test(timeout=600000)
    public void smallEventsRecordQueryPerformance() throws DTFException { 
        ArrayList<String> recorders = RecorderFactory.getRecorderNames();
        ArrayList<String> queries = QueryFactory.getQueryNames();

        long iterations = getConfig().getPropertyAsInt("iterations",200000);

        try { 
            for (int r = 0; r < recorders.size(); r++) { 
                String name = recorders.get(r);
                
                // skip the console and object recorders.
                if ( name.equals("object") || name.equals("console") ) 
                    continue;
               
                
                URI uri = new URI("storage://JUNIT/events_" + name + ".txt");
                RecorderBase recorder = 
                    RecorderFactory.getRecorder(name,
                                                uri,
                                                false,
                                                DTFConstants.DEFAULT_ENCODING);
                recorder.start();
                try { 
                    long start = System.currentTimeMillis();
                    for (long i = 0; i < iterations; i++) { 
                        Event event = new Event("test.event");
                        event.start();
                        event.stop();
                        event.addAttribute("iteration", i);
                        recorder.record(event);
                    }
                    long stop = System.currentTimeMillis();
                    double duration = (stop-start)/1000.0f;
                    long ops_per_sec = (long)(iterations/duration);
                    getLogger().info("Record [" + name + "] Op/sec: " + ops_per_sec);
                } finally { 
                    recorder.stop();
                }

                if ( !queries.contains(name) ) {
                    getLogger().warn("No query handler for [" + name + "]");
                    continue;
                }

                QueryIntf query = QueryFactory.getQuery(name);
                query.open(uri, null, null, "test.event", null);
                try { 
                    long start = System.currentTimeMillis();
                    for (long i = 0; i < iterations; i++) { 
                        query.next(false);
                    }
                    long stop = System.currentTimeMillis();
                    double duration = (stop-start)/1000.0f;
                    long ops_per_sec = (long)(iterations/duration);
                    getLogger().info("Query  [" + name + "] Op/sec: " + ops_per_sec);
                } finally { 
                    query.close();
                }
            }
        } catch (URISyntaxException e) { 
            throw new DTFException("Bad URI.",e);
        }
    }
    
    @Test(timeout=600000)
    public void largeEventsRecordQueryPerformance() throws DTFException { 
        ArrayList<String> recorders = RecorderFactory.getRecorderNames();
        ArrayList<String> queries = QueryFactory.getQueryNames();

        long iterations = getConfig().getPropertyAsInt("iterations",20000);
        try { 
            for (int r = 0; r < recorders.size(); r++) { 
                String name = recorders.get(r);
                
                // skip the console and object recorders.
                if ( name.equals("object") || name.equals("console") ) 
                    continue;
               
                
                URI uri = new URI("storage://JUNIT/events_" + name + ".txt");
                RecorderBase recorder = 
                    RecorderFactory.getRecorder(name,
                                                uri,
                                                false,
                                                DTFConstants.DEFAULT_ENCODING);
                StringBuffer data = new StringBuffer();
                
                for (int i = 0; i < 5*1024; i++) 
                    data.append("X");
               
                Event moch = new Event("test.event");
                moch.addAttribute("timestamp", System.currentTimeMillis());
                moch.addAttribute("data1", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
                moch.addAttribute("data2", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
                moch.addAttribute("data3", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
                moch.addAttribute("bigdata", data.toString());
             
                long length = 0;
                ArrayList<Attribute> attribs = moch.findActions(Attribute.class);
                for (int i = 0; i < attribs.size(); i++) { 
                    Attribute attrib = attribs.get(i);
                    length += attrib.getName().length() + attrib.getValue().length();
                }
                getLogger().info("Event size " + length + " bytes.");
                
                recorder.start();
                try { 
                    long start = System.currentTimeMillis();
                    for (long i = 0; i < iterations; i++) { 
                        Event event = new Event("test.event");
                        event.start();
                        event.addAttribute("iteration", i);
                        event.addActions(moch.children());
                        event.stop();
                        
                        recorder.record(event);
                    }
                    long stop = System.currentTimeMillis();
                    double duration = (stop-start)/1000.0f;
                    long ops_per_sec = (long)(iterations/duration);
                    long mb_per_sec = (length * ops_per_sec)/(1024*1024);
                    getLogger().info("Record [" + name + "] Op/sec: " + ops_per_sec);
                    getLogger().info("Record [" + name + "] MB/sec: " + mb_per_sec);
                } finally { 
                    recorder.stop();
                }

                if ( !queries.contains(name) ) {
                    getLogger().warn("No query handler for [" + name + "]");
                    continue;
                }

                QueryIntf query = QueryFactory.getQuery(name);
                query.open(uri, null, null, "test.event", null);
                try { 
                    long start = System.currentTimeMillis();
                    for (long i = 0; i < iterations; i++) { 
                        query.next(false);
                    }
                    long stop = System.currentTimeMillis();
                    double duration = (stop-start)/1000.0f;
                    long ops_per_sec = (long)(iterations/duration);
                    long mb_per_sec = (length * ops_per_sec)/(1024*1024);
                    getLogger().info("Query  [" + name + "] Op/sec: " + ops_per_sec);
                    getLogger().info("Query  [" + name + "] MB/sec: " + mb_per_sec);
                } finally { 
                    query.close();
                }
            }
        } catch (URISyntaxException e) { 
            throw new DTFException("Bad URI.",e);
        }
    }
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(EventSuite.class);
    }
}
