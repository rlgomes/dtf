package com.yahoo.dtf.components;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;

public class StorageStateUpdater extends Action {

    private String cid = null;
    private String sid = null;

    public StorageStateUpdater() { 
        
    }
   
    public StorageStateUpdater(String cid, String sid) {
        this.cid = cid;
        this.sid = sid;
    }
    
    @Override
    public void execute() throws DTFException {
        StorageState.updateStorage(getCid(), getSid());
    }

    public String getSid() { return sid; }
    public void setSid(String sid) { this.sid = sid; }

    public String getCid() { return cid; }
    public void setCid(String cid) { this.cid = cid; }
}