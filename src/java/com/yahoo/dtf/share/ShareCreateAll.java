package com.yahoo.dtf.share;

import java.util.HashMap;

import com.yahoo.dtf.actions.share.Share_create;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

public class ShareCreateAll extends Share_create {

    private String cid = null;
    
    @Override
    public void execute() throws DTFException {
        ShareComponentHook.createOnAll(getId(), getType(), getCid());
        HashMap<String, Share> shares = getShares();
        
        if (shares.containsKey(getId())) 
            throw new ParseException("[" + getId() + "] already exists.");
      
        Share sp = ShareFactory.getShare(getType(), getId());
        sp.setCid(getCid());
        
        synchronized (shares) {
	        shares.put(getId(),sp);
	        shares.notifyAll();
        }
    }
    
    public void setCid(String cid) { this.cid = cid; } 
    public String getCid() { return cid; }

}
