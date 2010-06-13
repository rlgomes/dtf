package com.yahoo.dtf.actions.file;

import java.net.URI;

import com.yahoo.dtf.actions.file.Returnfile;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.storage.StorageFactory;
import com.yahoo.dtf.storage.StorageIntf;

public class CreatePath extends Returnfile {

    private String path = null;
    
    public void execute() throws DTFException {
        StorageFactory sf = getStorageFactory();

        URI uri = parseURI(getUri());

        if ( !sf.checkStorage(uri.getHost()) ) 
            throw new DTFException("Storage not found [" + uri.getHost() + "]");
            
        StorageIntf storage = sf.getStorages().get(uri.getHost());
        
        
        if ( !storage.exists(getPath()) ) {
            if ( getLogger().isDebugEnabled() ) 
                getLogger().debug("Creating path [" + getPath() + "]");
            
            storage.createPath(getPath());
        }
    }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
}
