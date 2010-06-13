package com.yahoo.dtf.util;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketUtil {

    public static boolean isPortOpen(int port) throws UnknownHostException { 
        try { 
            Socket sock = new Socket("127.0.0.1",port); 
            sock.close();
            return true;
        } catch (IOException e) { 
            return false;
        }
    }
}
