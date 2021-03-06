package com.yahoo.dtf.comm;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMISocketFactory;
import java.util.HashMap;

import com.yahoo.dtf.DTFProperties;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.config.Config;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.logger.DTFLogger;

/**
 * @dtf.feature Tunneling Components
 * @dtf.feature.group Component Setup
 *
 * @dtf.feature.desc 
 * <p> 
 * Sometimes there are setups where communication over the normal DTF ports will 
 * not be possible  because there are firewalls between two points you're trying
 * to connect the various components of DTF. These situations are common and 
 * because of that DTF can tunnel all communication over any other local port 
 * to talk to the machine in question as long as you setup the tunneling ports 
 * correctly. Now lets first look at the scenario where we use the tunneling 
 * feature to tunnel over ssh to a machine that only has ssh open and wouldn't 
 * allow you to start up an agent and connect to it on whatever random port it 
 * usually gets assigned. So we have machine A that resides on a network where 
 * the only way to get to that machine is over ssh and we have machine B where 
 * we're going to run our DTFC and DTFX. Now we push out the build as we would 
 * normally do and then on machine B we'd use the tool ssh_tunnel.sh found in 
 * the distribution base directory. This tool is pretty straightforward and 
 * requires some Unix commands in order to work at the moment. So here are the 
 * things we need to decide at this point:
 * <ol>
 *  <li> Whats the port we're going to use on both the DTFA side for listening 
 *       and the DTFC side for forwarding. We'll keep it to the same port for 
 *       simplicity of making sense of the setup, but we need to figure out the 
 *       port before we start anything.
 *  </li>
 *  <li> Add a tunnel on the DTFC side using the ssh_tunnel.sh like so 
 *       (remember to always qualify the hostname completely hostname + domain):
 *       <pre>./ssh_tunnel.sh add B.domain 30000</pre>
 *  </li>
 *  <li> Once the above is done you'll have 1 reverse tunnel from machine B to 
 *       machine A on port 2000 that would connect the DTFA to the DTFC and 
 *       you'll also have a local ssh tunnel on port 30000 to port 30000 of the i
 *       machine B over the ssh tunnel. So now you can start your DTFA on 
 *       machine B like so: 
 *       <pre>./ant.sh run_dtfa -Ddtf.tunneled=true -Ddtf.listen.port=30000</pre>
 *       (remember to match the port with the right port you assigned during the
 *       creation of the tunnel on the DTFC as well as to have the dtf.tunneled 
 *       flag set to true otherwise the Agent will not connect tot he DTFC)
 *  </li>
 *  <li> You can list the tunnel at anytime by using the: 
 *       <pre>./ssh_tunnel.sh list</pre>
 *  </li>
 *  <li> Removing the tunnel is as easy as doing: 
 *       <pre>./ssh_tunnel.sh del B.domain </pre>
 *       (Again remember to fully qualify the name of the host with the domain 
 *       name in order for this to work correctly), by removing the tunnel.conf 
 *       file you'll remove all the tunnels. Whenever an entry from this file 
 *       disappears the actual ssh tunnels that are running in the background 
 *       will automatically die off on their own within a few seconds.
 *  </li>
 *  <li> Now you can run your DTFX any machine you'd like because in this 
 *       scenario we always tunnel all requests through the DTFC. So as long as 
 *       your DTFX and DTFC can communicate then you're tests will execute 
 *       normally.
 *  </li>
 *  <li> Other tunneling methods can be used and you can also edit the 
 *       tunnel.conf file to redirect traffic through a TCPReflector like tool 
 *       and be able to debug the data being sent.
 *  </li>
 * </ol>
 * 
 */

public class RMITunnelSF extends RMISocketFactory
                                 implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private static DTFLogger _logger = DTFLogger.getLogger(RMITunnelSF.class);
   
    public RMITunnelSF() { }

    @Override
    public ServerSocket createServerSocket(int port) throws IOException {
        return new ServerSocket(port);
    }
    
    public Socket createSocket(String host, int port) throws IOException {
        HashMap<String, Integer> tunnels = Comm.getTunnels();
       
        Config config = Action.getConfig();
        boolean tunneled = false;

        try { 
            tunneled = 
                config.getPropertyAsBoolean(DTFProperties.DTF_TUNNELED, false);
        } catch (ParseException e) {
            throw new IOException("Error parsing dtf.tunneled value.");
        }
       
        String key = host + port;
        
        if ( tunneled || tunnels.containsKey(key) ) { 
            if (tunnels.get(key) != null) 
                port = tunnels.get(key);

            host = "127.0.0.1";
            if (_logger.isDebugEnabled())
                _logger.debug("Tunneling through [" + host + ":" + port + "]");
        } else  {
            if (_logger.isDebugEnabled())
                _logger.debug("No tunnels matching [" + host + ":" + port + "]");
        }
        
        return new Socket(host,port);
    }
   
    /*
     * This avoids re-creating sockets when there is one already available
     */
    public boolean equals(Object that) {
        return that != null && that.getClass() == this.getClass();
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
