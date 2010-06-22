package com.yahoo.dtf.storage;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import com.yahoo.dtf.exception.StorageException;

public class StreamStorage extends StorageIntf {
    
    private HashMap<String, InputStream> _inputs = null; 
    private HashMap<String, OutputStream> _outputs = null; 
    
    public StreamStorage(String id, String path) throws StorageException {
        super(id, path, false);
        
        _inputs = new HashMap<String, InputStream>();
        _outputs = new HashMap<String, OutputStream>();
    }
    
    public void addInputStream(String name, InputStream is) { 
        _inputs.put(name, is);
    }

    public void addOutputStream(String name, OutputStream os) { 
        _outputs.put(name, os);
    }

    @Override
    public boolean exists(String name) {
        return _inputs.containsKey(name) || _outputs.containsKey(name);
    }

    @Override
    public InputStream getInputStream(String name,
                                      boolean checkForCompression) 
           throws StorageException {
        if ( name.charAt(0) == '/' ) 
            name = name.substring(1);
        
        if ( !_inputs.containsKey(name) ) { 
            throw new StorageException("Stream with id [" + name + "] does not exist.");
        }
        
        return _inputs.get(name);
    }

    @Override
    public InputStream getInputStream(String name) throws StorageException {
        return getInputStream(name,false);
    }

    @Override
    public OutputStream getOutputStream(String name, boolean append)
            throws StorageException {
        if ( name.charAt(0) == '/' ) 
            name = name.substring(1);
        
        if ( !_outputs.containsKey(name) ) { 
            throw new StorageException("Stream with id [" + name + "] does not exist.");
        }
        
        return _outputs.get(name);
    }

    @Override
    public void createPath(String path) throws StorageException {
        throw new RuntimeException("Not supported method.");
    }
    
    @Override
    public void wipe() throws StorageException {
        throw new RuntimeException("Not supported method.");
    }
    
    @Override
    public boolean isDirectory(String path) {
        throw new RuntimeException("Not supported method.");
    }
    
    @Override
    public String getFullPath() {
        throw new RuntimeException("Not supported method.");
    }
    
    @Override
    public String[] getFiles() {
        throw new RuntimeException("Not supported method.");
    }

    @Override
    public String[] getFiles(String path) {
        throw new RuntimeException("Not supported method.");
    }
    
    @Override
    public Long getLastModified(String filename) {
        throw new RuntimeException("Not supported method.");
    }
    
    @Override
    public boolean openedAsAppend(String filename) {
        throw new RuntimeException("Not supported method.");
    }
    
    @Override
    public long lastOpenedOffset(String filename) {
        throw new RuntimeException("Not supported method.");
    }
    
    @Override
    public void delete(String filename) throws StorageException {
        throw new RuntimeException("Not supported method.");
    }
}