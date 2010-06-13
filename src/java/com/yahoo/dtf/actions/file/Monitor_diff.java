package com.yahoo.dtf.actions.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.recorder.Event;

/**
 * @dtf.tag monitor_diff
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc With this tag you can check which files were added since the 
 *               moment you created the monitor.
 *               
 * @dtf.event monitor_diff
 * @dtf.event.attr newfiles
 * @dtf.event.attr.desc List of all the new files.
 *
 * @dtf.tag.example
 * <sequence>  
 *     <monitor_create id="LOGS" file="tests/ut/output/.*"/>
 *     <sleep time="3s"/>
 *     <monitor_diff id="LOGS"/>
 *     <monitor_destroy id="LOGS"/>
 *       
 *     <createrange name="files" value="${monitor_diff.newfiles}"/> 
 *     <for property="i" range="1..10">
 *         <log>new ${files}</log>
 *     </for>
 * </sequence>
 */
public class Monitor_diff extends Monitor_create {

    /**
     * @dtf.attr update
     * @dtf.attr.desc defines if we should move the current pointer in the file 
     *                forward or if we should keep analyzing all files from the 
     *                same point that we already picked at the beginning of the
     *                test. The default is to not update and stay at the same 
     *                point since this allows for you to run multiple expressions
     *                against the same file. By setting this attribute to true
     *                you can move the files virtual "pointer" forward and keep
     *                checking only the latest data for changes.
     *                
     */
    private String update = null;
 
    /**
     * Grabs the previous results and attaches any new files to the list of 
     * files that are to be monitored.
     * 
     * @return
     * @throws ParseException
     * @throws IOException 
     */
    protected HashMap<String, DTFBufferedReader> getNewFiles() 
              throws ParseException, IOException { 
        HashMap<String, FileMonitor> monitors = getMonitors();
        FileMonitor previous = monitors.get(getId());
        HashMap<String, DTFBufferedReader> files = previous.getFiles();
        HashMap<String, DTFBufferedReader> nfiles = 
                                       new HashMap<String, DTFBufferedReader>();

        /*
         * Now find files that have been created in this same directory 
         * after the monitor was started.
         */
        File directory = previous.getDirectory();
        final HashMap<String, DTFBufferedReader> filesFinal = files;
        final String patternFinal = previous.getPattern();
        File[] newfiles = directory.listFiles(new FilenameFilter() { 
            public boolean accept(File dir, String name) {
                String file =  dir.getAbsolutePath() + File.separatorChar + name; 
                return (!filesFinal.containsKey(file) && 
                         name.matches(patternFinal) &&
                         new File(dir,name).isFile());
            }
        });

        // Add the previous files to the files HashMap so that then you can 
        // process the new files as well.
        if ( newfiles != null ) {
	        for (int i = 0; i < newfiles.length; i++) { 
	            DTFBufferedReader br = new DTFBufferedReader(newfiles[i],0);
	            nfiles.put(newfiles[i].getAbsolutePath(),br);
	        }
        }
        
        return nfiles;
    }
    
    public void execute() throws DTFException {
        /*
         * Retrieve what the state of each of the old files was, i.e. what their
         * length was. So we can seek up to that length and then monitor for 
         * a grep expression beyond that point.
         * 
         * Also remember to get the list of files that were created since then 
         * that still belong to the monitor so we can report on any matches 
         * within those files.
         */
        try { 
            StringBuffer newfiles = new StringBuffer();
            HashMap<String, DTFBufferedReader> files = getNewFiles();
            
	        Iterator<Entry<String,DTFBufferedReader>> entries = 
	                                                files.entrySet().iterator();
	        while (entries.hasNext()) { 
	            Entry<String,DTFBufferedReader> entry = entries.next();
	            newfiles.append(entry.getKey() + (entries.hasNext() ? "," : ""));
	        }
	        
	        if ( !getUpdate() ) {
	            // we need to set the current set of files as the new base 
	            // for this monitor
	            HashMap<String, FileMonitor> monitors = getMonitors();
	            FileMonitor previous = monitors.get(getId());
	            previous.replaceFiles(files);
	        }

	        Event event = new Event("monitor_diff");
	        event.start();
	        event.stop();
	        event.addAttribute("newfiles", newfiles.toString());
	        getRecorder().record(event);
        } catch (FileNotFoundException e) { 
            throw new DTFException("Monitored file was deleted.", e);
        } catch (IOException e) {
            throw new DTFException("Monitored file failed to skip.", e);
        }
    }

    public boolean getUpdate() throws ParseException { return toBoolean("update",update); }
    public void setUpdate(String update) { this.update = update; }
}
