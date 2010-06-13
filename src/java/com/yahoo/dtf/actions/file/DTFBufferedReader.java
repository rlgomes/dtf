package com.yahoo.dtf.actions.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class DTFBufferedReader {

    private long _lineCount = 0;
    private BufferedReader br = null;
    private File _file = null;
    
    public DTFBufferedReader(File file, long skip) throws IOException {
        _file = file;
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);
        br = new BufferedReader(isr);
       
        int count = 0; 
        while ( count++ < skip && br.readLine() != null );
    }
    
    public String readLine() throws IOException {
        String line = br.readLine();
        
        if (line != null)
            _lineCount++;
        
        return line;
    }
    
    public File getFile() { return _file; }
    public long getCurrentPos() { return _lineCount; }
}
