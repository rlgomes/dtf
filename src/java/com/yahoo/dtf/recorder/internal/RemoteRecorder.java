package com.yahoo.dtf.recorder.internal;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import com.yahoo.dtf.DTFConstants;
import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.flowcontrol.Sequence;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.exception.RecorderException;
import com.yahoo.dtf.exception.StorageException;
import com.yahoo.dtf.query.Cursor;
import com.yahoo.dtf.query.TxtQuery;
import com.yahoo.dtf.recorder.Event;
import com.yahoo.dtf.recorder.RecorderBase;
import com.yahoo.dtf.recorder.TextRecorder;
import com.yahoo.dtf.storage.RemoteStorage;
import com.yahoo.dtf.util.CLIUtil;

public class RemoteRecorder extends RecorderBase {

    private String _threadId = null;
    private URI _uri = null;
   
    private RecorderBase _recorder = null;
  
    public RemoteRecorder(Action action,
                          boolean append,
                          String threadId) throws RecorderException {
        super(null, append,DTFConstants.DEFAULT_ENCODING);
        _threadId = threadId;
        
        try { 
            RemoteStorage.init();
            _uri = RemoteStorage.createUniqueURI("events-", ".txt");
            _recorder = new TextRecorder(_uri,
                                         append,
                                         DTFConstants.DEFAULT_ENCODING);
            
            if (Action.getLogger().isDebugEnabled())
                Action.getLogger().debug("Created file " + _uri);
        } catch (StorageException e) {
            throw new RecorderException("Unable to open remote storage.",e);
        } catch (URISyntaxException e) {
            throw new RecorderException("Unable to open remote storage.",e);
        } catch (ParseException e) {
            throw new RecorderException("Unable to open remote storage.",e);
        }
       
    }
    
    public void record(Event event) throws RecorderException {
        _recorder.record(event);
    }
   
    public void stop() throws RecorderException { 
        try {
            _recorder.stop();
          
            TxtQuery query = new TxtQuery();
            query.open(_uri, null, null, null, null, DTFConstants.DEFAULT_ENCODING);
            Cursor cursor = new Cursor(query);
            int count = 0;

            if (DTFNode.getOwner() != null) { 
                String ownerId = DTFNode.getOwner().getOwner();
                
                Sequence seq = new Sequence();
                seq.setThreadID(_threadId);
                
                while ( true ) { 
                    HashMap<String, String> map = cursor.next(false);
                        
                    if (map == null) 
                        break;
                        
                    Event event = CLIUtil.hashMapToEvent(map);
                    seq.addAction(event);
                    count++;
                        
                    if (count > 100) { 
                        Action.getComm().sendAction(ownerId, seq);
                        seq.clearChildren();
                        count = 0;
                    }
                }
               
                if (seq.children().size() != 0)
                    Action.getComm().sendAction(ownerId, seq);
            }
        } catch (StorageException e) {
            throw new RecorderException("Error handling remote events.",e);
        } catch (DTFException e) {
            throw new RecorderException("Error handling remote events.",e);
        } finally { 
            try { 
                RemoteStorage.delete(_uri);
            } catch (StorageException e) {
                throw new RecorderException("Error cleaning up event file.",e);
            }
        }
    }
    
    public void start() throws RecorderException { }
}
