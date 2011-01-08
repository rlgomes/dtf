package com.yahoo.dtf.deploy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;

public class Deploy {

    public static void main(String[] args) {
        JSch jsch = new JSch();

        String user = args[0];
        final String pass = args[1];
        String[] hosts = args[2].split(",");
        String home = args[3];
        String tarball = args[4];
        
        UserInfo ui = new UserInfo() {
            public void showMessage(String arg0) {
                System.out.println(arg0);
            }
            public boolean promptYesNo(String arg0) { 
                return true; 
            }
            public boolean promptPassword(String arg0) { 
                if (pass.equals("NOTSET")) { 
	                System.out.println(arg0);
	                System.out.println("Specify user with -Ddeploy.user=UUU.");
	                System.out.println("Specify password with -Ddeploy.pass=PPP.");
	                System.out.println("Alternatively concatenate your " + 
	                                   "~/.ssh/id_rsa.pub file to the " + 
	                                   "~/.ssh/authorized_keys file on " + 
	                                   "the remote host.");
	                return false;
                } 
                
                return true;
            }
            public boolean promptPassphrase(String arg0) { 
                return true;
            }
            public String getPassword() { return pass; }
            public String getPassphrase() { return "nothing"; }
        };

        try { 
            if (new File(home + "/.ssh/known_hosts").exists())
                jsch.setKnownHosts(home + "/.ssh/known_hosts");
            
            if (new File(home + "/.ssh/id_rsa").exists())
                jsch.addIdentity(home + "/.ssh/id_rsa");
        } catch (JSchException e) { 
            e.printStackTrace();
            System.exit(-1);
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss-dd-MM-yyyy");
	    String timestamp = sdf.format(new Date(System.currentTimeMillis()));
        for (int i = 0; i < hosts.length; i++) {
	        try {
	            
                Session session = jsch.getSession(user, hosts[i]);
                session.setUserInfo(ui);
                session.connect();

                System.out.println("");
                System.out.println("Deploying to [" + hosts[i] + "]");
                System.out.println("Backup current dtf to dtf-" + timestamp);
                Channel channel = session.openChannel("exec");
                ((ChannelExec) channel).setCommand("mv dtf dtf-" + timestamp);
                channel.connect();
                channel.disconnect();

                channel = session.openChannel("exec");
                ((ChannelExec) channel).setCommand("mkdir dtf");

                channel.connect();
                channel.disconnect();

                System.out.println("Copying tarball");
                ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
                sftp.connect();

                FileInputStream fis = new FileInputStream(tarball);
                OutputStream os = sftp.put("dtf.tar.gz");
                byte[] buffer = new byte[1024];
                int read = 0;
                while ((read = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
                os.close();
                fis.close();
                sftp.disconnect();

                System.out.println("Extracting tarball");
                channel = session.openChannel("exec");
                ((ChannelExec) channel).setCommand("tar xvfz dtf.tar.gz -C dtf");
                channel.connect();
                consumeStream(channel.getExtInputStream());
                channel.disconnect();

                System.out.println("Clean up tarball and fix file permissions");
                channel = session.openChannel("exec");
                String command = "rm dtf.tar.gz";
                ((ChannelExec) channel).setCommand(command);
                channel.connect();
                channel.disconnect();

                command = "chmod +x dtf/*.sh dtf/*.php dtf/apache-ant-1.6.0/bin/ant*";
                channel = session.openChannel("exec");
                ((ChannelExec) channel).setCommand(command);
                channel.connect();
                channel.disconnect();
               
                session.disconnect();
	        } catch (JSchException e) {
	            System.out.println("ERROR: " + e.getMessage() + " on " + hosts[i]);
	            System.exit(-1);
	        } catch (FileNotFoundException e) {
	            System.out.println("ERROR: " + e.getMessage() + " on " + hosts[i]);
	            System.exit(-1);
	        } catch (SftpException e) {
	            System.out.println("ERROR: " + e.getMessage() + " on " + hosts[i]);
	            System.exit(-1);
	        } catch (IOException e) {
	            System.out.println("ERROR: " + e.getMessage() + " on " + hosts[i]);
	            System.exit(-1);
	        }
        }
    }
    
    private static void consumeStream(InputStream is) throws IOException { 
        byte[] buffer = new byte[1024];
        while (is.read(buffer) != -1);
    }
}
