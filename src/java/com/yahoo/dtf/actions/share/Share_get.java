package com.yahoo.dtf.actions.share;

import java.util.HashMap;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.exception.ShareException;
import com.yahoo.dtf.share.Share;

/**
 * @dtf.tag share_get
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc The share_get tag will retrieve the current state from the 
 *               share and execute the actions put in that share on the current
 *               thread. If there is nothing in the share you were not using a
 *               blocking call then nothing is executed on the calling side and
 *               your thread will proceed normally. If there was some actions 
 *               to be executed they would always be executed before proceeding
 *               with the normal execution of this thread.
 *               
 * @dtf.tag.example 
 * <share_get id="SHARE1" blocking="true"/>
 *
 * @dtf.tag.example 
 * <share_get id="SHARE2"/>
 */
public class Share_get extends ShareOperation {

    /**
     * @dtf.attr blocking
     * @dtf.attr.desc defines if this get call is blocking or not, default is
     *                non blocking.
     */
    private String blocking = null;
    
    @Override
    public void execute() throws DTFException {
        HashMap<String, Share> shares = getShares();
        Share sp = shares.get(getId());
        
        if ( sp == null ) {
            throw new ShareException("Share with name [" + getId() + 
                                     "] does not exist.");
        }

        Action result = sp.get(getBlocking());
        result.execute();
    }

    public boolean getBlocking() throws ParseException { return toBoolean("blocking",blocking); }
    public void setBlocking(String blocking) { this.blocking = blocking; }
}
