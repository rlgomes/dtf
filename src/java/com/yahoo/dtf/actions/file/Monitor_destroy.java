package com.yahoo.dtf.actions.file;

import java.util.HashMap;

import com.yahoo.dtf.exception.DTFException;
/**
 * @dtf.tag monitor_destroy
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Frees all the resources that were allocated by the 
 *               {@dtf.link monitor_create} tag. You should call this if you 
 *               don't it will be detected and the component will clean up and 
 *               spam the logs about this bad behavior.
 *              
 * @dtf.tag.example
 * <monitor_destroy id="LOGS"/>
 */
public class Monitor_destroy extends Monitor_create {
    
    public void execute() throws DTFException {
        HashMap<String, FileMonitor> monitors = getMonitors();
        monitors.remove(getId());
       
        /*
         * Clean up the context if there are no monitors left on it.
         */
        if (monitors.isEmpty()) {
            unRegisterGlobalContext(MONITOR_CTX);
        }
    }
}
