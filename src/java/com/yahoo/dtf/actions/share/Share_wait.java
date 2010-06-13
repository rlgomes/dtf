package com.yahoo.dtf.actions.share;

import java.util.HashMap;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.exception.ShareException;
import com.yahoo.dtf.share.Share;
import com.yahoo.dtf.util.TimeUtil;

/**
 * @dtf.tag share_wait
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc This tag will wait for the existence of the share point
 *               identified by the attribute id. This can be useful if the 
 *               share point creation is being done dynamically by other 
 *               components and you just want to wait for the share point to be
 *               available before you try to access it.
 *               
 * @dtf.tag.example 
 * <share_wait id="SHARE1"/>
 *
 * @dtf.tag.example 
 * <share_wait id="SHARE2" timeout="30s"/>
 */
public class Share_wait extends ShareOperation {
   
    /**
     * @dtf.attr timeout
     * @dtf.attr.desc The amount of time before giving up on waiting for this
     *                share point to be created.
     */
    private String timeout = null;

    @Override
    public void execute() throws DTFException {
        HashMap<String, Share> shares = getShares();
        synchronized ( shares ) { 
	        while ( !shares.containsKey(getId()) ) {
	            synchronized( shares ) { 
	                try {
	                    shares.wait(getTimeout());
	                } catch (InterruptedException e) {
	                    throw new ShareException("Interrupted while accessing share.",e);
	                }
	            }
	        }
        }
        
        if ( shares.containsKey(getId())) {
	        Share sp = shares.get(getId());
	        if ( sp == null ) {
	            throw new ShareException("Share with name [" + getId() + 
	                                     "] does not exist.");
	        }
        } else { 
            throw new ShareException("Share with name [" + getId() + 
	                                 "] does not exist, timed out waiting.");
        }
    }

    public long getTimeout() throws ParseException { return TimeUtil.parseTime("timeout", timeout); }
    public void setTimeout(String timeout) { this.timeout = timeout; }
}
