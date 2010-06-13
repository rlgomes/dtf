package com.yahoo.dtf.actions.function;

import java.io.InputStream;
import java.net.URI;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.ActionFactory;
import com.yahoo.dtf.exception.ActionException;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;


/**
 * @dtf.tag import
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Import functions/references from another XML file. If the 
 *               function already exists then the import will not overwrite
 *               the current implementation. This will make it so that various
 *               imports of functions from different files do not overwrite 
 *               each others implementations and instead the very first import
 *               of function X will take precedence to all subsequent 
 *               implementation.s
 *
 * @dtf.tag.example 
 * <local>
 *     <import uri="storage://INPUT/functions.xml"/> 
 * </local>  
 * 
 */
public class Import extends Action {

    /**
     * @dtf.attr uri
     * @dtf.attr.desc Specifies an XML file from where we will load functions 
     *                and XML references.
     */
    private String  uri = null;
    

    /**
     * @dtf.attr loadFuncs
     * @dtf.attr.desc Specifies if functions should be loaded from the specified 
     *                XML file.
     */
    private boolean loadFuncs = true;
    
    /**
     * @dtf.attr loadRefs
     * @dtf.attr.desc Specifies if references should be loaded from the 
     *                specified XML file.
     */
    private boolean loadRefs = true;
    
    public void execute() throws DTFException {
        getLogger().info("Importing elements from " + this);
        InputStream is = getStorageFactory().getInputStream(getUri());
        String filename = getStorageFactory().getPath(getUri());
        ActionFactory.parseAction(is,filename, getLoadfuncs(),getLoadrefs());
    }

    public void setUri(String uri) { this.uri = uri; }
    public URI getUri() throws ActionException, ParseException { return parseURI(uri); }
    
    public void setLoadfuncs(boolean loadFuncs) { this.loadFuncs = loadFuncs; }
    public boolean getLoadfuncs() { return loadFuncs; } 

    public void setLoadrefs(boolean loadRefs) { this.loadRefs = loadRefs; }
    public boolean getLoadrefs() { return loadRefs; } 
}
