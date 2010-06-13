package com.yahoo.dtf;

public class DTFProperties {
   
    // DTF properties
    public final static String DTF_VERSION                = "dtf.version";

    // DTF Home directory used internally to find libs and properties files
    public final static String DTF_HOME                   = "dtf.home";
    
    // node properties
    public final static String DTF_NODE_TYPE              = "dtf.node.type";
    public final static String DTF_NODE_OS                = "dtf.os.name";
    public final static String DTF_NODE_OS_ARCH           = "dtf.os.arch";
    public final static String DTF_NODE_OS_VER            = "dtf.os.version";

    // listening properties
    public final static String DTF_LISTEN_ADDR            = "dtf.listen.addr";
   
    public final static String DTF_LISTEN_PORT            = "dtf.listen.port";
    public final static int    DTF_LISTEN_PORT_DEFAULT    = 20000;
   
    // connect properties
    public final static String DTF_CONNECT_ADDR           = "dtf.connect.addr";
    public final static String DTF_CONNECT_ADDR_DEFAULT   = "127.0.0.1";
    
    public final static String DTF_CONNECT_PORT           = "dtf.connect.port";
    public final static int    DTF_CONNECT_PORT_DEFAULT   = 20000;
    
    // debug properties
    public final static String DTF_DEBUG_PORT             = "dtf.debug.port";
    
    // tracing dtf
    public final static String DTF_TRACING                = "dtf.tracing";

    // XML file properties
    public final static String DTF_XML_FILENAME           = "dtf.xml.filename";
    public final static String DTF_XML_PATH               = "dtf.xml.path";
    
    // DTD file
    public final static String DTF_XSD_FILENAME           = "dtf.xsd.filename";
    
    // DTF Tunneling properties
    public final static String DTF_TUNNELED               = "dtf.tunneled";
    
    // Results properties
    public final static String DTF_TESTCASE_LOG           = "test.log.filename";
    public final static String DTF_LOGGING_FORMAT         
                             = "log4j.appender.STDOUT.layout.ConversionPattern";
    
    // Default properties file to load startup
    public final static String DTF_DEFAULTS    = "dtf.defaults";
}
