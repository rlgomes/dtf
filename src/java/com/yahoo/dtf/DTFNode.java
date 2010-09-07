package com.yahoo.dtf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.jxpath.JXPathIntrospector;
import org.openqa.selenium.server.SeleniumServer;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.component.Component;
import com.yahoo.dtf.actions.component.Lockcomponent;
import com.yahoo.dtf.actions.component.Stopcomponent;
import com.yahoo.dtf.actions.protocol.Lock;
import com.yahoo.dtf.actions.protocol.ReleaseAgent;
import com.yahoo.dtf.actions.selenium.server.SeleniumServerFactory;
import com.yahoo.dtf.actions.util.ScriptUtil;
import com.yahoo.dtf.comm.Comm;
import com.yahoo.dtf.comm.CommClient;
import com.yahoo.dtf.comm.CommServer;
import com.yahoo.dtf.comm.rpc.Node;
import com.yahoo.dtf.components.Components;
import com.yahoo.dtf.components.FunctionsLockHook;
import com.yahoo.dtf.components.PropertyState;
import com.yahoo.dtf.components.ReferencesLockHook;
import com.yahoo.dtf.components.StateComponentHook;
import com.yahoo.dtf.components.StorageState;
import com.yahoo.dtf.config.Config;
import com.yahoo.dtf.config.DTFDateStamp;
import com.yahoo.dtf.config.DTFGaussianLong;
import com.yahoo.dtf.config.DTFNodeName;
import com.yahoo.dtf.config.DTFRandomDouble;
import com.yahoo.dtf.config.DTFRandomInt;
import com.yahoo.dtf.config.DTFRandomLong;
import com.yahoo.dtf.config.DTFStream;
import com.yahoo.dtf.config.DTFTimeStamp;
import com.yahoo.dtf.config.transform.ConvertTransformer;
import com.yahoo.dtf.config.transform.JPathTransformer;
import com.yahoo.dtf.config.transform.ApplyTransformer;
import com.yahoo.dtf.config.transform.StringTransformer;
import com.yahoo.dtf.config.transform.TransformerFactory;
import com.yahoo.dtf.config.transform.XPathTransformer;
import com.yahoo.dtf.debug.DebugServer;
import com.yahoo.dtf.distribution.ThreadMgr;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.init.InitPlugins;
import com.yahoo.dtf.json.JSONObjInspector;
import com.yahoo.dtf.json.JSONObject;
import com.yahoo.dtf.logger.DTFLogger;
import com.yahoo.dtf.query.QueryFactory;
import com.yahoo.dtf.query.TxtQuery;
import com.yahoo.dtf.recorder.CSVRecorder;
import com.yahoo.dtf.recorder.ConsoleRecorder;
import com.yahoo.dtf.recorder.StatsRecorder;
import com.yahoo.dtf.recorder.NullRecorder;
import com.yahoo.dtf.recorder.ObjectRecorder;
import com.yahoo.dtf.recorder.Recorder;
import com.yahoo.dtf.recorder.RecorderFactory;
import com.yahoo.dtf.recorder.TextRecorder;
import com.yahoo.dtf.results.ConsoleResults;
import com.yahoo.dtf.results.JUnitResults;
import com.yahoo.dtf.results.NullResults;
import com.yahoo.dtf.results.Results;
import com.yahoo.dtf.results.ResultsFactory;
import com.yahoo.dtf.results.XMLResults;
import com.yahoo.dtf.share.CumulativeShare;
import com.yahoo.dtf.share.QueueShare;
import com.yahoo.dtf.share.ShareFactory;
import com.yahoo.dtf.share.SingleShare;
import com.yahoo.dtf.state.ActionState;
import com.yahoo.dtf.state.DTFState;
import com.yahoo.dtf.storage.StorageFactory;
import com.yahoo.dtf.streaming.RandomInputStream;
import com.yahoo.dtf.streaming.RepeatInputStream;
import com.yahoo.dtf.util.SystemUtil;
import com.yahoo.dtf.util.ThreadUtil;

/**
 * 
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
public class DTFNode {

    // Logger
    protected final static DTFLogger _logger = DTFLogger.getLogger(DTFNode.class);

    // Config 
    private static Config _config = null;
    
    // build id
    private static String _buildid = null;

    // Communications
    private Comm _comm = null;
    
    // State
    private static DTFState _state = null;
    
    // Cached values at startup
    private static String _type = null;
    
    private static Lock _owner = null;
    
    private static boolean _running = true;
   
    public Config getConfig() { return _config; }
    public Comm getComm() { return _comm; } 
    public static String getType() { return _type; } 
    
    public static Lock getOwner() { return _owner; } 
    public static void setOwner(Lock owner) { _owner = owner; } 
   
    /**
     * Method just initializes all of the state that a DTFNode needs before it 
     * can startup the communication layer and actually be part of the system.
     * 
     * Used by JUnit tests to initialize a DTFNode like environment without the
     * communication layer up and running.
     * 
     * @throws DTFException
     */
    public static void init() throws DTFException { 
        // Read Configuration File
        _config = new Config();

        // Setup Logger
        DTFLogger.setConfig(_config);
        _type = _config.getProperty(DTFProperties.DTF_NODE_TYPE, "NONE");
        
        _state = new DTFState(_config, new StorageFactory());
        _state.setRecorder(new Recorder(new NullRecorder(false), null));
        _state.setComponents(new Components());

        ActionState.getInstance().setState(DTFConstants.MAIN_THREAD_NAME, _state);
        /*
         * Check for any default properties to load from the dtf.defaults file.
         */
        loadDefaults();
       
        // init plugins
        InitPlugins.init();

        // Recorder Implemenations
        RecorderFactory.registerRecorder("console", ConsoleRecorder.class, false);
        RecorderFactory.registerRecorder("csv", CSVRecorder.class);
        RecorderFactory.registerRecorder("object", ObjectRecorder.class, false);
        RecorderFactory.registerRecorder("txt", TextRecorder.class);
        RecorderFactory.registerRecorder("stats", StatsRecorder.class, false);

        // Query Implemenations
        QueryFactory.registerQuery("txt", TxtQuery.class);
        
        // Results Implementations
        ResultsFactory.registerResults("xml",     XMLResults.class);
        ResultsFactory.registerResults("console", ConsoleResults.class);
        ResultsFactory.registerResults("junit",   JUnitResults.class);

        // JXPath registration for JSONObjects
        JXPathIntrospector.registerDynamicClass(JSONObject.class, 
                                                JSONObjInspector.class);
        
        // Transformer Implementations 
        TransformerFactory.registerTransformer("xpath", new XPathTransformer());
        TransformerFactory.registerTransformer("jpath", new JPathTransformer());
        TransformerFactory.registerTransformer("apply", new ApplyTransformer());
        TransformerFactory.registerTransformer("convert", new ConvertTransformer());
        TransformerFactory.registerTransformer("string", new StringTransformer());
        
        // Dynamic Properties
        Config.registerDynamicProperty(DTFTimeStamp.DTF_TIMESTAMP, new DTFTimeStamp());
        Config.registerDynamicProperty(DTFDateStamp.DTF_DATESTAMP, new DTFDateStamp());
       
        Config.registerDynamicProperty(DTFRandomInt.DTF_RANDOMINT, new DTFRandomInt());
        Config.registerDynamicProperty(DTFRandomLong.DTF_RANDOMLONG, new DTFRandomLong());
        Config.registerDynamicProperty(DTFRandomDouble.DTF_RANDOMDOUBLE, new DTFRandomDouble());
        Config.registerDynamicProperty(DTFGaussianLong.DTF_GAUSSIANLONG, new DTFGaussianLong());

        Config.registerDynamicProperty(DTFNodeName.DTF_NODENAME, new DTFNodeName());
        
        Config.registerDynamicProperty(DTFStream.DTF_STREAM, new DTFStream());
        
        // DTFInputStream handlers (for the above dynamic property DTF_STREAM
        DTFStream.registerStream("random", RandomInputStream.class);
        DTFStream.registerStream("repeat", RepeatInputStream.class);
        
        // Share type registering
        ShareFactory.registerShare(SingleShare.NAME, SingleShare.class);
        ShareFactory.registerShare(QueueShare.NAME, QueueShare.class);
        ShareFactory.registerShare(CumulativeShare.NAME, CumulativeShare.class);
        
        // Register default LockHooks
        Lockcomponent.registerLockHook(new ReferencesLockHook());
        Lockcomponent.registerLockHook(new FunctionsLockHook());
        
        // Register default component hooks
        // this hook manages the property state between runner and agents.
        PropertyState ps = new PropertyState();
        Component.registerComponentHook(ps);
        
        StorageState ss = new StorageState();
        Component.registerComponentHook(ss);
       
        // Register default unlock component hooks
        Component.registerComponentUnlockHook(ss);
        Component.registerComponentUnlockHook(ps);

        // Register component return hooks
        Component.registerComponentReturnHook(ss);
       
        // Clean up hook for StorageState management
        ReleaseAgent.addCleanUpHook(ss);

        // thread manager to be used for thread management within DTF
        ReleaseAgent.addCleanUpHook(new ThreadMgr());
       
        // simple clean up necessary for cleaning up Node related contexts
        ReleaseAgent.addCleanUpHook(new Node());
       
        // State clean up hook to make sure that agents don't keep around old 
        // and useless states.
        Component.registerComponentHook(new StateComponentHook());
        
        // we need to reinitialize because the registering of dynamic properties
        // by the main thread is done just above this as well as the registering
        // of any dynamic properties by any plug-in was done a few lines above
        // on the InitPlugins.init() call. So we re initialize the base config
        // before proceeding.
        _config.initDTFProperties();
    }
    
    public DTFNode() throws DTFException {
        init();
        
        // init the debug server
        DebugServer.init();
        
        // read build id
        readBuildID();
        
        try { 
	        _state.setResults(new Results(new NullResults()));
	        // Setup Communications
	        _comm = new Comm(_config);
	        _comm.start();
	        
	        // Set COMM to default state
	        _state.setComm(_comm);
        } catch (DTFException e) { 
            _logger.error("Unable to start node.",e);
            DebugServer.shutdown();
            System.exit(-1);
        }
       
        writeNodeState(false, true, null);
    }
    
    /**
     * State file can be used by other external tools to know where each of 
     * the components is running and what certain port numbers and id's are for
     * this specific node.
     * 
     * @throws DTFException
     */
    public void writeNodeState(boolean done,
                               boolean succeeded,
                               DTFException dtfe) throws DTFException { 
        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
            String name = DTFNode.getType();
            if ( name.equals("dtfa") ) name = Action.getLocalID();
          
            File statedir = new File("state");
            if ( !statedir.exists() ) {
                if ( !statedir.mkdirs() )  {
                    throw new DTFException("Unable to mkdir [" + statedir + "]");
                }
            }
            
            fos = new FileOutputStream(new File(statedir, name + ".state"));
            pw = new PrintWriter(fos);
            
            CommServer cs = _comm.getCommServer();
            pw.write("dtf.node.type=" + getType() + "\n");
            pw.write("dtf.node.host=" + _config.getProperty("dtf.listen.addr") + "\n");
            pw.write("dtf.node.user=" + System.getProperty("user.name") + "\n");
            pw.write("dtf.node.home=" + SystemUtil.getCWD() + "\n");
            pw.write("dtf.debug.port=" + DebugServer.getInstance().getPort() + "\n");
            pw.write("dtf.listen.addr=" + cs.getAddress() + "\n");
            pw.write("dtf.listen.port=" + cs.getPort() + "\n");
            
            if ( done ) { 
                pw.write("dtf.node.exited=true\n");
                pw.write("dtf.node.succeeded=" + succeeded + "\n");
            }
        } catch (FileNotFoundException e) {
            throw new DTFException("Unable to create node state file.",e);
        } finally { 
            if ( pw != null ) pw.close();
        }
    }
    
    /** 
     * @dtf.feature Loading Default Properties
     * @dtf.feature.group DTF Properties
     *   
     * @dtf.feature.desc
     * <p>
     * Because sometimes agent command lines can get pretty long including the 
     * tunneling properties, agent specific attributes and other settings and 
     * writing that out every time on the command line can be bothersome, there 
     * is now a single property that can be used to load default properties into
     * the current component about to be executed. This property name is 
     * dtf.defaults and it will load a Java Properties file with key=value 
     * lines. There is a default behavior of making all properties available to
     * all the code within DTF but also any properties that do not start with 
     * dtf.* prefix will be loaded as Agent attributes that can later be used to
     * lock the components. In other words, if you have a property named 
     * cli.type=xxx in your default properties file and load it using this 
     * property you can then use the lockcomponent tag like so:
     * </p> 
     * 
     * {@dtf.xml
     * <lockcomponent id="AGENT${agent}">
     *     <attrib name="client.type" value="xxx"/>
     * </lockcomponent>}
     * 
     * And you know you'd be locking the component who had that property and 
     * not any other one. This is useful for the user of the framework to setup 
     * agents with properties that define location, build and even APIs available 
     * from that agent for executing tests.
     */
    private static void loadDefaults() throws DTFException { 
        String defaults = _config.getProperty(DTFProperties.DTF_DEFAULTS);
        if ( defaults != null ) { 
            _logger.info("Loading defaults from [" + defaults + "]");
            Properties props = new Properties();
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(defaults);
                props.load(fis);
            } catch (FileNotFoundException e) {
                throw new DTFException("Error reading default properties file.",e);
            } catch (IOException e) {
                throw new DTFException("Error reading default properties file.",e);
            } finally { 
                if ( fis != null ) { 
                    try { 
                        fis.close();
                    } catch (IOException e) { 
                        throw new DTFException("Error closing default properties file.",e);
                    }
                }
            }
            
            Enumeration keys = props.keys();
            while (keys.hasMoreElements()) { 
                String key = (String)keys.nextElement();
                String value = props.getProperty(key);
                _config.setProperty(key, value);
                
                if (!key.startsWith("dtf.")) { 
                    CommClient.addAgentAttribute(key, value, true);
                }
            }
        }
    }
   
    public void run() throws DTFException {
        String xmlFile = _config.getProperty(DTFProperties.DTF_XML_FILENAME);
        DTFException failure = null;
        
        try {
            if ( xmlFile != null && getType().equals(DTFConstants.DTFX_ID) ) { 
                FileInputStream fis = null;
                try { 
                    try { 
                        fis = new FileInputStream(xmlFile);
                    } catch (FileNotFoundException e) {
                        _logger.error("Unable to find file [" + xmlFile + "]");
                        throw new DTFException("Unable to load xml.",e);
                    }
                   
                    /*
                     * Record DTF properties once for this test.
                     */
                    String version = _state.getConfig().
                                         getProperty(DTFProperties.DTF_VERSION);
                    _state.getResults().
                              recordProperty(DTFProperties.DTF_VERSION,version);
                    
                    ScriptUtil.executeScript(xmlFile, fis,_state);
                } finally { 
                    try {
                        if (fis != null)
                            fis.close();
                    } catch (IOException e) {
                        if (_logger.isDebugEnabled())
                            _logger.debug("Failed to close xmlFile.",e);
                    }
                }
            } else {
                if (getType().equals(DTFConstants.DTFX_ID)) { 
                    throw new DTFException("DTFX supplied without " + 
                                           DTFProperties.DTF_XML_FILENAME);
                }
                
                while (_running && _comm.isUp()) {
                    // output important stats
                    ThreadUtil.pause(3000);
                }
            }
        } catch (DTFException e) { 
            failure = e;
        } finally { 
            _state.getCursors().close();
            _comm.shutdown();
            SeleniumServerFactory.shutdown();
            DebugServer.shutdown();
            _logger.info("Shutting down " + getType());
        }
      
        /*
         * Don't want the stack trace printed yet again, it's enough to print 
         * it once at the script level and then exit with a return code that
         * is different from 0
         */
        if (failure != null) {
            if (!failure.wasLogged())
                _logger.error("Failure during test execution.",failure);
           
            writeNodeState(true, false, failure);

            System.exit(-1);
        } else { 
            writeNodeState(true, true, failure);
        }
    }

    /**
     * Debug method used to print the status of the current DTF Node. In the case
     * of the DTFC this also prints the status of all of the connected 
     * components.
     * 
     * @param _writer
     * @throws DTFException 
     */
    public static void status(PrintWriter pw) throws DTFException {
        if ( _state.getComm() != null ) { 
	        CommServer cs = _state.getComm().getCommServer();
	        pw.write("dtf.node.type=" + getType() + "\n");
	        pw.write("dtf.node.host=" + _config.getProperty("dtf.listen.addr") + "\n");
            pw.write("dtf.node.user=" + System.getProperty("user.name") + "\n");
            pw.write("dtf.node.home=" + SystemUtil.getCWD() + "\n");
	        pw.write("dtf.debug.port=" + DebugServer.getInstance().getPort() + "\n");
	        pw.write("dtf.listen.addr=" + cs.getAddress() + "\n");
	        pw.write("dtf.listen.port=" + cs.getPort() + "\n");
	        
	        if ( getType().equals("dtfx") ) { 
	            Action action = Action.getState().getAction();
	            
	            if ( action != null ) { 
		            pw.write("dtf.xml.filename=" + action.getFilename() + "\n");
		            pw.write("dtf.xml.column=" + action.getColumn() + "\n");
		            pw.write("dtf.xml.line=" + action.getLine() + "\n");
	            }
	        }
	        
	        if ( getType().equals("dtfc") ) { 
	            pw.write("\n");
	            // print the status of the currently connected components
	            NodeState ns = NodeState.getInstance();
	            ArrayList<NodeInfo> nodes = ns.getRegisteredNodes();
	            
	            for (NodeInfo node : nodes) { 
	                String type = node.findAttrib("dtf.node.type");
	                String host = node.findAttrib("dtf.node.host");
	                
			        pw.write("dtf.node.type=" + type + "\n");
	                pw.write("dtf.node.id=" + node.getId() + "\n");
			        pw.write("dtf.node.host=" + host + "\n");
			        pw.write("dtf.node.user=" + node.findAttrib("dtf.node.user") + "\n");
			        pw.write("dtf.node.home=" + node.findAttrib("dtf.node.home") + "\n");
			        
			        if ( type.equals("dtfa") ) { 
				        pw.write("dtf.node.locked=" + node.isLocked() + "\n");
				        if ( node.isLocked() ) 
				            pw.write("dtf.node.owner=" + node.getOwner() + "\n");
			        }
			        
			        pw.write("dtf.debug.port=" + node.findAttrib("dtf.debug.port") + "\n");
			        pw.write("dtf.listen.addr=" + node.getAddress() + "\n");
			        pw.write("dtf.listen.port=" + node.getPort() + "\n");
			        pw.write("\n");
	            }
	        }
        }
    }
    
    public static String getBuildID() { return _buildid; } 
    
    private void readBuildID() throws DTFException { 
        InputStream is = getClass().getResourceAsStream("/build.id");
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        
        try {
            _buildid = br.readLine();
            if ( _buildid == null ) 
                throw new DTFException("Unable to determine build id.");
        } catch (IOException e) { 
            throw new DTFException("Unable to determine build id.",e);
        }
        
        if ( _logger.isDebugEnabled() ) 
            _logger.debug("BuildID [" + _buildid + "]");
    }
    
    public static void stop() {
        _running = false;
       
        if (getType().equals("dtfc")) { 
            // tell all connected components to disconnect
            NodeState ns = NodeState.getInstance();
            ArrayList<NodeInfo> nodes = ns.getRegisteredNodes();
            Stopcomponent stop = new Stopcomponent();
            
            for (int i = 0; i < nodes.size(); i++) { 
                NodeInfo node = nodes.get(i);
                try {
                    node.getClient().sendAction(node.getId(), stop);
                } catch (DTFException e) {
                    _logger.error("Failure to stop [" + node.getId() + "]",e);
                }
            }
        }

    }
    
    public static void main(String[] args) throws DTFException {
        DTFNode node = new DTFNode();
        node.run();
    }
}
