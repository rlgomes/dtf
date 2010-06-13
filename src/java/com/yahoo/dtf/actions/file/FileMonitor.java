package com.yahoo.dtf.actions.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.yahoo.dtf.exception.DTFException;

public class FileMonitor {

    private HashMap<String, DTFBufferedReader> _files = null;
    private String _pattern = null;
    private File _directory = null;
    
    FileMonitor(String pattern, File directory) {
        _files = new HashMap<String, DTFBufferedReader>();
        _pattern = pattern;
        _directory = directory;
    }
   
    public void addFile(File file) throws DTFException {
        long linecount = 0;
        try { 
	        DTFBufferedReader br = new DTFBufferedReader(file,0);
	        
	        while (br.readLine() != null)
	            linecount++;

	        _files.put(file.getAbsolutePath(), br);
        } catch (IOException e) { 
            throw new DTFException("Error creating monitor.",e);
        }
    }
   
    public void replaceFiles(HashMap<String, DTFBufferedReader> files) {
        _files = files;
    }
    public HashMap<String, DTFBufferedReader> getFiles() { return _files; } 
    public File getDirectory() { return _directory; } 
    public String getPattern() { return _pattern; } 
}
