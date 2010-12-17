package com.yahoo.dtf.junit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import junit.framework.JUnit4TestAdapter;
import junit.framework.TestCase;

import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.config.DTFStream;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.streaming.DTFInputStream;
import com.yahoo.dtf.streaming.StringInputStream;

/**
 * Streaming Suite validates that the existing and registered DTFInputStream
 * handlers are all behaving correctly and don't present any obvious and easy 
 * to detect bugs. 
 * 
 * I also do a small performance measurement of operations per second and 
 * throug-put per second so you can validate your new DTFInputStreams agains the
 * existing ones and take some precautions when using your new handler.
 * 
 * @author rlgomes
 */
public class StreamingSuite extends DTFJUnitTest {
   
    private int[] buffersizes = new int[] {1, 1026, 64*1023};
    private long[] sizes = new long[]{0, 1024, 2000000};

    @BeforeClass
    public static void startUpNode() { 
        System.setProperty("dtf.node.type", "dtfa");
        System.setProperty("dtf.node.name", "dummy");
        try {
            DTFNode.init();
        } catch (DTFException e) {
            e.printStackTrace();
            TestCase.fail("Unable to initialize DTF teststack.");
        }
    }
    
    private boolean isExactStream(String name) { 
        return !( name.equals("xml") || name.equals("json"));
    }
        
    private String getArguments(String name) { 
        if ( name.equals("xml") || name.equals("json") ) { 
            return "<?xml version='1.0' encoding='UTF-8' standalone='no'?>" + 
                   "<xs:schema xmlns:xs='http://www.w3.org/2001/XMLSchema'" +
                   "           elementFormDefault='qualified'>" +
                   "   <xs:element name='list'>" +
                   "       <xs:complexType>" +
                   "           <xs:sequence maxOccurs='unbounded'" +
                   "                        minOccurs='0'>" +
                   "               <xs:element name='item' type='xs:string'/>" +
                   "           </xs:sequence>" +
                   "       </xs:complexType>" +
                   "   </xs:element>" +
                   "</xs:schema>";
        } else { 
            return "12345";
        }
    }
    
    @Test(timeout=600000)
    public void singleByteRead() throws DTFException { 
        DTFStream dtfstream = new DTFStream();
        ArrayList<String> streamhandlers = DTFStream.getStreamNames();
        
        for (int s = 0; s < sizes.length; s++) { 
            long size = sizes[s];
            
	        for (int i = 0; i < streamhandlers.size(); i++ ) { 
	            String name = streamhandlers.get(i);
	            
	            if ( isExactStream(name) ) { 
		            String args = getArguments(name);
		            String arguments = name + "," + size + "," + args;
		            DTFInputStream dtfis = dtfstream.getValueAsStream(arguments);
		            getLogger().info("Validating [" + name + "] with [" + 
		                             arguments + "]");
	
		            long cnt = 0;
		            try { 
			            while ( dtfis.read() != -1) {
			                cnt++;
			                if ( cnt > size ) {
			                    TestCase.fail("More bytes read then specified.");
			                }
			            }
			            TestCase.assertEquals(size, cnt);
		            } catch (IOException e) { 
		                getLogger().error("Error",e);
		                TestCase.fail("Error reading from DTFInputStream.");
		            }
	            } else { 
	                getLogger().debug("Can't test structured stream for exact byte count.");
	            }
	        }
        }
    }
    
    @Test(timeout=600000)
    public void stringInputStreamTest() throws DTFException { 
        long[] sizes = new long[]{0,1,2048};
        
        for (int s = 0; s < sizes.length; s++) { 
            long size = sizes[s];
            
            String data = new String();
            for (int i = 0; i < size;i++) 
                data += "X";
            
            StringInputStream dtfis = new StringInputStream(data);
            try { 
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                int read = 0;
                byte[] buffer = new byte[5];
                while ( (read = dtfis.read(buffer)) != -1) {
                    baos.write(buffer,0,read);
                }
                baos.close();
                String result = new String(baos.toByteArray());
               
                if ( result.length() != size ) {
                    getLogger().error("StringInputStream expected " + size + 
                                      " got " + result.length());
                }
                TestCase.assertEquals(size, result.length());
            } catch (IOException e) { 
                getLogger().error("Error",e);
                TestCase.fail("Error reading from DTFInputStream.");
            }
            
        }
    }

    @Test(timeout=600000)
    public void bufferedRead() throws DTFException { 
        DTFStream dtfstream = new DTFStream();
        ArrayList<String> streamhandlers = DTFStream.getStreamNames();
     
        
        for (int b = 0; b < buffersizes.length; b++) { 
            byte[] buffer = new byte[buffersizes[b]];
            
	        for (int s = 0; s < sizes.length; s++) { 
	            long size = sizes[s];
		        for (int i = 0; i < streamhandlers.size(); i++ ) { 
		            String name = streamhandlers.get(i);
		            if ( isExactStream(name) ) { 
			            String args = getArguments(name);
			            String arguments = name + "," + size + "," + args;
			            DTFInputStream dtfis = 
			                              dtfstream.getValueAsStream(arguments);
			            getLogger().info("Validating [" + name + "] with [" + 
			                        arguments + "] buffersize " + buffer.length);
			        
			            long cnt = 0;
			            try { 
			                long read = 0;
				            while ( ( read = dtfis.read(buffer)) != -1) {
				                cnt+=read;
				            }
			                if ( cnt != size ) {
			                    getLogger().error(name + " expected " + size + 
			                                      " got " + cnt);
			                }
				            TestCase.assertEquals(cnt, size);
			            } catch (IOException e) { 
			                getLogger().error("Error",e);
			                TestCase.fail("Error reading from DTFInputStream.");
			            }
		            } else { 
		                getLogger().debug("Can't test structured stream for exact byte count.");
		            }
		        }
	        }
        }
    }

    @Test(timeout=600000)
    public void emptyStream() throws DTFException { 
        byte[] buffer = new byte[5];
        DTFInputStream dtfis = Action.replacePropertiesAsInputStream("");
            
        long cnt = 0;
        try { 
            long read = 0;
            while ( ( read = dtfis.read(buffer)) != -1) {
                cnt+=read;
            }
            
            TestCase.assertEquals("", dtfis.getData());
        } catch (IOException e) { 
            getLogger().error("Error",e);
            TestCase.fail("Error reading from DTFInputStream.");
        }
    }

    @Test(timeout=600000)
    public void smallOpsPerformance() throws DTFException { 
        DTFStream dtfstream = new DTFStream();
        ArrayList<String> streamhandlers = DTFStream.getStreamNames();
     
        int size = 32;
        byte[] buffer = new byte[size];
        
        getLogger().info("Generating " + size + " byte objects.");
        
        for (int s = 0; s < streamhandlers.size(); s++ ) { 
            String name = streamhandlers.get(s);
            String args = getArguments(name);
            String arguments = name + "," + size + "," + args;
       
            try { 
                long cnt = 0;
                long read = 0;
                long iterations = 
                              getConfig().getPropertyAsInt("iterations",100000);
                long start = System.currentTimeMillis();
                for (int i = 0; i < iterations; i++) { 
                    DTFInputStream dtfis = dtfstream.getValueAsStream(arguments);
		            while ( ( read = dtfis.read(buffer)) != -1) {
		                cnt+=read;
		            }
		            dtfis.close();
                }
                long stop = System.currentTimeMillis();
                
                double duration_s = (stop-start)/1000.0f;
                double ops_per_sec = iterations/duration_s;
                getLogger().info(name + "\tOp/sec:\t" + (int)ops_per_sec);
            } catch (IOException e) { 
                getLogger().error("Error",e);
                TestCase.fail("Error reading from DTFInputStream.");
            }
        }
    }

    @Test(timeout=600000)
    public void bigOpsPerformance() throws DTFException { 
        DTFStream dtfstream = new DTFStream();
        ArrayList<String> streamhandlers = DTFStream.getStreamNames();
     
        byte[] buffer = new byte[32*1024];
        long iterations = getConfig().getPropertyAsInt("iterations", 3);
        int size = 100*1024*1024;
        
        if ( iterations == 1 ) {
            // 1 iterations isn't worth running the through put test with 100MB
            size=10*1024*1024;
        }

        getLogger().info("Generating " + (size/1048576) + "MB byte objects.");

        for (int s = 0; s < streamhandlers.size(); s++ ) { 
            String name = streamhandlers.get(s);
            String args = getArguments(name);
            String arguments = name + "," + size + "," + args;
       
            try { 
                long cnt = 0;
                long read = 0;
               
                long start = System.currentTimeMillis();
                for (int i = 0; i < iterations; i++) { 
                    DTFInputStream dtfis = dtfstream.getValueAsStream(arguments);
		            while ( ( read = dtfis.read(buffer)) != -1) {
		                cnt+=read;
		            }
		            dtfis.close();
                }
                long stop = System.currentTimeMillis();
                
                double duration_s = (stop-start)/1000.0f;
                double sizemb = (double)(size/1048576);
                double mb_per_sec = (iterations/duration_s)*sizemb;
                getLogger().info(name + "\tMB/sec:\t" + (int)mb_per_sec);
            } catch (IOException e) { 
                getLogger().error("Error",e);
                TestCase.fail("Error reading from DTFInputStream.");
            }
        }
    }

    @Ignore
    @Test(timeout=600000)
    public void visualTest() throws DTFException { 
        DTFStream dtfstream = new DTFStream();
        ArrayList<String> streamhandlers = DTFStream.getStreamNames();
     
        int size = 1024; 
        for (int i = 0; i < streamhandlers.size(); i++ ) { 
            String name = streamhandlers.get(i);
            if ( isExactStream(name) ) {
	            String args = getArguments(name);
	            String arguments = name + "," + size + "," + args;
	            DTFInputStream dtfis = dtfstream.getValueAsStream(arguments);
	            getLogger().info("Validating [" + name + "] with [" + 
	                             arguments + "]");
	
	            long cnt = 0;
	            try { 
	                StringBuffer data = new StringBuffer();
	                int read = 0;
		            while ( (read = dtfis.read()) != -1) {
		                cnt++;
		                data.append((char)read);
		            }
	                if ( cnt != size ) {
	                    getLogger().error(name + " expected " + size + 
	                                      " got " + cnt);
	                }
		            TestCase.assertEquals(size, cnt);
		            getLogger().info("Read [" + data.toString() + "]");
	            } catch (IOException e) { 
	                getLogger().error("Error",e);
	                TestCase.fail("Error reading from DTFInputStream.");
	            }
            } else { 
                getLogger().debug("Can't test structured stream for exact byte count.");
            }
        }
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(StreamingSuite.class);
    }
}
