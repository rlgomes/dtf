package com.yahoo.dtf.components;

import java.util.HashMap;

import com.yahoo.dtf.DTFConstants;
import com.yahoo.dtf.actions.protocol.Lock;
import com.yahoo.dtf.exception.DTFException;

/** 
 * @dtf.feature Starting Components
 * @dtf.feature.group Component Setup
 *   
 * @dtf.feature.desc
 * <p>
 * Lets take the following scenario:
 * 
 * We have 2 client machines (lets call them cl1 and cl2), each of those client 
 * machines we copy from the DTF base directory the build/dist directory over 
 * to each of the client machines and any other machines we may be controlling 
 * with this test. You can also use the new ant task called deploy which will 
 * push the DTF build to any Linux box that you have ssh access to.
 * </p>
 * <p> 
 * Now on one of the client machines (cl1) will start the DTFC by executing the 
 * ant.sh run_dtfc you should see the following output:
 * </p>
 * <pre>
 *  > ./ant.sh run_dtfc
 *      Buildfile: build.xml
 *      init:
 *          [echo] Creating log dir logs/16-07-2008.10.11.52
 *          [mkdir] Created dir: /.../dtf/build/dtf/dist/logs/16-07-2008.10.11.52
 *      run_dtfc:
 *          [java] INFO  16/07/2008 10:11:53 DTFNode         - Starting dtfc component.
 *          [java] INFO  16/07/2008 10:11:53 Comm            - Host address [cl1]
 *          [java] INFO  16/07/2008 10:11:53 RPCServer       - Listening at 20000
 *          [java] INFO  16/07/2008 10:11:53 Comm            - DTFC Setup.
 * </pre>
 * <p>
 * This means the DTFC is up and running and ready to manage DTFA connections on
 * port 20000 of the machine it was executed on (this is the default but if for 
 * some reason port 20000 is taken then it will try to bind to the next available 
 * port and then you must make sure to use the -Ddtf.connect.port=X on the other 
 * components to tell them to connect to the non default port). There are some 
 * properties that can be used to control the which address/port the controller 
 * will bind to. They are dtf.listen.addr and dtf.listen.port and should be 
 * specified using the normal ant property definition with 
 * -Ddtf.listen.addr=XXX.
 * </p>
 * <p>
 * So now we can start an agent and have it connect to this already running 
 * DTFC. This is simple, from the same directory as before you can execute 
 * ant.sh run_dtfa but you need to tell it where to connect to if you're running 
 * on another machine like so:
 * <pre>
 *  >./ant.sh run_dtfa -Ddtf.connect.addr=cl1
 *  Buildfile: build.xml
 *      init:
 *          [echo] Creating log dir logs/16-07-2008.09.46.10
 *          [mkdir] Created dir: /.../dtf/logs/16-07-2008.09.46.10
 *      run_dtfa:
 *          [echo] Starting DTFA
 *          [echo] logging to logs/16-07-2008.09.46.10/dtfa.*.log
 *          [java] INFO  16/07/2008 09:46:12 DTFNode         - Starting dtfa component.
 *          [java] INFO  16/07/2008 09:46:13 Comm            - Host address [68.180.200.245]
 *          [java] INFO  16/07/2008 09:46:13 RPCServer       - Listening at 0.0.0.0:20000
 *          [java] INFO  16/07/2008 09:46:13 Comm            - DTFA Setup.
 *          [java] INFO  16/07/2008 09:46:13 CommClient      - Registering component at http://10.73.144.127:20000/xmlrpc
 *          [java] INFO  16/07/2008 09:46:15 Rename          - Node name set to: dtfa-0
 *          [java] INFO  16/07/2008 09:46:15 CommClient      - Connected to DTFC
 * </pre>
 * <p>
 * Once we see the “Connected to DTFC” line we know that this DTFA is connected 
 * to the controller and ready to tasks to execute. Now having executed one of 
 * these DTFA's from cl1 itself and another from cl2.
 * </p>
 * <p>
 * Now that we have the framework up and running we can use the DTFX to execute 
 * a given test case within the current DTF setup. Executing the DTFX is done 
 * like so:
 * </p>
 * <pre>
 *  ./ant.sh run_dtfx -Ddtf.xml.filename=tests/ut/echo.xml -Ddtf.connect.addr=cl1
 *  Buildfile: build.xml
 *      init:
 *          [echo] Creating log dir logs/16-07-2008.10.23.07 
 *          [mkdir] Created dir: /.../dist/logs/16-07-2008.10.23.07
 *      run_dtfx:
 *          [java] INFO  16/07/2008 10:23:08 DTFNode         - Starting dtfx component.
 *          [java] INFO  16/07/2008 10:23:08 InitHook        - Example InitHook got called.
 *          [java] INFO  16/07/2008 10:23:08 InitPlugins     - Called init for plugin [ydht_dtf.jar].
 *          [java] INFO  16/07/2008 10:23:08 Comm            - Host address [cl1]
 *          [java] INFO  16/07/2008 10:23:08 RPCServer       - Listening at 0.0.0.0:20001
 *          [java] INFO  16/07/2008 10:23:08 Comm            - DTFX Setup.
 *          [java] INFO  16/07/2008 10:23:09 Createstorage   - Creating storage: INPUT
 *          [java] INFO  16/07/2008 10:23:09 Echo            - Echoing a message on the component DTFA1
 * </pre>
 * <p>
 * So as simple as that we have execute the echo.xml test case from the DTF unit 
 * tests against the current DTF setup. Which only really needed one DTFA if you 
 * look at the output from the test case run we only lock on dtfa of the type 
 * dtfa and use that to do a remote echo. Here's what the remote echo looks like 
 * on the agent side:
 * </p>
 * <pre>
 *  [java] INFO  16/07/2008 09:46:15 Rename          - Node name set to: dtfa-0
 *  [java] INFO  16/07/2008 09:46:15 CommClient      - Connected to DTFC
 *  [java] INFO  16/07/2008 09:46:35 CommClient      - Remote echo on component DTFA1
 * </pre>
 * <p>
 * Now as you've probably noticed the components don't have to be on separate 
 * machines. This fact is used to unit test the framework from a single machine 
 * by starting all of the components DTFA, DTFC and DTFX on the same machine 
 * and running the necessary unit tests against this DTF setup. Now for other 
 * purposes such as performance testing we obviously need separate machines to 
 * be able to stress the product being tested correctly.
 * </p>
 */
public class Components {

    public HashMap<String, Lock> _elems = new HashMap<String, Lock>();
   
    public Components() { }
    
    public void registerComponent(String key, Lock lock) { _elems.put(key, lock); }
    public void unregisterComponent(String key) { _elems.remove(key); }
    
    public Lock getComponent(String key) throws DTFException {
        Object obj = _elems.get(key);
        
        if (obj == null) {
           
            if (key.equals(DTFConstants.DTFC_COMPONENT_ID)) 
                return new Lock("dtfc",null,0);
            
            throw new DTFException("Component with id: " + key + " not registered.");
        }
        
        return (Lock)obj;
    }
    
    public boolean hasComponents() { return _elems.size() != 0; } 
}
