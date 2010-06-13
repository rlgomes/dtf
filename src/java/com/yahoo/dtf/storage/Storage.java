package com.yahoo.dtf.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import com.yahoo.dtf.storage.Storage;
import com.yahoo.dtf.exception.StorageException;
import com.yahoo.dtf.logger.DTFLogger;
import com.yahoo.dtf.util.MultiMemberGZIPInputStream;
import com.yahoo.dtf.util.SystemUtil;

public class Storage extends StorageIntf {
  
    private static DTFLogger _logger = DTFLogger.getLogger(Storage.class);

    private File _fpath = null;
    
    private HashMap<String, ModifiedOptions> _modified = null;
    
    private static class ModifiedOptions {
        public boolean append = false;
        public long modified = 0;
        public long offset = 0;
        
        public ModifiedOptions(long modified,
                               boolean append,
                               long offset) { 
            this.modified = modified;
            this.append = append;
            this.offset = offset;
        }
    }
    
    public Storage(String id, String path, boolean export) throws StorageException {
        super(id,path,export);

        _fpath = new File(path);
        
        if ( !_fpath.exists() ) {
            _logger.warn("Storage path does not exist [" + path + 
                         "] will create.");
            
            if ( !_fpath.mkdirs() )
                throw new StorageException("Unable to create storage path [" + 
                                           path + "], check folder permissions/ownership");
        }
        
        _fpath = new File(getPath());
        _modified = new HashMap<String, ModifiedOptions>();
    }
    
    public boolean exists(String filename) { 
        return new File(_fpath.getAbsolutePath() + 
                        File.separatorChar + filename).exists();
    }
    

    
    public OutputStream getOutputStream(String filename, boolean append) 
           throws StorageException {
        try {
            File file = new File(_fpath.getAbsolutePath() + 
                                 File.separatorChar + filename);
            
            if (!append && file.isDirectory()) {
                _logger.info("Wiping " + file);
                SystemUtil.deleteDirectory(file);
            }
          
            FileOutputStream fos = new FileOutputStream(file, append);
            modifiedUpdate(file.getName(), append, file.length());
            return fos;
        } catch (IOException e) {
            throw new StorageException("Unable to retrieve OutputStream for storage: " + getId(),e);
        }
    }
    
    public InputStream getInputStream(String filename,
                                      boolean checkForCompression)
           throws StorageException { 
        try {
            // XXX: code needs a bit of a better clean up... but is fine for now.
            // if the file with the same name and just a .gz extension exists
            // then just load that file instead because its the compressed 
            // version of this same file.
            if (new File(_fpath.getAbsolutePath() + File.separatorChar + 
                    filename + ".gz").exists()) { 
                filename += ".gz";
            }
            
            if (filename.endsWith(".gz")) { 
                return new MultiMemberGZIPInputStream(filename,this);
            } else { 
                return getInputStream(filename);
            }
        } catch (IOException e) {
            throw new StorageException("Unable to retrieve InputStream for storage: " + getId(),e);
        }
    }
    
    public InputStream getInputStream(String filename) 
           throws StorageException {
        try {
            return new FileInputStream(_fpath.getAbsolutePath() + 
                                       File.separatorChar + filename);
        } catch (FileNotFoundException e) {
            throw new StorageException("Unable to retrieve InputStream for storage: " + getId(),e);
        }
    }
    
    @Override
    public void createPath(String path) throws StorageException {
        File aux = new File(_fpath, path);

        if ( !aux.mkdirs() )
            throw new StorageException("Unable to create [" + 
                                       aux.getAbsolutePath() + "]");
    }
    
    @Override
    public void wipe() throws StorageException {
        wipe(_fpath);
    }
    
    /**
     * This just wipes the directories for any files that shouldn't be there.
     * Nothing is done to the directory structure itself and .nfs files are 
     * avoided since they sometimes refuse to be deleted.
     */
    private void wipe(File path) throws StorageException {
        String[] files = path.list(new FilenameFilter() { 
	            public boolean accept(File dir, String name) {
	                return !name.startsWith(".nfs");
	            }
	        }
        );

        if (files != null) { 
            for (int i = 0; i < files.length; i++) { 
                File file = new File(path.getAbsolutePath() + File.separatorChar +  files[i]);
               
                if (file.isFile())  {
                    if ( !file.delete() ) {
                        throw new StorageException("Unable to delete file [" + file + 
                                                   "]");
                    }
                } else {
                    wipe(file);
                }
            }
        }
    }
    
    @Override
    public boolean isDirectory(String path) {
        return new File(_fpath, path).isDirectory();
    }
    
    @Override
    public String getFullPath() { 
        return _fpath.getPath();
    } 
    
    @Override
    public String[] getFiles() {
        return _fpath.list();
    }

    @Override
    public String[] getFiles(String path) {
        return new File(_fpath, path).list();
    }
    
    @Override
    public Long getLastModified(String filename) {
        ModifiedOptions mo = _modified.get(filename);
        
        if ( mo == null ) {
            return 0L;
        }
       
        return mo.modified;
    }
    
    @Override
    public boolean openedAsAppend(String filename) {
        ModifiedOptions mo = _modified.get(filename);
        if ( mo == null )
            return false;
        else
            return mo.append;
    }
    
    @Override
    public long lastOpenedOffset(String filename) {
        ModifiedOptions mo = _modified.get(filename);
        if ( mo == null )
            return 0;
        else 
            return mo.offset;
    }
    
    private void modifiedUpdate(String filename, boolean append, long offset) { 
        ModifiedOptions mo = _modified.get(filename);

        if ( mo == null ) {
            mo = new ModifiedOptions(0, append, offset);
            _modified.put(filename, mo);
        }
       
        if ( append == false || (mo.offset == 0 && !mo.append) )
            mo.offset = offset;

        mo.append = append;
        mo.modified++;
    }
}
