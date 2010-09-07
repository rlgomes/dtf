package com.yahoo.dtf.plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class JarUtil {

    public static String getXSDFilename(File jarFile) {
        String filename = getXSDPropertyValue(jarFile, 
                                              XSDConstants.XSD_FILE_PROPERTY);
        if (filename != null) 
            return filename;
        else 
            return null;
    }

    public static InputStream getXSDInputStream(File jarFile) {
        String filename = getXSDPropertyValue(jarFile, 
                                              XSDConstants.XSD_FILE_PROPERTY);
        
        if (filename != null) {
            try { 
                JarFile jarfile = new JarFile(jarFile);
                ZipEntry ze = jarfile.getEntry(filename);
                if (ze == null) { 
                    return null;
                }
                return jarfile.getInputStream(ze);
            } catch (IOException e) { 
                return null;
            }
        } else 
            return null;
    }
    
    public static String getXSDPropertyValue(File jarFile,String property) {
        Attributes.Name attributeName = new Attributes.Name(property);

        try {
            JarFile jarfile = new JarFile(jarFile);
            if (jarfile.getManifest() != null ) { 
	            Attributes attributes = jarfile.getManifest().getMainAttributes();
	            
	            if (attributes.containsKey(attributeName)) {
	                String filename = attributes.getValue(attributeName);
	                return filename;
	            }
            }
        } catch (IOException e) { }
        
        return null;
    }
}
