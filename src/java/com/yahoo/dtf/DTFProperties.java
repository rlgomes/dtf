package com.yahoo.dtf;

/**
 * @dtf.feature Internal Properties
 * @dtf.feature.group DTF Properties
 * 
 * @dtf.feature.desc
 * <p> 
 * DTF has some internal properties which are useful for writing test case and 
 * that allow you to do small things you wouldn't otherwise know how to do, here 
 * are the currently available internal DTF properties:
 * </p>
 * 
 * <table border='1'>
 *     <tr><th>Property</th><th>Description</ht></tr>
 *     <tr><td>dtf.xml.path</td>
 *         <td>Assigned at the beginning of any DTF run and is always assigned 
 *             to the location of the XML that defines the currently executed 
 *             test case.</td>
 *     </tr>
 *     <tr><td>dtf.script.id</td>
 *         <td>This is assigned the name of your script that is defined by the 
 *             name attribute of the script tag.</td>
 *     </tr>
 *     <tr><td>dtf.timestamp</td>
 *         <td>Retrieves the System.currentMilliseconds() value dynamically at 
 *             execution time of the test case.</td>
 *     </tr>
 *     <tr><td>dtf.randomInt</td>
 *         <td>Gets a random int value using java.util.Random</td>
 *     </tr>
 *     <tr><td>dtf.randomLong</td>
 *         <td>Gets a random long value using java.util.Random</td>
 *     </tr>
 *     <tr><td>dtf.randomDouble</td>
 *         <td>Gets a random double value using java.util.Random</td>
 *     </tr>
 *     <tr><td>dtf.gaussianLong</td>
 *         <td>Generates a random Long that follows a normal gaussian 
 *             distribution.</td>
 *     </tr>
 * </table>
 * 
 * <p> 
 * The random generators can also be passed arguments to specify limits when 
 * generating random numbers, like so:
 * </p>
 * 
 * <pre>
 * ${dtf.randomInt(1000)} – generate a random upto 1000
 * ${dtf.randomDouble(-100,100)} – generate a random double between -100 and 100 
 * </pre>
 *
 */
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

    public final static String DTF_BUILD_ID               = "dtf.build.id";
    
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
