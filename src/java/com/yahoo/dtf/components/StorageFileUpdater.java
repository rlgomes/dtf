package com.yahoo.dtf.components;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;

public class StorageFileUpdater extends Action {

    private String cid = null;
    private String sid = null;
    private String filename = null;

    public StorageFileUpdater() { 
        
    }
   
    public StorageFileUpdater(String cid, String sid, String filename) {
        this.cid = cid;
        this.sid = sid;
        this.filename = filename;
    }
    
    @Override
    public void execute() throws DTFException {
        StorageState.updateLastModified(getCid(), getSid(), getFilename());
    }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public String getSid() { return sid; }
    public void setSid(String sid) { this.sid = sid; }

    public String getCid() { return cid; }
    public void setCid(String cid) { this.cid = cid; }
}