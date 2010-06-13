package com.yahoo.dtf.deploy;

import java.util.NoSuchElementException;
import java.util.Scanner;

import com.jcraft.jsch.UserInfo;
import com.yahoo.dtf.logger.DTFLogger;

class DeployUI implements UserInfo {
    
    private static DTFLogger _logger = DTFLogger.getLogger(DeployUI.class);
    
    public boolean setup_passwordless_ssh = false;
    
    private String host = null;
    private String user = null;
    
    public DeployUI(String host, String user) {
        this.host = host;
        this.user = user;
    }
        
    public void showMessage(String arg0) {
        _logger.info(arg0);
    }
       
    public boolean promptYesNo(String arg0) { 
        return true; 
    }
        
    public boolean promptPassword(String arg0) { 
        return true;
    }
        
    public boolean promptPassphrase(String arg0) { 
        return true;
    }
        
    public String getPassword() { 
        _logger.info(user + "@" + host + " Password: ");
        setup_passwordless_ssh = true;
        Scanner scanner = new Scanner(System.in);
        try { 
            String password = scanner.nextLine();
            return password;
        } catch (NoSuchElementException e) {
            return "NONE";
        }
    }
        
    public String getPassphrase() {
        return "dtf";
    }
}
