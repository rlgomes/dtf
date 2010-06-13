package com.yahoo.dtf.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;

import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.logger.DTFLogger;
import com.yahoo.dtf.recorder.Event;

/**
 * This class only exists because the Vespa JDK for freebsd has some serious 
 * issues with executing some external scripts and it won't close the stdout and
 * stderr streams leaving your normal method of reading from the process till 
 * it is done writing method hanging... :( so we have to interrupt it once it 
 * has returned an exit code and just read whatever is left and hope thats good
 * enough.
 * 
 * @author rlgomes
 *
 */
public class Command {

    private final static int BUFFER_SIZE = 1024;

    public final static String ATTR_RC = "rc";
    public final static String ATTR_STDOUT = "stdout";
    public final static String ATTR_STDERR = "stderr";
    
    private Process _process = null;

    private boolean _sudo = false;

    public Command(boolean sudo) throws DTFException {
        if (sudo)
            Command.checkPermissions();

        _sudo = sudo;
    }

    public static void checkPermissions() throws DTFException {
        try {
            DTFLogger log = Action.getLogger();
            String command = "id -u -n";
            Process p = Runtime.getRuntime().exec(command);

            String out = StreamUtil.readToString(p.getInputStream());
            if (!out.startsWith("yahoo")) {
                log.warn("***************************************************");
                log.warn("This command should be run as yahoo user, do the ");
                log.warn("following to setup the yahoo user:");
                log.warn(" 1. yinst install admin/user-yahoo");
                log.warn(" 2. Add 'yahoo   ALL=(ALL) NOPASSWD:ALL' to /etc/sudoers");
                log.warn(" 3. yinst install admin/sudo-as-yahoo-USERNAME "
                        + "(replacing USERNAME with your username.");
                log.warn(" 4. sudo su - yahoo  and run the agent from there");
                log.warn("***************************************************");
                throw new DTFException("Run with user yahoo.");
            }

            if (p.waitFor() != 0) {
                String err = StreamUtil.readToString(p.getErrorStream());
                log.info("STDOUT: " + out);
                log.info("STDERR: " + err);
                throw new DTFException("Unable to execute command [" + command
                        + "] got return code " + p.exitValue());
            }
        } catch (IOException e) {
            throw new DTFException(
                    "Error trying to make sure user Yahoo in use.", e);
        } catch (InterruptedException e) {
            throw new DTFException(
                    "Error trying to make sure user Yahoo in use.", e);
        }
    }

    /**
     * Example code taken from ProcessExecutor.java Original Author: Jon S
     * Bratseth <bratseth@yahoo-inc> Thanks Jon! :)
     */
    public Event execute(String eventName,
                         String command) throws DTFException {
        return execute(eventName,command,null,null,null);
    }
        
    public Event execute(String eventName,
                         String command,
                         String[] envarray,
                         String directory,
                         InputStream is) throws DTFException {
        Event event = new Event(eventName);
        Process process = null;

        try {
            File dir = null;
           
            if ( directory != null ) 
                dir = new File(directory);
            
            event.start();
            if (_sudo)
                process = Runtime.getRuntime().exec("sudo " + command,envarray,dir);
            else
                process = Runtime.getRuntime().exec(command, envarray, dir);
            event.stop();
        } catch (IOException e) {
            throw new DTFException("Unable to execute [" + command + "]", e);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();

        OutputStream os = null;
        if ( is != null ) 
            os = process.getOutputStream();

        ConsumerThread consumer = new ConsumerThread(process.getInputStream(),
                                                     process.getErrorStream(),
                                                     out,
                                                     err);
        consumer.start();
        
        // is there input to push
        if ( is != null ) { 
            byte[] buffer = new byte[BUFFER_SIZE];
            int read = 0;
            try {
                while ( (read = is.read(buffer)) != -1 ) {
                    os.write(buffer,0,read);
                }
                os.flush();
                os.close();
            } catch (IOException e) {
                throw new DTFException("Error writing to STDIN.",e);
            }
        }

        int returnCode = 0;
        try {
            returnCode = process.waitFor();
            consumer.done();
        } catch (InterruptedException e) {
            throw new DTFException("Execution interrupted.",e);
        }

        event.addAttribute(ATTR_RC, returnCode);
        event.addAttribute(ATTR_STDOUT, out.toString());
        event.addAttribute(ATTR_STDERR, err.toString());

        consumer.checkForException();

        return event;
    }
   
    /**
     * XXX: this method needs to merge with the other one above at some point
     *      in the future.
     *      
     * @param eventName
     * @param cmdarray
     * @param is
     * @return
     * @throws DTFException
     */
    public Event execute(String eventName,
                         String[] cmdarray,
                         String[] envarray,
                         String directory,
                         InputStream is) throws DTFException {
        Event event = new Event(eventName);
        Process process = null;
        File dir = null;
           
        if ( directory != null )
            dir = new File(directory);

        try {
            event.start();
            process = Runtime.getRuntime().exec(cmdarray, envarray, dir);
            event.stop();
        } catch (IOException e) {
            throw new DTFException("Unable to execute [" + cmdarray[0]
                    + "]", e);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();

        ConsumerThread consumer = new ConsumerThread(process.getInputStream(),
                                                     process.getErrorStream(),
                                                     out,
                                                     err);
        consumer.start();
       
        // is there input to push
        if ( is != null ) { 
            byte[] buffer = new byte[BUFFER_SIZE];
            int read = 0;
            try {
                OutputStream os = process.getOutputStream();
                while ( (read = is.read(buffer)) != -1 )
                    os.write(buffer,0,read);
            } catch (IOException e) {
                throw new DTFException("Error writing to STDIN.",e);
            }
        }
        
        int returnCode = 0;
        try {
            returnCode = process.waitFor();
            consumer.done();
        } catch (InterruptedException e) {
            throw new DTFException("Execution interrupted.",e);
        }

        event.addAttribute(ATTR_RC, returnCode);
        event.addAttribute(ATTR_STDOUT, out.toString());
        event.addAttribute(ATTR_STDERR, err.toString());

        consumer.checkForException();

        return event;
    }

    private static class ConsumerThread extends Thread {
        private InputStream out, err;

        private OutputStream output = null;
        private OutputStream error = null;

        private IOException _exception = null;

        public ConsumerThread(InputStream out, InputStream err,
                OutputStream output, OutputStream error) {
            this.out = out;
            this.err = err;
            this.output = output;
            this.error = error;
        }

        @Override
        public void run() {
            try {
                try {
                    // can't use the isInterrupted because the flag for 
                    // interruption has to set before the interruption is made
                    // an dif we get out of this code before hitting the 
                    // interruption then we could hit it outside of this loop 
                    // and generate an unnecessary exception.
                    while ( true ) {
                        readAvailable(out, output);
                        readAvailable(err, error);
                        Thread.sleep(100);
                    }
                } catch (InterruptedException ignore) { 
                } catch (InterruptedIOException ignore) { }

                readAvailable(out, output);
                readAvailable(err, error);
            } catch (IOException e) {
                _exception = e;
            }
        }
        
        public void done() throws InterruptedException { 
            interrupt();
            join();
        }

        public void checkForException() throws DTFException {
            if (_exception != null)
                throw new DTFException("Unable to read from output.",
                        _exception);
        }

        private void readAvailable(InputStream stream, OutputStream output)
                throws IOException {
            while ( stream.available() != 0 )
                output.write(stream.read());
        }
    }

    public Event execute(String eventName, String[] command)
            throws DTFException {
        Event event = new Event(eventName);
        Process process = null;

        try {
            event.start();
            if (_sudo) {
                String[] cmd = new String[command.length + 1];
                cmd[0] = "sudo";
                for (int i = 1; i < cmd.length; i++) {
                    cmd[i] = command[i - 1];
                }
                process = Runtime.getRuntime().exec(cmd);
            } else {
                process = Runtime.getRuntime().exec(command);
            }
            event.stop();
        } catch (IOException e) {
            StringBuffer cmd = new StringBuffer();
            
            for (int i = 0; i < command.length; i++) 
                cmd.append(command[i] + " ");
            
            throw new DTFException("Unable to execute [" + cmd + "]", e);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();

        ConsumerThread consumer = new ConsumerThread(process.getInputStream(),
                                                     process.getErrorStream(),
                                                     out,
                                                     err);
        consumer.start();
        int returnCode = 0;
        try {
            returnCode = process.waitFor();
            consumer.done();
        } catch (InterruptedException e) {
            throw new DTFException("Execution interrupted.",e);
        }

        event.addAttribute(ATTR_RC, returnCode);
        event.addAttribute(ATTR_STDOUT, out.toString());
        event.addAttribute(ATTR_STDERR, err.toString());
        consumer.checkForException();

        return event;
    }

    public void close() {
        if (_process != null) {
            try {
                _process.getOutputStream().close();
            } catch (IOException ignore) { }

            try {
                _process.getErrorStream().close();
            } catch (IOException ignore) { }

            try {
                _process.getInputStream().close();
            } catch (IOException ignore) { }
        }
    }

}