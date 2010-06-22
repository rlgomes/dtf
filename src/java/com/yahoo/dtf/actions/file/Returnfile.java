package com.yahoo.dtf.actions.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.zip.GZIPOutputStream;

import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.storage.StorageFactory;
import com.yahoo.dtf.storage.StorageIntf;

public abstract class Returnfile extends Action {
  
    
    protected String uri = null;
    
    private static int chunkSize = 64*1024; // default chunk size of 64KB
    
    public Returnfile() { }
    
    public String getUri() throws DTFException { return replaceProperties(uri); }
    public void setUri(String uri) { this.uri = uri; }

    public static void pushFileToRunner(String uri, 
                                        int offset, 
                                        String remotefile,
                                        boolean append,
                                        boolean compress) throws DTFException {
        if ( DTFNode.getOwner() != null ) {
            String owner = DTFNode.getOwner().getOwner();
            pushFile(owner, uri, offset, remotefile, append, compress);
        } else { 
            /*
             * Special situation in which you may use the pushFileToRunner on 
             * the runner itself and all we have to do is some copying of the 
             * file data between storages.
             */
            File file = new File(remotefile);
            StorageFactory sf = getStorageFactory();
            URI urix = parseURI(uri);
           
            if ( file.isDirectory()) { 
                StorageIntf storage = sf.getStorage(urix.getHost());
                String rpath = remotefile.replace(storage.getFullPath(), "");
                
                storage.createPath(rpath);
                String[] files = storage.getFiles(rpath);
                for (int i = 0; i < files.length; i++) { 
                    String auxfile = remotefile + "/" + files[i]; 
                    String auxuri = uri + "/" + files[i];
                    pushFileToRunner(auxuri, offset, auxfile, append, compress);
                }
            } else { 
                OutputStream os = null;
                FileInputStream fis = null;
	            try { 
	                os = sf.getOutputStream(urix);
		            fis = new FileInputStream(remotefile);
		            
		            byte[] bytes = new byte[chunkSize];
		            int read; 
		            while ( ( read = fis.read(bytes)) != -1 ) { 
		                os.write(bytes,0,read);
		            }
	            } catch(IOException e) { 
	                throw new DTFException("Unable to copy file.",e);
	            } finally { 
	                if ( os != null )
                        try { os.close(); } catch (IOException ignore) {}
	                
	                if ( fis != null ) 
                        try { fis.close(); } catch (IOException ignore) {}
	            }
            }
        }
    }

    /**
     * 
     * @param id
     * @param uri
     * @param offset
     * @param remotefile
     * @param append
     * @param compress
     * @throws DTFException
     */
    protected static void pushFile(String id,
                                   String uri,
                                   long offset,
                                   String remotefile,
                                   boolean append,
                                   boolean compress) throws DTFException {
        FileInputStream fis = null;
        File tmpfile = null;
        String sourcefile = null;

        if ( getLogger().isDebugEnabled() )
            getLogger().debug("Push file [" + remotefile + "] to " + id + 
                              " append: " + append);

        try {
            if (compress) {
                // just add the extension so we don't forget it was compressed.
                uri+=".gz";
                
                if (getLogger().isDebugEnabled())
                    getLogger().debug("Using compression.");
                
                tmpfile =  File.createTempFile("dtf",".gz");
                tmpfile.deleteOnExit();
              
                File rFile = new File(remotefile);
                String path = rFile.getAbsolutePath();
                gzip(new File(path),tmpfile);
                fis = new FileInputStream(tmpfile);  
                sourcefile = tmpfile.getAbsolutePath();
            } else { 
                File file = new File(remotefile);
                StorageFactory sf = getStorageFactory();

                if ( file.isDirectory() ) { 
                    URI urix = parseURI(uri); 
                    StorageIntf storage = sf.getStorage(urix.getHost());
                    String rpath = remotefile.replace(storage.getFullPath(), "");
                    
                    CreatePath cp = new CreatePath();
                    cp.setUri(uri);
                    cp.setPath(rpath);
                    getComm().sendActionToCaller(id, cp); 
                    
                    if ( getLogger().isDebugEnabled() ) {
	                    getLogger().debug("Creating directory [" + rpath + 
	                                      "] at [" + uri + "] on [" + id + "]");
                    }

                    String[] files = storage.getFiles(rpath);
                    if ( files != null ) { 
	                    for (int i = 0; i < files.length; i++) { 
	                        String auxfile = remotefile + "/" + files[i]; 
	                        pushFile(id, 
	                                 uri + "/" + files[i],
	                                 offset,
	                                 auxfile,
	                                 append,
	                                 compress);
	                    }
                    }
                    return;
                } else { 
	                fis = new FileInputStream(remotefile);  
	                sourcefile = remotefile;
                }
            }

            if ( getLogger().isDebugEnabled() ) {
                getLogger().debug("Pushing [" + sourcefile + "] to [" + id + 
                                  "] at [" + uri + "] append [" + append + "]");
            }

            long length = fis.getChannel().size();
            byte[] bytes = new byte[chunkSize];
           
            int skipped = 0;
            while ( skipped < offset )
                skipped += fis.skip((offset-skipped)); 
            
            long i = skipped;
            while ( i < length ) { 
                int read = fis.read(bytes);
                Writechunk write = new Writechunk();
                write.setUri(uri);
                write.setAppend(append || i > 0);
                write.setLength(read);
                write.bytes(bytes);
                
                getComm().sendActionToCaller(id, write); 
                i+=read;
            }
            
            if ( length == 0 ) { 
                Writechunk write = new Writechunk();
                write.setUri(uri);
                write.setAppend(append || i > 0);
                write.setLength(0);
                write.bytes(new byte[0]);
                getComm().sendActionToCaller(id, write); 
            }
		} catch (FileNotFoundException e) {
			throw new DTFException("Unable to read file [" + remotefile + "]",e);
		} catch (IOException e) {
			throw new DTFException("Unable to read file [" + remotefile + "]",e);
        } finally { 
		    if ( fis != null) 
		        try { 
		            fis.close();
		        } catch (IOException ignore) { }
		        
            if (tmpfile != null) {
                if ( !tmpfile.delete() ) { 
                    getLogger().warn("Unable to delete tmpfile [" + tmpfile + "]");
                }
            }
		}
    }
    
    public static void gzip(File source, File dest) throws DTFException { 
        try { 
            GZIPOutputStream gzipout = new GZIPOutputStream(new FileOutputStream(dest));
            FileInputStream input = new FileInputStream(source);
        
            byte[] buffer = new byte[64*1024];
            int read = 0;
            while (( read = input.read(buffer)) != -1) {
                gzipout.write(buffer,0,read);
            }
            
            gzipout.close();
            input.close();
        } catch (IOException e) { 
            throw new DTFException("Error compressing file.",e);
        }
    }
    
}
