package com.yahoo.dtf.actions.file;

import java.io.File;

import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.actions.file.Returnfile;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

/**
 * @dtf.tag getfile
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc this file can be used to retrieve a file from a component and
 *               place it in a storage identified on the runner side.
 *              
 * @dtf.tag.example
 * <getfile uri="storage://OUTPUT/build.copy1.xml" remotefile="build.xml"/>
 *
 * @dtf.tag.example
 * <getfile uri="storage://OUTPUT/hosts1" remotefile="/etc/hosts"/>
 */
public class Getfile extends Returnfile {

    /**
     * @dtf.attr remotefile
     * @dtf.attr.desc The fullpath to the remote file relative to where the 
     *                DTFA was started.
     */
    private String remotefile = null;
    
    private long offset = 0;
    
    /**
     * @dtf.attr append
     * @dtf.attr.desc Specifies if you would like the file to append to any 
     *                existing data in the uri specified.
     */
    private String append = "false";
    
    /**
     * @dtf.attr compress
     * @dtf.attr.desc Specifies if you would want the file to be compressed at 
     *                the destination. This also speeds up the copy because the
     *                compression is done on the agent side.
     */
    private String compress = "false";

    private String owner = null;
   
    public void execute() throws DTFException {
        if (!new File(getRemotefile()).exists())  {
            throw new DTFException("Remote file does not exist [" + 
                                   getRemotefile() + "]");
        }
        
        if ( getOwner() == null ) 
            setOwner(DTFNode.getOwner().getOwner());
        
        pushFile(getOwner(),
                 getUri(),
                 getOffset(),
                 getRemotefile(),
                 getAppend(),
                 getCompress());
    }
    
    public void setOffset(long offset) { this.offset = offset; } 
    public long getOffset() { return offset; } 

    public void setAppend(String append) { this.append = append; } 
    public boolean getAppend() throws ParseException { 
        return toBoolean("append", append);
    } 

    public void setCompress(String compress) { this.compress = compress; } 
    public boolean getCompress() throws ParseException {
        return toBoolean("compress",compress);
    } 

    public void setRemotefile(String remotefile) { this.remotefile = remotefile; } 
    public String getRemotefile() throws ParseException { 
        return replaceProperties(remotefile); 
    } 

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    
}  