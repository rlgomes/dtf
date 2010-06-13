package com.yahoo.dtf.storage;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import com.yahoo.dtf.NodeInfo;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.protocol.CleanUpHook;
import com.yahoo.dtf.actions.protocol.ReleaseAgent;
import com.yahoo.dtf.comm.rpc.Node;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.exception.StorageException;
import com.yahoo.dtf.state.ActionState;

public class RemoteStorage implements CleanUpHook {

    public static final String REMOTE_STORAGE_ID = "REMOTESTORAGEID";

    private static Object mutex = new Object();
    private static long id = 0;
    
    static { 
        ReleaseAgent.addCleanUpHook(new RemoteStorage());
    }
   
    /**
     * 
     * @return
     * @throws StorageException
     * @throws ParseException
     */
    public static String getRemoteStoragePath() 
           throws StorageException, ParseException {  
        StorageFactory sf = Action.getStorageFactory();
        init();
        return sf.retrieveStorage(REMOTE_STORAGE_ID).getPath();
    }
   
    /**
     * 
     * @throws StorageException
     * @throws ParseException
     */
    public static void init() throws StorageException, ParseException { 
        StorageFactory sf = Action.getStorageFactory();
        
        synchronized( mutex ) { 
            if (!sf.checkStorage(REMOTE_STORAGE_ID)) {
                sf.createStorage(REMOTE_STORAGE_ID,
                                 "remote-storage" + 
                                 File.separatorChar + NodeInfo.getLocalID(),
                                 false);
               
                StorageIntf storage = sf.getStorage(REMOTE_STORAGE_ID);
                storage.wipe();

            }
        } 
    }
    
    public void cleanup() throws DTFException {
        Action.getLogger().info("Cleaning up remote storages");
        ActionState as = ActionState.getInstance();
        as.getState("main").setStorage(new StorageFactory());
        as.getState(Node.BASE_CONFIG).setStorage(new StorageFactory());
    }
   
    /**
     * 
     * @param prefix
     * @param suffix
     * @return
     * @throws URISyntaxException
     * @throws StorageException
     */
    public static URI createUniqueURI(String prefix,
                                      String suffix)
           throws URISyntaxException, StorageException { 
        StorageFactory sf = Action.getStorageFactory();
        URI uri = null;
        do { 
            synchronized( mutex ) { 
                uri = new URI("storage://" + REMOTE_STORAGE_ID + 
                              "/" + prefix + (id++) + suffix);
            }
        } while (sf.exists(uri));
        
        return uri;
    }
   
    /**
     * 
     * @param uri
     * @return
     * @throws StorageException
     */
    public static boolean delete(URI uri) throws StorageException { 
        StorageFactory sf = Action.getStorageFactory();
        if ( sf.checkStorage(REMOTE_STORAGE_ID) ) {
            return new File(sf.getPath(uri)).delete();
        }        
        return false;
    }
}
