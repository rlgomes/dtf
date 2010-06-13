package com.yahoo.dtf.actions.protocol.deploy;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

public class DTFNode extends Action {

    private String host = null;
    
    private String user = null;

    private String path = null;
    
    private String rsakey = null;

    private String wrapcmd = null;
    
    @Override
    public void execute() throws DTFException {
        throw new DTFException("This is not meant to be executed.");
    }

    public String getHost() throws ParseException { return replaceProperties(host); }
    public void setHost(String host) { this.host = host; }

    public String getUser() throws ParseException { return replaceProperties(user); }
    public void setUser(String user) { this.user = user; }

    public String getPath() throws ParseException { return replaceProperties(path); }
    public void setPath(String path) { this.path = path; }

    public String getWrapcmd() throws ParseException { return replaceProperties(wrapcmd); }
    public void setWrapcmd(String wrapcmd) { this.wrapcmd = wrapcmd; }

    public String getRsakey() throws ParseException { return replaceProperties(rsakey); }
    public void setRsakey(String rsakey) { this.rsakey = rsakey; }
    
    @Override
    public boolean equals(Object obj) {
	    try { 
	        if ( obj instanceof DTFNode ) { 
	            DTFNode node = (DTFNode) obj;
	         
	            String npath = node.getPath();
	            String path = getPath();
	            return node.getUser().equals(getUser()) && 
	                   node.getHost().equals(getHost()) && 
	                   (npath.equals(getPath()) || 
	                    npath == null || path == null || 
	                    npath.endsWith(path) || path.endsWith(npath));
	                     
	        } 
        } catch (DTFException e) {  }
       return false;
    }
    
    @Override
    public int hashCode() {
        return host.hashCode() + path.hashCode() + user.hashCode();
    }
}
