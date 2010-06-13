package com.yahoo.dtf.actions.file;

import java.io.IOException;
import java.io.OutputStream;

import com.yahoo.dtf.actions.file.Returnfile;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.storage.StorageFactory;
import com.yahoo.dtf.util.ByteArrayUtil;

public class Writechunk extends Returnfile {

    private boolean append = false;
    private byte[] bytes = null;
    private int length = 0;
    
    public void execute() throws DTFException {
        StorageFactory sf = getStorageFactory();
        OutputStream os = sf.getOutputStream(parseURI(getUri()), getAppend());
       
        if ( getLogger().isDebugEnabled() ) 
            getLogger().debug("Writing file [" + getUri() + "]");

        registerContext("noreturnhooks", true);
        try {
            if (bytes != null)
                os.write(bytes, 0, getLength());
        } catch (IOException e) {
            throw new DTFException("Unable to write to file.",e);
        } finally { 
            try {
                if (os != null) 
                    os.close();
            } catch (IOException e) {
                throw new DTFException("Error closing file.",e);
            }
        }
    }
   
    public boolean getAppend() { return append; } 
    public void setAppend(boolean append) { this.append = append; } 
    
    public String getBytes() {
        return ByteArrayUtil.byteArrayToHexString(bytes,length);
    }
    
    public void setBytes(String string) { 
        this.bytes = ByteArrayUtil.hexToByteArray(string);
    }
    
    public void bytes(byte[] bytes) { this.bytes = bytes.clone(); } 
    
    public void setLength(int length) { this.length = length; } 
    public int getLength() { return length; } 
}
