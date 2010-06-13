package com.yahoo.dtf;

import java.nio.charset.Charset;

public class DTFConstants {

    /*
     * This value is almost always main in java so this should work fine.
     */
    public final static String MAIN_THREAD_NAME = "main";
    
    public final static String SCRIPT_ID = "dtf.script.id";
    
    public final static String DTFX_ID = "dtfx";
    
    public final static String DTFC_COMPONENT_ID = "DTF-DTFC";

    public final static String DTF_NODE_NAME              = "dtf.node.name";
   
    /*
     * Set the default encoding to the default system encoding that can be 
     * overwritten from the command line by setting the file.encoding property
     * to the one you'd rather use.
     */
    public final static String DEFAULT_ENCODING = Charset.defaultCharset().displayName();
    
}
