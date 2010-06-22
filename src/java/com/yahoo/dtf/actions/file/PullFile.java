package com.yahoo.dtf.actions.file;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;

/*
 * Used by internal DTF protocol only
 */
public class PullFile extends Getfile {

    private String to = null;
    
    public void execute() throws DTFException {
        Getfile gf = new Getfile();
       
        gf.setOwner(Action.getLocalID());
        gf.setUri(getUri());
        gf.setRemotefile(getRemotefile());
        gf.setAppend("" + getAppend());
        gf.setOffset(getOffset());

        if ( getLogger().isDebugEnabled() ) {
            getLogger().debug("Pulling file " + getRemotefile() + 
                              " from [" + getTo() + "] to [" + getOwner() + 
                              "] append: " + getAppend());
        }
        
        getComm().sendActionToCaller(getTo(), gf).execute();
    }
    
    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }
}
