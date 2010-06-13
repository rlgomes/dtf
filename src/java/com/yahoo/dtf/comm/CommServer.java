package com.yahoo.dtf.comm;

import com.yahoo.dtf.exception.CommException;

public interface CommServer {
    
    public int getPort();
    public String getAddress();
    public void addHandler(String name, Object obj) throws CommException;
    public void start() throws CommException;
    public void shutdown();
    public void printStats();
    
}
