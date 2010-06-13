package com.yahoo.dtf.actions.storage;

import java.io.File;

import com.yahoo.dtf.DTFConstants;
import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.exception.StorageException;
import com.yahoo.dtf.storage.RemoteStorage;

/**
 * @dtf.tag createstorage
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc <p>
 *               Identifies and creates a storage to be used by various other 
 *               tags within the DTF framework.
 *               </p>
 *               <p>
 *               This tag also creates a new property constructed with the ID 
 *               of this storage and the suffix .dir. So for a createstorage 
 *               with an ID OUTPUT the property ${OUTPUT.dir} will be created. 
 *               This property can be used to reference the physical location 
 *               of the storage.
 *               </p>
 * 
 * @dtf.tag.example 
 * <local>
 *     <createstorage id="INPUT" path="${dtf.path}/tests/ut/input"/>
 *     <createstorage id="OUTPUT" path="${dtf.path}/tests/ut/output"/>
 * </local>
 */
public class Createstorage extends Action {
    
    private final static String STORAGE_SUFFIX = "dir";

    /**
     * @dtf.attr id
     * @dtf.attr.desc This specifies the unique identifier by which all other 
     *                tags can refer to this storage.
     */
    private String id = null;
    
    /**
     * @dtf.attr path
     * @dtf.attr.desc File system path to a directory that we wish to reference 
     *                from other tags in this test case.
     */
    private String path = null;
   
    /**
     * @dtf.attr export
     * @dtf.attr.desc With export set to true this storage will be visible and
     *                usable from any agent locked by the current runner. This
     *                of course has a small overhead when communicating with 
     *                the component but the benefit is that all input and 
     *                outputs done on either side are pulled back to the storage
     *                on the runner side, making the writing of tests easier to
     *                read. The default of this attribute is set to false since
     *                in most scenarios users don't need to share the storages
     *                with all of their agents.
     */
    private String export = null;

    public Createstorage() { }
    
    public void execute() throws StorageException, ParseException {
        String path = getPath();
        
        if ( !DTFNode.getType().equals(DTFConstants.DTFX_ID) )
            path = RemoteStorage.getRemoteStoragePath() + File.separatorChar + path;
        
        getLogger().info("Creating storage: " + getId() + " at " + path);
        getStorageFactory().createStorage(getId(), path, getExport());
        /*
         * Create a property with the name of the storage and a suffix that can
         * be used later to reference the exact physical location of this storage
         */
        getConfig().setProperty(getId() + "." + STORAGE_SUFFIX,
                                new File(getPath()).getAbsolutePath());
    }

    public String getId() throws ParseException { return replaceProperties(id); }
    public void setId(String id) { this.id = id; }

    public String getPath() throws ParseException { return replaceProperties(path); }
    public void setPath(String path) { this.path = path; }

    public boolean getExport() throws ParseException { return toBoolean("export",export); }
    public void setExport(String export) { this.export = export; }
}
