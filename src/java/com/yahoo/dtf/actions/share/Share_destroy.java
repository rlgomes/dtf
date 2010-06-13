package com.yahoo.dtf.actions.share;

import java.util.HashMap;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ShareException;
import com.yahoo.dtf.share.Share;
import com.yahoo.dtf.share.ShareComponentHook;

/**
 * @dtf.tag share_destroy
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Frees this share points resource which is always a good 
 *               practice.
 *               
 * @dtf.tag.example 
 * <share_destroy id="SHARE1"/>
 */
public class Share_destroy extends ShareOperation {

    @Override
    public void execute() throws DTFException {
        HashMap<String, Share> shares = getShares();
        Share sp = shares.remove(getId());
        
        if ( sp == null ) {
            throw new ShareException("Share with name [" + getId() + 
                                     "] does not exist.");
        }
        
        ShareComponentHook.removeShare(getId());
    }
}
