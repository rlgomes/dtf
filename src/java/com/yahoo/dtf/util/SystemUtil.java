package com.yahoo.dtf.util;

import java.io.File;
import java.io.IOException;

import com.yahoo.dtf.exception.DTFException;

public class SystemUtil {

    public static void deleteDirectory(File path) throws IOException { 
        String[] files = path.list();

        if (files != null) { 
            for (int i = 0; i < files.length; i++) { 
                File file = new File(path.getAbsolutePath() + File.separatorChar +  files[i]);
               
                if (file.isFile())  {
                    if ( !file.delete() ) {
                        throw new IOException("Unable to delete file [" + file + 
                                              "]");
                    }
                } else {
                    deleteDirectory(file);
                }
            }
        }
        
        if ( !path.delete() ) { 
            throw new IOException("Unable to delete directory [" + path + "]");
        }
    }
   
    public static String getCWD() throws DTFException { 
        try {
            return new File(".").getCanonicalPath();
        } catch (IOException e) {
            throw new DTFException("Unable to resolve cwd",e);
        }
    }
    
}
