package com.yahoo.dtf.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostUtils {

    public static String getHostname() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            return addr.getCanonicalHostName();
        } catch (UnknownHostException e) { }
       
        // default to localhost
        return "127.0.0.1";
    }
    
    public static String getHostAddress() { 
        try {
            InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostAddress();
        } catch (UnknownHostException e) { }
       
        // default to localhost
        return "127.0.0.1";
    }
    
    public static boolean isLocal(String host) throws UnknownHostException { 
        InetAddress addr = InetAddress.getByName(host);
        InetAddress localip = InetAddress.getByName(getHostname());
        
        String laddress = localip.getHostAddress();
        String oaddress = addr.getHostAddress();
        
        return ( addr.isLoopbackAddress() || getHostname().equals(host) || 
                 oaddress.equals(laddress) );
    }
}
