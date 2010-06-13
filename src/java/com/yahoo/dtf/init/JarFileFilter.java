package com.yahoo.dtf.init;

import java.io.File;
import java.io.FilenameFilter;

public class JarFileFilter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(".jar");
    }
}