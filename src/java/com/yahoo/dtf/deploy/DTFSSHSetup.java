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

/**
 * @dtf.feature Command Line Usage 
 * @dtf.feature.group Deployment
 * @dtf.feature.desc 
 * <p>
 * Deploying DTF to several machines and setting it up to run can be done 
 * manually and isn't that hard all you have to do is copy the build/dtf/dist 
 * directory to the machine you want to start any of the DTF components and 
 * then you can issue the appropriate command line to start that component up. 
 * You will need a Sun JDK 1.5+ to execute the components and then you can start
 * them up with the following example command lines:
 * <p>
 * 
 * Start up the DTFC (Controller):
 * <pre>./ant.sh run_dtfc</pre>
 * 
 * Start up a DTFA and connect it to the controller on the specified address:
 * <pre>./ant.sh run_dtfa -Ddtf.connect.addr=host_machine_for_dtfc</pre>
 * 
 * Start up the test tests/xxx.xml and run it against the controller on the 
 * specified address:
 * <pre>./ant.sh run_dtfx -Ddtf.connect.addr=host_machine_for_dtfc -Ddtf.xml.filename=tests/ut/echo.xml</pre>
 * 
 * <p>
 * That is the manual way and is easy enough for small setups with just 2-3 
 * agents but when we get into larger setups that run multiple client side 
 * agents and a few other server side agents and we have to use SSH tunneling 
 * in order to connect some of those agents back to the controller machine we 
 * find that the setup time is long and prone to errors. In order to make this 
 * easier, there is a way of starting up your DTF setup by just passing a 
 * configuration file to a few targets in your build and requesting to check 
 * the status, stop and start your setup, etc. This takes care of all of the 
 * configuration steps for you. Lets start by showing what the format of the 
 * actual configuration file looks like: 
 * </p>
 * 
 * {@dtf.xml
 *  <setup xmlns="http://dtf.org/v1">
 *      <dtfc host="${host}" user="${user}">
 *          <dtfa host="${host}" user="${user}">
 *              <property name="node.id" value="1"/>
 *          </dtfa>
 *          <dtfa host="${host}" user="${user}">
 *              <property name="node.id" value="2"/>
 *          </dtfa>
 *          <dtfa host="${host}" user="${user}">
 *              <property name="node.id" value="3"/>
 *          </dtfa>
 *          <dtfx host="${host}"
 *                user="${user}"
 *                test="tests/ut/ut.xml"
 *                logs="tests/ut/output/ut_results.xml">
 *          </dtfx>
 *      </dtfc>
 *  </setup>}
 *  
 * <p>
 * Many of you will notice that it has a declaration just like the normal DTF 
 * test scripts but just a few new tags that are used to define your setup. The
 * tags themselves just define the important properties that are necessary for 
 * the deployment feature to know where to setup each component. So you can see 
 * that you need to specify at least the host and user  name to be used to SSH 
 * into each machine, that will also be the user that the component would be 
 * executed as. Then you can specify any properties directly in this file that 
 * would be loaded into the component at runtime. 
 * </p>
 *  
 * <p>
 * All of the deployment targets are available under the build directory 
 * (dtf/build/dtf/dist) and all you need to do to see the available targets is 
 * issue the command ./ant.sh -p to see each target and a description of what 
 * it does. Now we'll go through the most used targets and how they can be used
 * to setup and monitor your DTF configuration.
 * </p>
 *  
 * <h2>Setup Configuration</h2>
 * <pre>./ant.sh setup-dtf -Ddeploy.config=path/to/your/config.xml</pre>
 * 
 * <p>
 * This will setup all of the machines identified in the deploy.config file 
 * (default is config.xml) so that the DTFC and the machine issuing this 
 * command can SSH into any of those other machines. This allows the DTFC to be
 * able to startup the necessary components on the other machines and allows 
 * the issuing machine to be able to gather logs.  The SSH keys generated and 
 * used from now on will be under you $HOME/.dtf directory on *nix machines and
 * under %HOMEPATH%/dtf directory for Windows. When executing this command be 
 * sure to watch for prompts like so:
 * </p>
 *  
 * <pre>
 *  setup-dtf: 
 *      [java] INFO  10/11/2009 11:06:24 DeployUI        - rlgomes@dayspentlist.corp.yahoo.com:
 * </pre>
 *  
 * <p> 
 * This is basically a prompt from SSH requesting you type your password and 
 * currently the password will be visible to the person typing. So be careful 
 * not to do this step when others are watching your screen. 
 * </p>
 *  
 * <h2>Start Configuration</h2>
 * <pre>./ant.sh deploy-start -Ddeploy.config=path/to/your/config.xml</pre>
 *  
 * <p>
 * The previous target starts up your DTFC first and then make sure that its up
 * and running, then it will start up each of the DTFA specified and lastly 
 * start up a DTFX if one is defined. Once this has completed you should be 
 * able to check the status of your with the follow target.
 * </p>
 *  
 * <h2>Check Status of Configuration</h2>
 * <pre>./ant.sh deploy-status -Ddeploy.config=path/to/your/config.xml</pre>
 * 
 * <p>
 * This will output the current state of your configuration when compared with 
 * the configuration file you've specified. It will identify DTFA's that are no
 * longer running as well as the state of your runner's (DTFX) last test run. 
 * Here's an example of running this command with the tests/setup/ut_config.xml
 * configuration file on your localhost:
 * </p>
 *  
 * <pre>./ant.sh -Ddeploy.config=tests/setup/ut_config.xml -Dhost=localhost -Duser=rlgomes deploy-status
 *  
 * Buildfile: build.xml
 *  
 * init: 
 *     [echo] Creating log dir ./logs/10-11-2009.14.19.05
 *     [mkdir] Created dir: /home/rlgomes/workspace/dtf/build/dtf/dist/logs/10-11-2009.14.19.05
 *      
 * deploy-init:
 * 
 * deploy-status: 
 *     [java] INFO  10/11/2009 14:19:09 DeployDTF       - Status of dtfc on localhost
 *     [java] INFO  10/11/2009 14:19:09 DeployDTF       - DTFC on localhost
 *     [java] INFO  10/11/2009 14:19:09 DeployDTF       - DTFA [dtfa-0] on localhost is locked
 *     [java] INFO  10/11/2009 14:19:09 DeployDTF       - DTFA [dtfa-1] on localhost is locked
 *     [java] INFO  10/11/2009 14:19:09 DeployDTF       - DTFA [dtfa-2] on localhost is locked
 *     [java] INFO  10/11/2009 14:19:10 DeployDTF       - DTFX on localhost running tests/ut/state_management.xml:41,16
 *      
 * BUILD SUCCESSFUL 
 *     Total time: 6 seconds 
 * </pre>
 *  
 * <p> 
 * You can easily see the state of each of the components connected and that 
 * your DTFX is currently on the test tests/ut/state_management.xml at line 41.
 * You can also watch the output easily with the target deploy-watch. The 
 * deploy-watch target will tail the output file on the DTFX and you can watch 
 * it on your screen and Ctrl-C that whenever you'd like. Once the status 
 * reports that the DTFX is completed you can easily gather your logs.
 * </p>
 *  
 * <h2>Saving your Logs</h2>
 * <pre>./ant.sh -Ddeploy.config=tests/setup/config.xml deploy-savelogs</pre>
 *  
 * <p>
 * This target simply copies back all of the component output files to the 
 * dtf_logs directory and will copy any other of the log files from the runner 
 * that were specified with the attribute logs (comma separated list). Your old
 * dtf_logs will be backed up to the dtf_logs_bk in case you required them for 
 * some reason.
 * </p>
 * 
 * <h2>Waiting for a deployment to finish running tests</h2>
 * <p>
 * You can easily wait on an existing setup that has a DTFX running in it by 
 * using the "deploy-wait" target to wait till the DTFX has completed and reports
 * the exact status of that completion.
 * </p>
 * 
 * <h2>Other Targets</h2>
 * <p>
 * There are quite a few other deployment targets and you can easily check on 
 * the available targets by issuing an "ant -p", like so:
 * </p>
 * <pre>
 * > ant -p
 * Buildfile: build.xml
 * 
 * Main targets:
 *  deploy-savelogs  collect all of the logs for the DTF setup identifed by the ${deploy.config} property
 *  deploy-start     start the DTF setup defined in the ${deploy.config} file
 *  deploy-status    check the status of the DTF setup identified in the config file
 *  deploy-stop      This target will stop the specified DTF config and collect all of the logs
 *  deploy-wait      wait for the DTF setup defined in the ${deploy.config} file to complete running the test
 *  deploy-watch     sets up all machines identified in the ${deploy.config} file so they can be used by the other deploy-xxx targets
 *  run_dtfa         startup the DTFA component
 *  run_dtfc         startup the DTFC component
 *  run_dtfx         startup the DTFX component
 *  run_dvt          run DTF deployment verification tests (dvt).
 *  run_ut           run DTF unit tests.
 *  setup-script     execute a script on all machines identified in the ${deploy.config} file
 *  setup-ssh        sets up all machines identified in the ${deploy.config} file so that the deploy-xxx tags can SSH to those machines without any issues
 * </pre>
 * 
 * <h2>Host Requirements</h2>
 * 
 * <p>
 * The host machines that you are going to deploy to require an ssh server 
 * running on them and they should have Java 1.5+ installed. Other than that
 * there is a requirement at the OS level, that the following commands are 
 * available:
 * </p>
 * 
 *  <table border="1">
 *      <tr>
 *          <th>Command</th> 
 *          <th>Usage</th> 
 *      </tr>
 *      <tr>
 *          <td>nohup</td> 
 *          <td>command used to background the running of the components</td> 
 *      </tr>
 *      <tr>
 *          <td>cd</td> 
 *          <td>command used to changed directories on the remote host machines.</td> 
 *      </tr>
 *      <tr>
 *          <td>cat</td> 
 *          <td>command used copy files back and forth, the reason we use this
 *              instead of a built in ftp command of scp is because some older
 *              ssh versions don't support the file copying mechanism and by 
 *              using this command we work around that problem.</td> 
 *      </tr>
 *      <tr>
 *          <td>chmod</td> 
 *          <td>command used to set the correct execution attributes for 
 *              the necessary executable files.</td> 
 *      </tr>
 *      <tr>
 *          <td>mkdir</td> 
 *          <td>used to create directories remotely and locally for different 
 *              operations.</td>
 *      </tr>
 *  </table>
 *  
 * @author rlgomes
 */
public class DTFSSHSetup {
    
    public final static String BG_CMD       = "nohup";
    public final static String CD_CMD       = "cd";
    public final static String CAT_CMD      = "cat";
    public final static String CHMOD_CMD    = "chmod";
    public final static String MKDIR_CMD    = "mkdir";

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
        Session session = SSHUtil.connectToHost(host, user, null, null);
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
        
        Session session = SSHUtil.connectToHost(host,
                                                user,
                                                node.getRsakey(),
                                                node.getPassphrase());
        
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
                   
                    logger.info("Done setting up passwordless ssh on [" + 
                                setupkey + "]");
                }
                
                logger.info("Setting up DTF at [" + setupkey + "] as a " + 
                            component);
                if ( component.equals("dtfc") && node.getRsakey() != null ) { 
	                // if its the controller then it should also get the private
	                // key made accessible so it can easily then deploy 
	                // components on the other machines using this key to ssh 
	                // into those other hosts
                    SSHUtil.execute(session, MKDIR_CMD + " .dtf", false);
                    
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
	                    ByteArrayInputStream bais = 
	                               new ByteArrayInputStream(baos.toByteArray());
	                    csftp.put(bais, ".dtf/id_rsa", ChannelSftp.OVERWRITE);
	                    bais.close();
                    } finally {
                        csftp.disconnect();
                    }
                    
                    SSHUtil.execute(session,
                                    CHMOD_CMD + " 600 .dtf/id_rsa",
                                    false);
                    
                    logger.info("Stop any previously running dtfc");
                    DeployDTF.waitForComponentToStop("dtfc", host, 40000);
                    ThreadUtil.pause(2000);
                }
           
                logger.info("Delete old dtf build at [" + path + "]");
                SSHUtil.execute(session, "rm -fr " + path, false);
                
                logger.info("Recreate DTF directory");
                String cmd = MKDIR_CMD + " -p " + path;
                cmd = DeployDTF.wrap(wrapcmd,cmd);
                SSHUtil.execute(session, cmd, logger.isDebugEnabled());
                
                logger.info("Copying dtf.jar to " + setupkey);
                long start = System.currentTimeMillis();
                File jar = new File("dtf.jar");
                FileInputStream fis = new FileInputStream(jar);
                try { 
                    cmd = CAT_CMD + " > " + path + "/dtf.jar";
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
                cmd = CD_CMD + " " + path + " ; jar xvf dtf.jar";
                cmd = DeployDTF.wrap(wrapcmd,cmd);
                
                rc = SSHUtil.execute(session, cmd, logger.isDebugEnabled());
                
                if ( rc != 0 )
                    throw new DTFException("Unable to uncompress dtf.jar got rc [" + rc + "]");
        
                logger.info("Fixing file permissions...");
                cmd = CHMOD_CMD + " +x " + path + "/*.sh " + path + "/apache*/bin/*";
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
