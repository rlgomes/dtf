package com.yahoo.dtf.actions.share;

import java.util.HashMap;

import com.yahoo.dtf.actions.conditionals.Conditional;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.share.Share;

/**
 * @dtf.tag share_exists
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Conditional tag used to validate if a specified share point 
 *               exists.
 *               
 * @dtf.tag.example 
 */
public class Share_exists extends ShareOperation implements Conditional {

    @Override
    public void execute() throws DTFException {
        throw new DTFException("Conditional tag should not be executed.");
    }
    
    public boolean evaluate() throws DTFException {
        HashMap<String, Share> shares = getShares();
        Share sp = shares.get(getId());
        
        if ( sp == null )
            return false;
        
        return true;
    }
    
    public String explanation() throws DTFException {
        return "share point [" + getId() + "] exists";
    }
}
