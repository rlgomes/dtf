package com.yahoo.dtf.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.Map.Entry;

import com.yahoo.dtf.state.DTFState;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.comm.Comm;
import com.yahoo.dtf.components.Components;
import com.yahoo.dtf.config.Config;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.functions.Functions;
import com.yahoo.dtf.query.Cursors;
import com.yahoo.dtf.recorder.Recorder;
import com.yahoo.dtf.references.References;
import com.yahoo.dtf.results.Results;
import com.yahoo.dtf.storage.StorageFactory;


/*
 * TODO: Cleanup of this code and it's logic is needed, probably will benefit 
 *       from using ThreadLocal
 */
public class DTFState {
    
    private Config config = null;
    private StorageFactory storage = null;
    private Comm comm = null;
    private Components components = null;
   
    private Recorder recorder = null;
    private Cursors cursors = null;

    private Hashtable context = null;
   
    private Hashtable globalContext = null;
    
  
    /*
     * maintained by the ScriptUtil class to point to the root of the current
     * script
     */
    private Action root = null;
    
    private Action current = null;
    
    private Results results = null;

    private boolean replace = true;
    
    private References references = null;
    private Functions functions = null;
    
    
    public DTFState(Config config, StorageFactory storage) throws DTFException {
        this.config = config;
        this.storage = storage;
        this.context = new Hashtable();
        this.globalContext = new Hashtable();
        this.cursors = new Cursors();
        this.references = new References();
        this.functions = new Functions();
    }
    
    public DTFState(Config config, 
                    StorageFactory storage, 
                    Comm comm,
                    Components components,
                    Recorder recorder,
                    Cursors cursors,
                    References references,
                    Results results,
                    Functions functions,
                    Hashtable contexts,
                    Hashtable globalContexts) {
        this.config = config;
        this.storage = storage;
        this.comm = comm;
        this.components = components;
        this.recorder = recorder;
        this.context = contexts;
        this.globalContext = globalContexts;
        this.cursors = cursors;
        this.references = references;
        this.functions = functions;
        this.results = results;
    }
    
    public Config getConfig() { return config; }
    public void setConfig(Config config) { this.config = config; }

    public StorageFactory getStorage() { return storage; }
    public void setStorage(StorageFactory storage) { this.storage = storage; }

    public Comm getComm() { return comm; }
    public void setComm(Comm comm) { this.comm = comm; }
    
    public void disableReplace() { replace = false; }
    public void enableReplace() { replace = true; }
    public boolean replace() { return replace; }
    
    public void registerContext(String key, Object value) { context.put(key, value); }
    public void unRegisterContext(String key) { context.remove(key); }
    public Object getContext(String key) { return context.get(key); }
    public void resetContext() { context = new Hashtable(); }

    public void registerGlobalContext(String key, Object value) { globalContext.put(key, value); }
    public void unRegisterGlobalContext(String key) { globalContext.remove(key); }
    public Object getGlobalContext(String key) { return globalContext.get(key); }
    public void setGlobalContext(Hashtable globalCtx) { globalContext = globalCtx; }
    public Hashtable getGlobalContext() { return globalContext; }
    public void resetGlobalContext() { globalContext = new Hashtable(); }
    
    public void setAction(Action action) { current = action; }
    public Action getAction() { return current; }
    
    public Object clone() throws CloneNotSupportedException {
        throw new Error("Method not implemented, use duplicate().");
    }
    
    public Object recursiveDuplication(Object obj) { 
        /*
         * If we have collections inside of the context we need to make 
         * sure to clone those, otherwise we're not giving a duplicate that
         * can safely be used by another thread.
         */
        Object result = obj;
        
        if ( obj instanceof Vector ) {
            Vector v = new Vector();
            Vector aux = (Vector)obj;
            for (int i = 0; i < aux.size(); i++) { 
                v.add(recursiveDuplication(aux.get(i)));
            }
            result = v;
        } else if ( obj instanceof ArrayList ) {
            ArrayList a = new ArrayList();
            ArrayList aux = (ArrayList)obj;
            for (int i = 0; i < aux.size(); i++) { 
                a.add(recursiveDuplication(aux.get(i)));
            }
            result = a;
        } else if ( obj instanceof Hashtable ) {
            Hashtable h = new Hashtable();
            Hashtable aux = (Hashtable)obj;
            Iterator<Entry> entries = aux.entrySet().iterator();
            while (entries.hasNext()) { 
                Entry entry = entries.next();
                h.put(entry.getKey(), recursiveDuplication(entry.getValue()));
            }
            result = h;
        } else if  ( obj instanceof HashMap ) {
            HashMap h = new HashMap();
            HashMap aux = (HashMap)obj;
            Iterator<Entry> entries = aux.entrySet().iterator();
            while (entries.hasNext()) { 
                Entry entry = entries.next();
                h.put(entry.getKey(), recursiveDuplication(entry.getValue()));
            }
            result = h;
        } 
        
        return result;
    }
    
    public DTFState duplicate() {
        Hashtable ctx =  new Hashtable();
        Iterator<Entry> entries = context.entrySet().iterator();
        while ( entries.hasNext() ) { 
            Entry entry = entries.next();
            Object obj = entry.getValue();
            ctx.put(entry.getKey(), recursiveDuplication(obj));
        }
        
        DTFState state = new DTFState((Config)config.clone(),
                                      storage,
                                      comm,
                                      components,
                                      recorder,
                                      cursors,
                                      references,
                                      results,
                                      functions,
                                      ctx,
                                      globalContext);
        state.setRoot(root);
        return state;
    }
    
    public void setRoot(Action root) { this.root = root; }
    public Action getRoot() { return root; }

    public Components getComponents() { return components; }
    public void setComponents(Components components) { this.components = components; }

    public Recorder getRecorder() { return recorder; }
    public void setRecorder(Recorder recorder) {  this.recorder = recorder;  }

    public Results getResults() { return results; }
    public void setResults(Results results) {  this.results = results;  }

    public Cursors getCursors() { return cursors; }
    public void setCurors(Cursors cursors) { this.cursors = cursors; }
    
    public References getReferences() { return references; }
    public void setReferences(References references) { this.references = references; }
    
    public Functions getFunctions() { return functions; } 
    public void setFunctions(Functions functions) { this.functions = functions; } 
}
