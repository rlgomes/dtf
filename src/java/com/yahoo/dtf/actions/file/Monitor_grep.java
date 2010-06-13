package com.yahoo.dtf.actions.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.recorder.Event;

/**
 * @dtf.tag monitor_grep
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc With this tag you can grep for any lines in the files that you 
 *               monitored for changes since the moment you created the monitor.
 *               The expression for matching is just a normal regular expression
 *               and all of the lines that match this expression will generate
 *               an event as specified below.
 *               
 * @dtf.event grep
 * @dtf.event.attr line
 * @dtf.event.attr.desc The line number at which the match was found.
 *
 * @dtf.event grep
 * @dtf.event.attr match
 * @dtf.event.attr.desc The line contents that matched the regular expression 
 *                      defined above.
 *                     
 * @dtf.event grep
 * @dtf.event.attr file
 * @dtf.event.attr.desc The full path name and filename of the file that contains
 *                      the line that was matched.
 *              
 * @dtf.tag.example
 * <sequence>
 *     <monitor_create id="LOGS" file="tests/ut/output/${logfile}.*"/>
 *     <sleep time="3s"/>
 *     <monitor_grep id="LOGS" expression="NEW.*"/>
 *     <monitor_destroy id="LOGS"/>
 * </sequence>
 * 
 */
public class Monitor_grep extends Monitor_create {

    /**
     * @dtf.attr expression
     * @dtf.attr.desc the regular expression to match each of the lines of the
     *                monitored files against.
     */
    private String expression = null;
    
    /**
     * @dtf.attr insensitive
     * @dtf.attr.desc defines if the comparison should be done with case 
     *                senstivity enabled or disabled. 
     */
    private String insensitive = null;

    
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
    protected HashMap<String, DTFBufferedReader> getNewChanges() 
              throws ParseException, IOException { 
        HashMap<String, FileMonitor> monitors = getMonitors();
        FileMonitor previous = monitors.get(getId());
        HashMap<String, DTFBufferedReader> files = previous.getFiles();

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
        StringBuffer sb = new StringBuffer();
        if ( newfiles != null ) {
	        for (int i = 0; i < newfiles.length; i++) { 
	            DTFBufferedReader br = new DTFBufferedReader(newfiles[i],0);
	            files.put(newfiles[i].getAbsolutePath(),br);
	            sb.append(newfiles[i].getName() + 
	                      (i == newfiles.length-1 ? "" : ", "));
	        }
	        getLogger().info("New files [" + sb.toString() + "] in " +  
                              previous.getDirectory().getAbsolutePath());
        } else { 
            getLogger().info("No files to monitor at [" + 
                             previous.getDirectory().getAbsolutePath() + "]");
        }

        
        return files;
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
            HashMap<String, DTFBufferedReader> files = getNewChanges();
            int flags = 0;
            
            if (getInsensitive())
                flags |= Pattern.CASE_INSENSITIVE;
            
            Pattern pattern = Pattern.compile(getExpression(), flags);
	        
	        Iterator<Entry<String,DTFBufferedReader>> entries = 
	                                                files.entrySet().iterator();
	        while (entries.hasNext()) { 
	            Entry<String,DTFBufferedReader> entry = entries.next();
	            DTFBufferedReader br = entry.getValue();
	            
	            /*
	             * If we're not updating then we just let the BufferedReaders 
	             * move forward otherwise we make sure to keep the BufferedReaders
	             * pointing to the same place as when we created them with the
	             * monitor_create
	             */
	            if ( !getUpdate() ) { 
	                DTFBufferedReader aux = new DTFBufferedReader(br.getFile(),br.getCurrentPos());
	                files.put(entry.getKey(), aux);
	            }
 
	            String line = null;
	            while ( (line = br.readLine()) != null) { 
	                if (pattern.matcher(line).matches()) {
		                Event event = new Event("grep");
		                event.start();
		                event.stop();
		                event.addAttribute("file", entry.getKey()); 
		                event.addAttribute("match", line); 
		                event.addAttribute("line", br.getCurrentPos());
		                getRecorder().record(event);
	                }
	            }
	        }
	        
	        if ( !getUpdate() ) {
	            // we need to set the current set of files as the new base 
	            // for this monitor
	            HashMap<String, FileMonitor> monitors = getMonitors();
	            FileMonitor previous = monitors.get(getId());
	            previous.replaceFiles(files);
	        }
        } catch (FileNotFoundException e) { 
            throw new DTFException("Monitored file was deleted.", e);
        } catch (IOException e) {
            throw new DTFException("Monitored file failed to skip.", e);
        }
    }

    public String getExpression() throws ParseException { return replaceProperties(expression); }
    public void setExpression(String expression) { this.expression = expression; }

    public boolean getInsensitive() throws ParseException { return toBoolean("insensitive",insensitive); }
    public void setInsensitive(String insensitive) { this.insensitive = insensitive; }

    public boolean getUpdate() throws ParseException { return toBoolean("update",update); }
    public void setUpdate(String update) { this.update = update; }
}
