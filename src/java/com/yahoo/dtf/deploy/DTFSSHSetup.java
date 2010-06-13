package com.yahoo.dtf.deploy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.yahoo.dtf.actions.protocol.deploy.DTFNode;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.logger.DTFLogger;
import com.yahoo.dtf.util.ThreadUtil;

public class DTFSSHSetup {

    private static ArrayList<String> dtfsetup = new ArrayList<String>();
    
    private static String getDTFSSHLocation() { 
        // on windows we'll place the dtf ssh keys in the user home under the
        // directory dtf
        String home = System.getProperty("user.home");
        
        if ( System.getProperty("os.name").startsWith("Windows") )
            return home + "/dtf";
        
        return home + "/.dtf";
    }
    
    public static Session setupSSH(String host,
                                     String user,
                                     DTFLogger logger) 
           throws JSchException, FileNotFoundException, IOException, SftpException, DTFException { 
        Session session = SSHUtil.connectToHost(host, user, null);
        DeployUI ui = (DeployUI) session.getUserInfo();
        
        String dtfssh = getDTFSSHLocation();
        File fssh = new File(dtfssh);
        if ( !fssh.exists() && !fssh.mkdirs() ) 
            throw new DTFException("Unable to create [" + fssh + "]");
        
        String id_rsa_pub = dtfssh + "/id_rsa.pub";
        String id_rsa = dtfssh + "/id_rsa";
    
        try { 
            if ( ui.setup_passwordless_ssh ) { 
                logger.info("Setting up [" + user + "@" + host + 
                             "] for passwordless ssh...");
      
                File pubkey = new File(id_rsa_pub);
                File prikey = new File(id_rsa);
                    
                if ( !pubkey.exists() || !prikey.exists() ) {
                    logger.warn("*** Generating new DTF RSA keys ***");
                    JSch jsch = new JSch();
                    KeyPair kpair = KeyPair.genKeyPair(jsch, KeyPair.RSA);
                     
                    kpair.setPassphrase("dtf");
                    kpair.writePrivateKey(prikey.getAbsolutePath());
                    kpair.writePublicKey(pubkey.getAbsolutePath(),
                                         "dtf deployment private rsa key");
                    kpair.dispose();
                }
                
                Channel sChannel = session.openChannel("sftp");
                sChannel.connect();
                ChannelSftp csftp = (ChannelSftp)sChannel;
        
                logger.info("Appending public SSH RSA key to ~/.ssh/authorized_keys");
                FileInputStream fis = new FileInputStream(id_rsa_pub);
                try { 
                    csftp.put(fis, ".ssh/authorized_keys", ChannelSftp.APPEND);
                } finally { 
                    fis.close();
                    csftp.disconnect();
                }
            }
        } finally { 
        }
        return session;
    }
    
    public static Session setupHost(DTFNode node,
                                      String component,
                                      DTFLogger logger)
                   throws JSchException, SftpException, IOException, DTFException { 
        String host = node.getHost();
        String user = node.getUser();
        String wrapcmd = node.getWrapcmd();
        
        Session session = SSHUtil.connectToHost(host, user, node.getRsakey());
        DeployUI ui = (DeployUI) session.getUserInfo();
        
        String dtfssh = getDTFSSHLocation();
        File fssh = new File(dtfssh);
        if ( !fssh.exists() && fssh.mkdir() ) 
            throw new DTFException("Unable to create [" + fssh + "]");
        
        String id_rsa_pub = dtfssh + "/id_rsa.pub";
        String id_rsa = dtfssh + "/id_rsa";
        
        int rc = 0;
   
        String path = node.getPath();
        if ( path == null ) {
            path = SSHUtil.getHomeDir(session, node) + "/dtf";
        }
        
        try { 
            String setupkey = user + "@" + host + ":" + path;
            
            File pubkey = new File(id_rsa_pub);
            File prikey = new File(id_rsa);
            
            if ( !dtfsetup.contains(setupkey) ) {
                if ( ui.setup_passwordless_ssh ) { 
			        Channel sChannel = session.openChannel("sftp");
			        sChannel.connect();
			        ChannelSftp csftp = (ChannelSftp) sChannel;
			        
                    logger.info("Setting up [" + user + "@" + host + 
                                 "] for passwordless ssh...");
      
                    
                    if ( !pubkey.exists() || !prikey.exists() ) {
                        logger.warn("*** Generating new DTF RSA keys ***");
                        JSch jsch = new JSch();
                        KeyPair kpair = KeyPair.genKeyPair(jsch, KeyPair.RSA);
                     
                        kpair.setPassphrase("dtf");
                        kpair.writePrivateKey(prikey.getAbsolutePath());
                        kpair.writePublicKey(pubkey.getAbsolutePath(),
                                             "dtf deployment private rsa key");
                        kpair.dispose();
                    }
        
                    logger.info("Appending public SSH RSA key to ~/.ssh/authorized_keys");
                    FileInputStream fis = new FileInputStream(id_rsa_pub);
                    try { 
                        csftp.put(fis, ".ssh/authorized_keys", ChannelSftp.APPEND);
                    } finally { 
                        fis.close();
                        csftp.disconnect();
                    }
                   
                    logger.info("Done setting up passwordless on [" + setupkey + "]");
                }
                
                logger.info("Setting up DTF at [" + setupkey + "] as a " + component);
                if ( component.equals("dtfc") ) { 
	                // if its the controller then it should also get the private
	                // key made accessible so it can easily then deploy 
	                // components on the other machines using this key to ssh 
	                // into those other hosts
                    SSHUtil.execute(session, "mkdir .dtf", false);
                    
                    logger.info("Pushing DTF RSA key to controller");
                    FileInputStream fis = new FileInputStream(prikey);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 
                    /*
                     * Safe way to not overwrite the same local file and result
                     * in a 0 byte file
                     */
                    try { 
	                    byte[] buffer = new byte[4*1024];
	                    int read = 0;
	                    while ( (read = fis.read(buffer)) != -1 ) { 
	                        baos.write(buffer,0,read);
	                    }
	                    baos.close();
                    } finally { 
                        fis.close();
                    }
                    
                    Channel sChannel = session.openChannel("sftp");
                    sChannel.connect();
                    ChannelSftp csftp = (ChannelSftp) sChannel;
                   
                    try { 
	                    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
	                    csftp.put(bais, ".dtf/id_rsa", ChannelSftp.OVERWRITE);
	                    bais.close();
                    } finally {
                        csftp.disconnect();
                    }
                    
                    SSHUtil.execute(session, "chmod 600 .dtf/id_rsa", false);
                    
                    logger.info("Stop any previously running dtfc");
                    DeployDTF.waitForComponentToStop("dtfc", host, 40000);
                    ThreadUtil.pause(2000);
                }
           
//                logger.info("Delete old dtf build");
//                SSHUtil.execute(session, "rm -fr " + path, false);
                
                logger.info("Recreate DTF directory");
                String cmd = "mkdir -p " + path;
                cmd = DeployDTF.wrap(wrapcmd,cmd);
                SSHUtil.execute(session, cmd, logger.isDebugEnabled());
                
                logger.info("Copying dtf.jar to " + setupkey);
                long start = System.currentTimeMillis();
                File jar = new File("dtf.jar");
                FileInputStream fis = new FileInputStream(jar);
                try { 
                    cmd = "cat > " + path + "/dtf.jar";
                    cmd = DeployDTF.wrap(wrapcmd, cmd);
                    
                    rc = SSHUtil.execute(session, 
                                         cmd,
                                         fis,
                                         new ByteArrayOutputStream(),
                                         new ByteArrayOutputStream());
                } finally { 
                    fis.close();
                }
                long stop = System.currentTimeMillis();
                
                if ( logger.isDebugEnabled() )
                    logger.debug("Time to copy dtf.jar " + (stop-start) + "ms");
                
                logger.info("Uncompressing the dtf.jar");
                cmd = "cd " + path + " ; jar xvf dtf.jar";
                cmd = DeployDTF.wrap(wrapcmd,cmd);
                
                rc = SSHUtil.execute(session, cmd, logger.isDebugEnabled());
                
                if ( rc != 0 )
                    throw new DTFException("Unable to uncompress dtf.jar got rc [" + rc + "]");
        
                logger.info("Fixing file permissions...");
                cmd = "chmod +x " + path + "/*.sh " + path + "/apache*/bin/*";
                cmd = DeployDTF.wrap(wrapcmd,cmd);
                
                rc = SSHUtil.execute(session,cmd, logger.isDebugEnabled());
                
                if ( rc != 0 )
                    throw new DTFException("Unable to execute [" +cmd + "] got rc : [" + rc + "]");
    
                dtfsetup.add(setupkey);
            } else { 
                logger.info("Latest DTF build already at " + setupkey);
            }
        } finally { 
            // nothing
        }
        
        return session;
    }
}
