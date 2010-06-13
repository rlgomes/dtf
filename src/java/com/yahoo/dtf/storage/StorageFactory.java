package com.yahoo.dtf.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;

import com.yahoo.dtf.storage.Storage;
import com.yahoo.dtf.storage.StorageFactory;
import com.yahoo.dtf.exception.StorageException;


public final class StorageFactory implements Cloneable {

    private HashMap<String, StorageIntf> _storages = null;
   
    /**
     * There will always be the DEFAULT storage which actually refers to the 
     * directory where DTF resides. ie where the dtf.properties is.
     */
    public StorageFactory() {
        _storages = new HashMap<String, StorageIntf>();
    }
   
    /**
     * 
     * @param id
     * @return
     * @throws StorageException
     */
    public StorageIntf retrieveStorage(String id) 
           throws StorageException {
        if (_storages.containsKey(id))
            return (StorageIntf)_storages.get(id);
        else 
            throw new StorageException("Unable to find storage: " + id);
    }
   
    /**
     * 
     * @param id
     * @return
     */
    public boolean checkStorage(String id) {
        return _storages.containsKey(id);
    }
   
    /**
     * 
     * @param id
     * @param path
     * @throws StorageException 
     * @throws StorageException
     */
    public void createStorage(String id, String path) throws StorageException { 
        createStorage(id, path, false);
    }
   
    /**
     * 
     * @param id
     * @param path
     * @param export
     * @throws StorageException
     */
    public void createStorage(String id, String path,boolean export) 
           throws StorageException {
        StorageIntf storage = new Storage(id, path, export);
        
        if (_storages.containsKey(id))
            throw new StorageException("Storage already exists with id: " + id);
        
        _storages.put(id, storage);
    }
   
    /**
     * 
     * @param storage
     * @throws StorageException
     */
    public void addStorage(StorageIntf storage) throws StorageException { 
        if (_storages.containsKey(storage.getId()))
            throw new StorageException("Storage already exists with id: " +
                                       storage.getId());
        
        _storages.put(storage.getId(), storage);
    }
   
    /*
     * 
     */
    public void delStorage(String id) { 
        _storages.remove(id);
    }
    
    /**
     * 
     * @param uri
     * @return
     * @throws StorageException
     */
    public String getPath(URI uri) throws StorageException {
        StorageIntf storage = retrieveStorage(uri.getHost());
        return storage.getFullPath() + uri.getPath();
    }
    
    public boolean exists(URI uri) throws StorageException { 
        return retrieveStorage(uri.getHost()).exists(uri.getPath());
    }

    public InputStream getInputStream(URI uri,
                                      boolean checkForCompression)
           throws StorageException {
        StorageIntf storage = retrieveStorage(uri.getHost());
        return storage.getInputStream(uri.getPath(),checkForCompression); 
    }
    
    /**
     * 
     * @param uri
     * @return
     * @throws StorageException
     */
    public InputStream getInputStream(URI uri) throws StorageException {
        StorageIntf storage = retrieveStorage(uri.getHost());
        return storage.getInputStream(uri.getPath()); 
    }
   
    /**
     * 
     * @param uri
     * @return
     * @throws StorageException
     */
    public BufferedReader getBufferedReader(URI uri) throws StorageException {
        InputStream is = getInputStream(uri);
        InputStreamReader isr = new InputStreamReader(is);
        return new BufferedReader(isr);        
    }
    
    /**
     * 
     * @param uri
     * @return
     * @throws StorageException
     */
    public OutputStream getOutputStream(URI uri) throws StorageException {
        return getOutputStream(uri,false);
    }

    /**
     * 
     * @param uri
     * @return
     * @throws StorageException
     */
    public OutputStream getOutputStream(URI uri,boolean append) throws StorageException {
        StorageIntf storage = retrieveStorage(uri.getHost());
        return storage.getOutputStream(uri.getPath(),append); 
    }
   
    /**
     * 
     * @return
     */
    public HashMap<String, StorageIntf> getStorages() { 
        return _storages;
    }

    public StorageIntf getStorage(String id) { 
        return _storages.get(id);
    }
   
    /**
     * 
     */
    public Object clone() throws CloneNotSupportedException {
        StorageFactory storageFactory = new StorageFactory();
        storageFactory._storages = (HashMap)this._storages.clone(); 
        return storageFactory;
    }
}
