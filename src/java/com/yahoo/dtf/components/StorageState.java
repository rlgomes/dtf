package com.yahoo.dtf.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.yahoo.dtf.DTFConstants;
import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.file.PullFile;
import com.yahoo.dtf.actions.protocol.CleanUpHook;
import com.yahoo.dtf.actions.storage.Createstorage;
import com.yahoo.dtf.comm.rpc.Node;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.state.ActionState;
import com.yahoo.dtf.storage.StorageFactory;
import com.yahoo.dtf.storage.StorageIntf;

public class StorageState implements ComponentHook,
                                     ComponentReturnHook,
                                     ComponentUnlockHook,
                                     CleanUpHook {
    
    private static String SSFILES   = "dtf.storage.storages.ctx";
    private static String SSSTORAGE = "dtf.storage.files.ctx";
   
    private static Object getGlobalContext(String id ) { 
        if ( DTFNode.getType().equals(DTFConstants.DTFX_ID) ) { 
            return Action.getGlobalContext(id);
        }
       
        ActionState as = ActionState.getInstance();
        return as.getState(Node.BASE_CONFIG).getGlobalContext(id);
    }
    
    private static void registerGlobalContext(String id, Object obj ) { 
        if ( DTFNode.getType().equals(DTFConstants.DTFX_ID) ) { 
            Action.registerGlobalContext(id, obj);
        }
        
        ActionState as = ActionState.getInstance();
        as.getState(Node.BASE_CONFIG).registerGlobalContext(id, obj);
    }
    
    private static HashMap<String, HashMap<String,Long>> getSent(String id) { 
        HashMap<String,HashMap<String,HashMap<String,Long>>> sent =
            (HashMap<String,HashMap<String,HashMap<String,Long>>>)
                                                      getGlobalContext(SSFILES);

        if ( sent == null ) { 
            sent = new HashMap<String,HashMap<String,HashMap<String,Long>>>();
            registerGlobalContext(SSFILES, sent);
        }

        HashMap<String, HashMap<String,Long>> sentfiles = sent.get(id);
        if ( sentfiles == null ) { 
            sentfiles = new HashMap<String, HashMap<String,Long>>();
            sent.put(id, sentfiles);
        }
        
        return sentfiles;
    }
    
    
    private static ArrayList<String> getStorages(String id) { 
        HashMap<String, ArrayList<String>>  cstorage =
               (HashMap<String, ArrayList<String>>) getGlobalContext(SSSTORAGE);

        if ( cstorage == null ) { 
           cstorage = new HashMap<String, ArrayList<String>>();
           registerGlobalContext(SSSTORAGE, cstorage);
        }
        
        ArrayList<String> sentstorages = cstorage.get(id);
        if ( sentstorages == null ) { 
            sentstorages = new ArrayList<String>();
            cstorage.put(id, sentstorages);
        }
        
        return sentstorages;
    }
    
    public synchronized ArrayList<Action> handleComponent(String id)
           throws DTFException {
        long start = System.currentTimeMillis();
        ArrayList<Action> result = new ArrayList<Action>();
        
        StorageFactory sf = Action.getStorageFactory();
        HashMap<String, StorageIntf> storages = sf.getStorages();
        HashMap<String, HashMap<String,Long>> sentfiles = getSent(id);
        ArrayList<String> sentstorages = getStorages(id);
        
        Iterator<Entry<String,StorageIntf>> entries = 
                                                 storages.entrySet().iterator();
      
        boolean exported = false;
        
        while ( entries.hasNext() ) {
            Entry<String,StorageIntf> entry = entries.next();
            String key = entry.getKey();
            StorageIntf storage = entry.getValue();
          
            if ( !storage.isExportable() )
                continue;

            exported = true;
            
            String cid = Action.getLocalID();
            
            if ( !DTFNode.getType().equals(DTFConstants.DTFX_ID) )
                cid = DTFNode.getOwner().getId();
            
            if ( !sentstorages.contains(key) ) { 
                // this storage has not been created on the component mentioned.
                Createstorage cs = new Createstorage();
                cs.setId(key);
                cs.setPath(storage.getPath());
                cs.setExport("true");

                if ( Action.getLogger().isDebugEnabled() ) {
                    Action.getLogger().debug("Creating storage [" + key + 
                                             "] on [" + id + "]");
                }

                StorageStateUpdater ssu = 
                                    new StorageStateUpdater(cid,
                                                            storage.getId());
                result.add(cs);
                result.add(ssu);
                
                sentstorages.add(key);
            } else { 
                if ( Action.getLogger().isDebugEnabled() )
                    Action.getLogger().debug(id + " already has storage [" + 
                                             key + "]");
            }
           
            HashMap<String,Long> sfiles = sentfiles.get(key);
            if ( sfiles == null ) { 
                sfiles = new HashMap<String,Long>();
                sentfiles.put(key, sfiles);
            }
          
            String[] files = storage.getFiles(); 
            if ( files != null ) { 
                
                if ( files.length == 0 ) { 
                    if ( Action.getLogger().isDebugEnabled() )
                        Action.getLogger().debug("[" + storage + 
                                                 "] storage is empty."); 
                }
                
	            for (int i = 0; i < files.length; i++) { 
	                Long l = sfiles.get(files[i]);
	                Long m = storage.getLastModified(files[i]);
	            
	                if ( l == null || l < m ) {  
	                    String sid = storage.getId();
	                    String uri = "storage://" + sid + "/" + files[i];
	                    PullFile pf = new PullFile();
	                   
	                    if ( DTFNode.getType().equals("dtfa") ) 
	                        pf.setOwner(DTFNode.getOwner().getOwner());
	                    
	                    pf.setRemotefile(storage.getFullPath() + "/" + files[i]);
	                    pf.setUri(uri);
	                    pf.setTo(Action.getLocalID());
	                   
	                    if ( DTFNode.getType().equals(DTFConstants.DTFX_ID) ) {
	                        pf.setAppend("false");
	                    } else {
	                        pf.setAppend("" + storage.openedAsAppend(files[i]));
	                        pf.setOffset(storage.lastOpenedOffset(files[i]));
	                    }
                        
	                    StorageFileUpdater sfu = 
	                                 new StorageFileUpdater(cid, sid, files[i]);
	                    
	                    result.add(pf);
	                    result.add(sfu);
	
	                    if ( Action.getLogger().isDebugEnabled() ) {
	                        Action.getLogger().debug("sending file [" + uri + 
	                                                 "] to [" + id +
	                                                 "] modified [" + m + 
	                                                 "] append [" + 
	                                                 pf.getAppend() + "]");
	                    }
	                    sfiles.put(files[i], m);
	                } else { 
	                    if ( Action.getLogger().isDebugEnabled() ) {
	                        Action.getLogger().debug("not sending file [" +
	                                                 files[i] +  "] to [" +
	                                                 id + "] modified [" + m + 
	                                                 "] synced at [" + l + "]");
	                    }
	                }
	            }
            } else { 
                if ( Action.getLogger().isDebugEnabled() ) {
                    Action.getLogger().debug("[" + storage + 
                                             "] storage is empty."); 
                }
            }
        }
        
        long stop = System.currentTimeMillis();
        if ( Action.getLogger().isDebugEnabled() && exported ) {
            long dur = (stop-start);
            Action.getLogger().debug("Time to sync storages: " + dur + "ms");
        }

        return result;
    }
    
    public ArrayList<Action> returnToRunner(String id) throws DTFException {
        ArrayList<Action> result = handleComponent(id);
        return result;
    }
    
    public  static void updateStorage(String componentid, String storageid) { 
        ArrayList<String> sentstorages = getStorages(componentid);
        
        if ( Action.getLogger().isDebugEnabled() ) { 
            Action.getLogger().debug("Updated storage [" + storageid + 
                                     "] for [" + componentid + "]");
        }

        sentstorages.add(storageid);
    }
    
    public static void updateLastModified(String componentid,
                                          String storageid, 
                                          String filename) { 
        StorageFactory sf = Action.getStorageFactory();
        StorageIntf storage = sf.getStorages().get(storageid);
        Long m = storage.getLastModified(filename);

        // get all storages and files that were sent to this componentid
        HashMap<String, HashMap<String,Long>> sentfiles = getSent(componentid);
       
        // only the specific files that were sent for this storageid
        HashMap<String, Long> sfiles = sentfiles.get(storageid);

        if ( sfiles == null ) {
            sfiles = new HashMap<String, Long>();
            sentfiles.put(storageid, sfiles);
        }
       
        if ( Action.getLogger().isDebugEnabled() ) { 
	        Action.getLogger().debug("Updating [" + storageid + ": " + 
	                                 filename + "] for [" + componentid + 
	                                 "] modified at [" + m + "]");
        }

        // simply update the last modified so we have a starting point
        sfiles.put(filename, m);
    }
    
    public void unlockComponent(String id) throws DTFException {
        HashMap<String, HashMap<String,Long>> sentfiles = getSent(id);
        ArrayList<String> sentstorages = getStorages(id);
        
        if  ( sentstorages != null ) 
            sentstorages.clear();
        
        if  ( sentfiles != null ) 
            sentfiles.clear();
    }
    
    public void cleanup() throws DTFException {
        Action.unRegisterGlobalContext(SSFILES);
        Action.unRegisterGlobalContext(SSSTORAGE);
    }
}
