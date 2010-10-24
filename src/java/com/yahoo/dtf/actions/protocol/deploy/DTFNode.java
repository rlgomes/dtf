package com.yahoo.dtf.actions.protocol.deploy;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

public class DTFNode extends Action {

    /**
     * @dtf.attr host
     * @dtf.attr.desc hostname of the machine on which this component will be 
     *                started.
     */
    private String host = null;
    
    /**
     * @dtf.attr user
     * @dtf.attr.desc the username to login to the already identified host when
     *                ssh'ing.
     */
    private String user = null;

    /**
     * @dtf.attr path
     * @dtf.attr.desc the local path on the remote machine on which the DTF node
     *                will be started.
     */
    private String path = null;
    
    /**
     * @dtf.attr rsakey
     * @dtf.attr.desc the RSA key to use during the SSH login. The default is 
     *                to point to your ~/.ssh/id_rsa file, if it exists.
     */
    private String rsakey = null;

    /**
     * @dtf.attr passphrase
     * @dtf.attr.desc the passphrase to unlock the identified RSA key.
     */
    private String passphrase = null;

    /**
     * @dtf.attr wrapcmd
     * @dtf.attr.desc <p>
     *                this attribute is used execute your command within another
     *                environment such as another shell or a virtualization
     *                environment that can be logged into through a command line
     *                execution. You can specify which argument is the argument
     *                that can be used to execute the commands necessary by the
     *                deployment feature. This argument is controlled by placing
     *                the '%U' within the wrapcmd attribute at the correct 
     *                location.
     *                </p>
     *                <p>
     *                As an example here is how you would set the wrapcmd so 
     *                that it would execute under a different root system:
     *                </p>
     *                <pre>
     *                wrapcmd="chroot /home/newroot %U"
     *                </pre>
     *                <p>
     *                With the previous wrapcmd you'd be executing all of the
     *                deployment commands under a different system level root. 
     *                This can also be easily used to spawn the commands under
     *                a virtual system or other special environments.
     *                </p>
     *                
     */
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

    public String getPassphrase() throws ParseException { return replaceProperties(passphrase); }
    public void setPassphrase(String passphrase) { this.passphrase = passphrase; }
    
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
