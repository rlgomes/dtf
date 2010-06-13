package com.yahoo.dtf.util;

import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.StorageException;
import com.yahoo.dtf.storage.StorageIntf;

/*
 *  XXX: clean this up once someone over at Sun decides to fix this obvious bug.
 *  
 *  Using this class from bug id 4691425 in java... since the GZIPInputStream is  *  to special to handle the case of multiple files in the same gzip file.
 * 
 *  This was copied from the bug as a possible workaround and seems to solve
 *  the problem quite well.
 *  
 *  Link to bug: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4691425
 *
 */
public class MultiMemberGZIPInputStream extends InputStream {

    private ArrayList<GZIPInputStream> entities = null;
    
    private String filename = null;
    private StorageIntf storage = null;

    private InputStream in = null;
    private GZIPInputStreamWrapper gzipin = null;
   
    public MultiMemberGZIPInputStream(String filename, 
                                      StorageIntf storage) 
           throws IOException, StorageException {
        this.filename = filename;
        this.storage = storage;
        
        in = storage.getInputStream(filename);
        gzipin = new GZIPInputStreamWrapper(in);
    } 
    
    private void init() throws DTFException { 
        Action.getLogger().debug("Calculating gzip boundaries...");
        entities = new ArrayList<GZIPInputStream>();
        
        int read = 0;
        int previous = 0;
        int now = 0;
        int count = 0;
        long start = System.currentTimeMillis();
        
        try {
            byte[] mainBuffer = new byte[32*1024];
            int mainRead = 0;
            
            InputStream in = storage.getInputStream(filename);
            while ( mainRead != -1 ) { 
                // read a chunk of data then process the bytes for the right 
                // gzip header signature.
	            mainRead = in.read(mainBuffer);
	           
	            for (int i = 0; i < mainRead; i++) { 
		            byte aux = mainBuffer[i];
		            now = aux & 0xFF; // unsignedInt = signedByte & 0xFF;
		            read++;
		            int value = now << 8 | previous;
		            previous = now;
		            
		            if (value == GZIPInputStream.GZIP_MAGIC) {
		                InputStream auxin = storage.getInputStream(filename); 
		                int auxread = 0;
		                try { 
		                    /*
		                     * XXX: hack to figure out how many gzip files exist in the 
		                     *      multimember gzip file
		                     *      
		                     * First time is a test where we parse the header and read 
		                     * 1KB of data to see if there is anything completely messed
		                     * up
		                     */
		                    int skip = read-2;
		                    while ( (skip -= auxin.skip(skip)) != 0);
		                    
		                    GZIPInputStream gzipin = new GZIPInputStream(auxin);
		                    byte[] buffer = new byte[32*1024];
		                    // read through the whole file and validate its really
		                    // a gzip file, horribly ineffecient but the only way of
		                    // being sure right now... XXX: ineffeciency.
		                    while ( (auxread = gzipin.read(buffer)) != -1 );
		                   
		                    /*
		                     * Second time save the pointer to the file.
		                     */
		                    gzipin.close();
		                    
		                    auxin = storage.getInputStream(filename); 
		                    
		                    skip = read-2;
		                    while ( (skip -= auxin.skip(skip)) != 0);
		                    
		                    gzipin = new GZIPInputStream(auxin);
		
		                    entities.add(gzipin);
		                    count++;
		                } catch (IOException ignore) { 
		                    if (auxread != 0)
		                    auxin.close();
		                } 
		            }
	            }
	        }
        } catch (IOException e) {
            throw new DTFException("Unable to handle multi part gzip file",e);
        } finally {
            if (in != null) {
                try { 
                    in.close();
                } catch (IOException e ) {
                    throw new DTFException("Unable to handle multi part gzip file",e);
                }
            }
        }
        long stop = System.currentTimeMillis();

        if (Action.getLogger().isDebugEnabled()) {
            Action.getLogger().debug("Gzip preprocessing took " + 
                                     (stop-start) + "ms");
        }
    }
    
    public synchronized int getEntityCount() throws DTFException {
        if (entities == null) 
            init();
        
        return entities.size();
    }
    
    public GZIPInputStream getEntity(int index) {
        return entities.get(index);
    }
    
    public int read(byte[] b) throws IOException {
        if ( entities != null ) { 
	        if (entities.size() == 0) {
	            return -1;
	        }
	        
	        int read = entities.get(0).read(b);
	        if (read == -1) {
	            entities.remove(0);
	            return read(b);
	        }
	        return read;
        } else { 
            return gzipin.read(b);
        }
    }
    
    public int read(byte[] b, int off, int len) throws IOException {
        if ( entities != null ) { 
	        if (entities.size() == 0) {
	            return -1;
	        }
	        
	        int read = entities.get(0).read(b, off, len);
	        if (read == -1) {
	            entities.remove(0);
	            return read(b, off, len);
	        }
	        return read;
        } else { 
            return gzipin.read(b, off, len);
        }
    }
    
    public int read() throws IOException {
        if ( entities != null ) {
	        if (entities.size() == 0) {
	            return -1;
	        }
	        
	        int read = entities.get(0).read();
	        if (read == -1) {
	            entities.remove(0);
	            return read();
	        }
	        return read;
        } else { 
            return gzipin.read();
        }
    }
    
    public void close() throws IOException {
        if ( entities != null ) { 
	        for (int i = 0; i < entities.size(); i++) { 
	            entities.get(i).close();
	        }
        }
        
        if ( gzipin != null )
            gzipin.close();
    }
}
