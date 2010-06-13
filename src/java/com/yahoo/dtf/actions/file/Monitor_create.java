package com.yahoo.dtf.actions.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag monitor_create
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc This tag is used to monitor a set of files for changes, it 
 *               can detect new files that have appeared since after the time 
 *               you said to create the monitor that fit the same pattern. There
 *               are two other tags that can be used in conjunction with this one
 *               they are {@dtf.link monitor_grep} and {@dtf.link monitor_destroy}.
 *               <br/>
 *               <br/>
 *               After you're done with a monitor make sure to clean it up using
 *               the {@dtf.link monitor_destroy} tag. The other grep tag will 
 *               allow you to take all of the files that have changed since the
 *               time of the monitor_create to the time of the monitor_grep is 
 *               called and to check if there are any new lines that match a 
 *               specific pattern. For more information on using 
 *               {@dtf.link monitor_grep} read its documentation.
 *              
 * @dtf.tag.example
 * <monitor_create id="LOGS" file="tests/ut/output/${logfile}.*"/>
 * 
 */
public class Monitor_create extends Action {
    
    protected final static String MONITOR_CTX = "dtf.monitor.ctx";

    /**
     * @dtf.attr id
     * @dtf.attr.desc The unique identifier for this monitor that is being 
     *                created.
     */
    private String id = null;

    /**
     * @dtf.attr file
     * @dtf.attr.desc The file pattern to monitor, you can use a syntax like so:
     *                /var/log/syslog.* or just a plain /var/log/my.log.
     */
    private String file = null;
  
    private static Object _mapLock = new Object();
    protected HashMap<String, FileMonitor> getMonitors() { 
        synchronized(_mapLock) { 
	        HashMap<String, FileMonitor> monitors = 
	               (HashMap<String, FileMonitor>) getGlobalContext(MONITOR_CTX);
	      
	        if (monitors == null) {
	            monitors = new HashMap<String, FileMonitor>();
	            registerGlobalContext(MONITOR_CTX, monitors);
	        }
	        
	        return monitors;
        }
    }
    
    public void execute() throws DTFException {
        HashMap<String, FileMonitor> monitors = getMonitors();
        
        File file = new File(getFile());
        File path = file.getParentFile();
        String pattern = file.getName();
      
        final String patternFinal = pattern;
        File[] files = 
            path.listFiles(new FilenameFilter() { 
                public boolean accept(File dir, String name) {
                    return name.matches(patternFinal) && 
                           new File(dir,name).isFile();
                }  
            });
           
        FileMonitor monitor = new FileMonitor(pattern, path);
        StringBuffer sb = new StringBuffer();
        if (files != null) {
	        for (int i = 0; i < files.length; i++) { 
	            monitor.addFile(files[i]);
	            sb.append(files[i].getName() + 
	                      (i == files.length-1 ? "" : ", "));
	        }
	        
            if ( getLogger().isDebugEnabled() ) {
		        getLogger().debug("Monitoring [" + sb.toString() + "] in [" + 
		                         path.getAbsolutePath() + "]");
            }
        } else {
            if ( getLogger().isDebugEnabled() ) {
	            getLogger().debug("No files to monitor at [" + 
	                             path.getAbsolutePath() + "]");
            }
        }
        monitors.put(getId(), monitor);
    }

    public String getId() throws ParseException { return replaceProperties(id); }
    public void setId(String id) { this.id = id; }

    public String getFile() throws ParseException { return replaceProperties(file); }
    public void setFile(String file) { this.file = file; }
}
