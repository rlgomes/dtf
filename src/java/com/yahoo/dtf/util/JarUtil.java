package com.yahoo.dtf.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import com.yahoo.dtf.exception.DTFException;

public class JarUtil {
    
    private static final int BUFFERSIZE = 32*1024;
    
    public static void jarUp(File dir, File output) throws  DTFException  { 
        try {
            ArrayList<String> skip = new ArrayList<String>();
            skip.add(output.getCanonicalPath());
            FileOutputStream fos = new FileOutputStream(output);
            JarOutputStream jos = new JarOutputStream(fos);
            jarUp(dir, jos, skip);
        } catch (FileNotFoundException e) {
            throw new DTFException("Error creating jar.",e);
        } catch (IOException e) {
            throw new DTFException("Error creating jar.",e);
        }
    }

    private static void jarUp(File dir,
                              JarOutputStream jos,
                              ArrayList<String> skip) throws DTFException {
        File[] flist = dir.listFiles();
        byte[] buf = new byte[BUFFERSIZE];
        
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                jarUp(flist[i], jos, skip);
                continue;
            }
           
            // skip hidden files (.nfs files are soooooo freaking annoying)
            if ( flist[i].isHidden() )
                continue;
           
            try { 
	            if ( skip.contains(flist[i].getCanonicalPath()) ) 
	                continue;
            
	            FileInputStream fis = new FileInputStream(flist[i]);
	            String path = flist[i].getPath();
	           
	            // BSD jar command does not like file entries with ./ at the 
	            // start
	            if ( path.startsWith("./") )
	                path = path.replace("./","");
	            
	            JarEntry je = new JarEntry(path);
	            je.setSize(flist[i].length());
	            jos.putNextEntry(je);
	            int count = 0;
	            while ((count = fis.read(buf, 0, BUFFERSIZE)) != -1) {
	                jos.write(buf, 0, count);
	            }
	            jos.closeEntry();
	            fis.close();
            } catch (IOException e) { 
                throw new DTFException("Unable to create jar.",e);
            }
        }
    }
}
