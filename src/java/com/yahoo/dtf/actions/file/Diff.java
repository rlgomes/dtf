package com.yahoo.dtf.actions.file;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.recorder.Event;
import com.yahoo.dtf.recorder.Recorder;
import com.yahoo.dtf.storage.StorageFactory;

/**
 * @dtf.tag diff
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc This tag is used for calculating the differences between two 
 *               or more files. The tag will throw events of the type diffs.xxx 
 *               that will contain the various differences between the files and
 *               will throw one final diff.xxx event that contains the 
 *               summarized information for all of the differences between those
 *               files. See the Event information below for more detalis.
 *       
 * @dtf.event diff
 * @dtf.event.attr difflines
 * @dtf.event.attr.desc The total number of lines that are different between 
 *                      the files being diffed.
 *
 * @dtf.event diff
 * @dtf.event.attr totallines 
 * @dtf.event.attr.desc The total number of lines in that were compared during
 *                      the diff calculation.
 *
 * @dtf.event diff
 * @dtf.event.attr totalfiles
 * @dtf.event.attr.desc The total number of files that were compared during the 
 *                       diff calculation.
 *
 * @dtf.event diff
 * @dtf.event.attr uri[0-totalfiles].path
 * @dtf.event.attr.desc The full path for each of of the files that were 
 *                      compared during the diff calculation.
 *
 * @dtf.event diffs
 * @dtf.event.attr uris
 * @dtf.event.attr.desc the ids of the URI's that differed (0-totafiles).
 *
 * @dtf.event diffs
 * @dtf.event.attr line[0-totalfiles]
 * @dtf.event.attr.desc There will exist two of these per diffs event that is 
 *                      thrown and they will be numbered based on the uris 
 *                      attribute so that you can easily identify which file
 *                      contained which line.
 * 
 * @dtf.event diffs
 * @dtf.event.attr linenumber
 * @dtf.event.attr.desc The line number of where the two files identified by 
 *                      the uris attribute differ.
 * 
 * @dtf.tag.example
 * <diff>
 *     <input uri="storage://OUTPUT/file1"/>
 *     <input uri="storage://OUTPUT/file2"/>
 *     <input uri="storage://OUTPUT/file3"/>
 * </diff>
 *
 * @dtf.tag.example
 * <diff>
 *     <input uri="storage://OUTPUT/basedata"/>
 *     <input uri="storage://OUTPUT/currentdata"/>
 * </diff>
 *
 * @dtf.tag.example
 * <sequence>
 *      <record uri="storage://OUTPUT/diffs">
 *          <diff>
 *               <input uri="storage://OUTPUT/file1"/>
 *               <input uri="storage://OUTPUT/file2"/>
 *               <input uri="storage://OUTPUT/file3"/>
 *          </diff>
 *      </record>
 *     
 *      <log>number of lines that differ: ${diff.difflines}</log>
 *      
 *      <query uri="storage://OUTPUT/diffs" 
 *             cursor="differences" 
 *             event="diffs"/>
 *             
 *      <iterate cursor="differences">
 *          <log>files differ at line: ${differences.linenumber}</log>
 *          <for property="id" range="${differences.uris}">
 *              <log>file ${diff.uri${id}.path} has ${differences.line${id}}</log>
 *          </for>
 *      </iterate>
 * </sequence>
 */
public class Diff extends Action {
    
    public void execute() throws DTFException {
        ArrayList<Input> inputs = findActions(Input.class);
        BufferedReader[] brs = new BufferedReader[inputs.size()];
        StorageFactory sf = getStorageFactory();
        for (int i = 0; i < inputs.size(); i++) { 
            URI uri = inputs.get(i).getUri();
            
            if ( uri != null ) { 
                brs[i] = sf.getBufferedReader(uri);
            } else { 
                String data = inputs.get(i).getCDATA();
                if ( data != null ) {
                    ByteArrayInputStream bais = new ByteArrayInputStream(data.getBytes());
                    InputStreamReader reader = new InputStreamReader(bais);
                    brs[i] = new BufferedReader(reader);
                }
            }
        }
      
        long MAX = 100;
        long count = 0;
        long different = 0;
        
        try { 
            try {
		        Event event = new Event("diff");
	            event.start();
	          
	            HashMap<Integer,ArrayList<String>> lines = new HashMap<Integer, ArrayList<String>>();
	            
	            for (int i = 0; i < brs.length; i++)
	                lines.put(i, new ArrayList<String>());
	           
	            boolean haslines = true;
	            while ( haslines ) { 
	                haslines = false;
	                Iterator<Entry<Integer, ArrayList<String>>> entries = 
	                                                lines.entrySet().iterator();
	                 
	                while ( entries.hasNext() ) { 
	                    Entry<Integer, ArrayList<String>> flines = entries.next();
	                    Integer index = flines.getKey();
	                    ArrayList<String> clines = flines.getValue();
	                    if ( clines.size() < MAX ) { 
	                        String line = null;
	                        while ((line = brs[index].readLine()) != null
	                                && clines.size() < MAX ) {
	                            haslines = true;
	                            clines.add(line);
	                        }
	                        
	                        if ( line != null )
	                            clines.add(line);
	                        
	                    } else { 
	                        haslines = true;
	                    }
	                }
	                
	                if ( !haslines )
	                    break;
	                
	                ArrayList<Event> diffs = new ArrayList<Event>();
	                ArrayList<Event> diffs1 = new ArrayList<Event>();
	                ArrayList<Event> diffs2 = new ArrayList<Event>();
	                
	                ArrayList<String> blines = lines.get(0);
	                if ( blines.size() == 0 )  {
	                    for (int i = 1; i < lines.size(); i++) { 
	                        ArrayList<String> alines = lines.get(i);
	                       
	                        while ( alines.size() > 0 ) {
	                            Event aux = new Event("diffs");
                                aux.start();
                                aux.stop();
                                aux.addAttribute("uris","0," + i); 
                                aux.addAttribute("diff", "<" + alines.get(0));
                                diffs1.add(aux);
                                diffs2.add(aux);
                                
	                            if ( getLogger().isDebugEnabled() )
	                                getLogger().debug("< " + alines.get(0));
	                            
	                            alines.remove(0);
	                        }
	                    }
	                }
	                
                    for (int i = 1; i < lines.size(); i++) { 
                        int a,b;
                        
                        ArrayList<String> alines = lines.get(i);

                        // calculate the longest common string
                        int[][] opt = new int[alines.size()+1][blines.size()+1];
                        for (a = alines.size()-1; a >= 0; a--) {
                            for (b = blines.size()-1; b >= 0; b--) {
                                if (!blines.get(b).equals(alines.get(a))) {
                                    opt[a][b] = opt[a+1][b+1] + 1;
                                } else {
                                    opt[a][b] = Math.max(opt[a+1][b], opt[a][b+1]);
                                }
                            }
                        }
                       
                        a = 0;
                        b = 0;
                        while (a < alines.size() && b < blines.size()) {
                            count++;
                            if ( alines.get(a).equals(blines.get(b)) ) {
                                a++;
                                b++;
                            } else if ( opt[a+1][b] >= opt[a][b+1] ) { 
                                if ( getLogger().isDebugEnabled() )
                                    getLogger().debug("< " + alines.get(a));
                
                                Event aux = new Event("diffs");
                                aux.start();
                                aux.stop();
					            aux.addAttribute("uris","0," + i); 
					            aux.addAttribute("diff", "<" + alines.get(a));
					            diffs1.add(aux);
                                a++;
                            } else {
                                if ( getLogger().isDebugEnabled() ) 
                                    getLogger().debug("> " + blines.get(b));
                                
                                Event aux = new Event("diffs");
                                aux.start();
                                aux.stop();
					            aux.addAttribute("uris","0," + i); 
					            aux.addAttribute("diff", ">" + blines.get(b));
					            diffs1.add(aux);
                                b++;
                            }
                        }

                        while (a < alines.size() || b < blines.size()) {
                            count++;
                            if (a == alines.size()) {
                                Event aux = new Event("diffs");
                                aux.start();
                                aux.stop();
					            aux.addAttribute("uris","0," + i); 
					            aux.addAttribute("diff", ">" + blines.get(b));
					            diffs1.add(aux);
					            
					            if ( getLogger().isDebugEnabled() ) 
					                getLogger().debug("> " + blines.get(b));
					            b++;
                            } else if (b == blines.size()) {
                                Event aux = new Event("diffs");
                                aux.start();
                                aux.stop();
					            aux.addAttribute("uris","0," + i); 
					            aux.addAttribute("diff", "<" + alines.get(a));
					            diffs1.add(aux);
					            
					            if ( getLogger().isDebugEnabled() ) 
					                getLogger().debug("< " + alines.get(a));
					            a++;
                            }
                        }
                        
                        alines = lines.get(i);
                        ArrayList<String> tlines = null;
                        
                        tlines = blines;
                        blines = alines;
                        alines = tlines;
                        
                        opt = new int[alines.size()+1][blines.size()+1];
                        // calculate the longest common string
                        for (a = alines.size()-1; a >= 0; a--) {
                            for (b = blines.size()-1; b >= 0; b--) {
                                if (!blines.get(b).equals(alines.get(a))) {
                                    opt[a][b] = opt[a+1][b+1] + 1;
                                } else {
                                    opt[a][b] = Math.max(opt[a+1][b], opt[a][b+1]);
                                }
                            }
                        }
                       
                        a = 0;
                        b = 0;
                        while (a < alines.size() && b < blines.size()) {
                            if ( alines.get(a).equals(blines.get(b)) ) {
                                a++;
                                b++;
                            } else if ( opt[a+1][b] >= opt[a][b+1] ) { 
                                if ( getLogger().isDebugEnabled() )
                                    getLogger().debug("< " + alines.get(a));
                
                                Event aux = new Event("diffs");
                                aux.start();
                                aux.stop();
                                aux.addAttribute("uris","0," + i); 
                                aux.addAttribute("diff", "<" + alines.get(a));
                                diffs2.add(aux);
                                a++;
                            } else {
                                if ( getLogger().isDebugEnabled() ) 
                                    getLogger().debug("> " + blines.get(b));
                                
                                Event aux = new Event("diffs");
                                aux.start();
                                aux.stop();
                                aux.addAttribute("uris","0," + i); 
                                aux.addAttribute("diff", ">" + blines.get(b));
                                diffs2.add(aux);
                                b++;
                            }
                        }

                        while (a < alines.size() || b < blines.size()) {
                            if (a == alines.size()) {
                                Event aux = new Event("diffs");
                                aux.start();
                                aux.stop();
                                aux.addAttribute("uris","0," + i); 
                                aux.addAttribute("diff", ">" + blines.get(b));
                                diffs2.add(aux);
                                
                                if ( getLogger().isDebugEnabled() ) 
                                    getLogger().debug("> " + blines.get(b));
                                b++;
                            } else if (b == blines.size()) {
                                Event aux = new Event("diffs");
                                aux.start();
                                aux.stop();
                                aux.addAttribute("uris","0," + i); 
                                aux.addAttribute("diff", "<" + alines.get(a));
                                diffs2.add(aux);
                                
                                if ( getLogger().isDebugEnabled() ) 
                                    getLogger().debug("< " + alines.get(a));
                                a++;
                            }
                        }
                      
                        diffs = (diffs1.size() > diffs2.size() ? diffs2 : diffs1);
                        
                        for (int x = 0; x < a; x++)
                            alines.remove(0);
                    }
                    
                    for (int x = 0; x < diffs.size(); x++) { 
                        different++;
                        Recorder recorder = getRecorder();
                        recorder.record(diffs.get(x));
                    }

                    blines.clear();
	            }
	            event.stop();
	            
	            for (int i = 0; i < brs.length; i++) {
	                URI uri = inputs.get(i).getUri(); 
	                if ( uri != null ) { 
		                String path = sf.getPath(uri);
		                event.addAttribute("uri" + i + ".path", path);
	                } else { 
		                event.addAttribute("uri" + i + ".path", 
		                                   inputs.get(i).getXMLLocation());
	                }
	            }
	            
	            event.addAttribute("totalfiles", brs.length);
	            event.addAttribute("totallines", count);
	            event.addAttribute("difflines", different);
	            getRecorder().record(event);
	        } finally { 
	            for (int i = 0; i < inputs.size(); i++) { 
		            if ( brs[i] != null ) 
		                brs[i].close();
	            }
	        }
        } catch (IOException e) {
            throw new DTFException("Error reading files.",e);
        }
    }
}
