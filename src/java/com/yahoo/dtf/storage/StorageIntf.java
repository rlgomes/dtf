package com.yahoo.dtf.storage;

import java.io.InputStream;
import java.io.OutputStream;

import com.yahoo.dtf.exception.StorageException;

public abstract class StorageIntf {
    
    private String _id = null;
    private String _path = null;
    private boolean _export = false;
    
    public StorageIntf(String id, String path, boolean export) 
           throws StorageException {
        _id = id;
        _path = path;
        _export = export;
    }
    
    public String getId() { return _id; }
    public String getPath() { return _path; }

    public boolean isExportable() { return _export; }
    
    public abstract boolean exists(String filename);

    public abstract OutputStream getOutputStream(String filename, 
                                                 boolean append)
           throws StorageException;

    public abstract InputStream getInputStream(String filename,
                                               boolean checkForCompression)
           throws StorageException;

    public abstract InputStream getInputStream(String filename)
           throws StorageException;
    
    public abstract void createPath(String path) throws StorageException;
    
    public abstract boolean isDirectory(String path);
    
    public abstract String getFullPath();

    public abstract Long getLastModified(String filename);
    
    public abstract boolean openedAsAppend(String filename);
   
    /**
     * This method should return the last offset at which this file was opened.
     * This is useful when syncing storages and can be used also by other tags
     * when appending or attemping to be a bit smarter on the way they lookup
     * data within a file on a given storage.
     * 
     * @param filename
     * @return
     */
    public abstract long lastOpenedOffset(String filename);
   
    /**
     * This method will return all of the direct files listed below this storage.
     * That includdes directories and hidden files as well.
     * @return
     */
    public abstract String[] getFiles();
   
    /**
     * This method must return the names of all of the files that exist within
     * the path specified by the attribute path. The attribute path can be any
     * directory below the storage path to which this storage is pointing.
     *  
     * @param path
     * @return
     */
    public abstract String[] getFiles(String path);
    
    /**
     * Wipe is used to clear out any old files in this storage, this is mainly
     * used internally to wipe the remote storages before using them on agents. 
     * 
     * @throws StorageException
     */
    public abstract void wipe() throws StorageException;
   
    /**
     * delete the resource within this storage with the specified filename
     *  
     * @param filename
     */
    public abstract void delete(String filename) throws StorageException;
   
    /**
     * moves a resource within a storage allowing for renaming of directories 
     * and files.
     *  
     * @param src
     * @param dst
     * @throws StorageException
     */
    public abstract void move(String src, String dst) throws StorageException;
}