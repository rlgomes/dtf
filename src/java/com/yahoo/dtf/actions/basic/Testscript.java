package com.yahoo.dtf.actions.basic;

import java.net.URI;
import java.util.ArrayList;

import com.yahoo.dtf.DTFProperties;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.util.ScriptUtil;
import com.yahoo.dtf.components.Components;
import com.yahoo.dtf.exception.ActionException;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.state.DTFState;
import com.yahoo.dtf.storage.StorageFactory;


/**
 * @dtf.tag testscript
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc The testscript tag allows you to execute an external XML script
 *               by identifying where it is with the <code>uri</code> attribute.
 *               The properties,references are inherited from the parent script, 
 *               but all components and storages are not inherited from the 
 *               parent script. 
 *
 * @dtf.tag.desc This tag generates test result events that can be recorded with
 *               the results tag and later processed to keep track of test 
 *               results over of time or builds.
 *               
 * @dtf.tag.example 
 * <testscript uri="storage://INPUT/storage.xml"/>
 * 
 */
public class Testscript extends Action {

    public final static String TESTSCRIPT_FAILED_CTX = 
                                                    "dtf.testscript.failed.ctx";
    /**
     * @dtf.attr uri
     * @dtf.attr.desc The uri identifies the location of the script to be 
     *                executed.
     */
    private String uri = null;

    public Testscript() { }
 
    private Object _lock = new Object();
    
    public void execute() throws DTFException {
        getLogger().info("Executing " + uri);
      
        DTFState state = (DTFState) getState().duplicate();
        // necessary so we don't have collisions, plus we don't want to 
        // pass down global context to a different testcase.
        state.resetGlobalContext();
        state.setComponents(new Components());
        state.setStorage(new StorageFactory());
        state.getConfig().setProperty(DTFProperties.DTF_XML_FILENAME,
                                      getStorageFactory().getPath(getUri()));
        // Set the node name to null to register this new testscript 
        state.getConfig().initDTFProperties();
        
        // Execute testproperty tags that will automatically record their value
        // for the next test result generated.
        executeChildren();

        try { 
            StorageFactory sf = getStorageFactory();
	        ScriptUtil.executeScript(sf.getPath(getUri()),
	                                 sf.getInputStream(getUri()), 
	                                 state);
        } catch (DTFException e) { 
            getLogger().error("Failed [" + getUri() + "]");
            Testsuite ts = (Testsuite) getGlobalContext(Testsuite.TESTSUITE_CTX);
           
            if ( ts != null ) { 
                if (!ts.getContinueonfailure() ) {
                    throw e;        
                } else { 
                    synchronized (_lock) { 
                        ArrayList<DTFException> exceptions = 
                            (ArrayList<DTFException>)
                                        getGlobalContext(TESTSCRIPT_FAILED_CTX);

                        if ( exceptions == null ) { 
                            exceptions = new ArrayList<DTFException>();
                            registerGlobalContext(TESTSCRIPT_FAILED_CTX, exceptions); 
                        }
                        
                        exceptions.add(e);
                    }
                }
            } else { 
                throw e;
            }
        }
    }

    public URI getUri() throws ActionException, ParseException { return parseURI(uri); }
    public void setUri(String uri) throws ActionException { this.uri = uri; }
}
