package com.yahoo.dtf.logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.yahoo.dtf.logger.DTFLogger;
import com.yahoo.dtf.DTFProperties;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.config.Config;

public class DTFLogger {
    
    private static Config _config = null;

    private Logger _logger = null;

    protected DTFLogger(Logger logger) {
        _logger = logger;
    }

    public static void setConfig(Config config) {
        _config = config;
    }
   
    /*
     * We don't need multiple instances of the DTFLogger since it already makes
     * sure to log the name of the calling class.
     */
    private static DTFLogger _instance = null;

    public synchronized static DTFLogger getLogger(String name) {
        if ( _instance == null ) { 
	        Logger logger = Logger.getLogger(name);
	
	        if (_config != null) {
	            PropertyConfigurator.configure(_config.getProperties());
	        } else {
	            /*
	             * Creating default logger.
	             */
	            Properties properties = new Properties();
	            FileInputStream fis = null;
	            try {
	                String home = System.getProperty(DTFProperties.DTF_HOME);
	                fis = new FileInputStream(home + File.separatorChar
	                        + "dtf.properties");
	                properties.load(fis);
	            } catch (FileNotFoundException e) {
	                throw new RuntimeException("Unable to find properties file.", e);
	            } catch (IOException e) {
	                throw new RuntimeException("Unable to find properties file.", e);
	            } finally {
	                if (fis != null) {
	                    try {
	                        fis.close();
	                    } catch (IOException e) {
	                        throw new RuntimeException(
	                                "Unable to close properties file.", e);
	                    }
	                }
	            }
	            PropertyConfigurator.configure(properties);
	        }
	        
	        _instance = new DTFLogger(logger);
        }

        return _instance;
    }

    private static String pad(String str, int padlen, String pad) {
        StringBuffer padding = new StringBuffer("");
        int len = Math.abs(padlen) - str.toString().length();
        if (len < 1)
            return str.toString();
        
        for (int i = 0; i < len; ++i)
            padding.append(pad);

        return (padlen < 0 ? padding + str : str + padding);
    }

    private String getCallingClassname() {
        String classname = new Throwable().getStackTrace()[2].getClassName();
        classname = classname.substring(classname.lastIndexOf('.') + 1);
        return pad(classname, 15, " ");
    }

    public void info(Object message) {
        _logger.info(getCallingClassname() + " - " + message);
    }

    public void info(Object message, Throwable t) {
        _logger.info(message, t);
    }

    public void error(Object message) {
        _logger.error(getCallingClassname() + " - " + message);
    }

    public void error(Object message, Throwable t) {
        _logger.error(message, t);
    }

    public void warn(Object message) {
        _logger.warn(getCallingClassname() + " - " + message);
    }

    public void warn(Object message, Throwable t) {
        _logger.warn(message, t);
    }

    public void debug(Object message) {
        _logger.debug(getCallingClassname() + " - " + message);
    }

    public void debug(Object message, Throwable t) {
        _logger.debug(message, t);
    }

    public boolean isDebugEnabled() {
        return _logger.isDebugEnabled();
    }

    public void addAppender(Appender appender) {
        _logger.addAppender(appender);
        _logger.setAdditivity(true);
    }

    public void removeAppender(Appender appender) {
        _logger.removeAppender(appender);
    }

    public static DTFLogger getLogger(Class aClass) {
        return getLogger(Action.getClassName(aClass));
    }
    
    public final static int INFO  = 0;
    public final static int ERROR = 1;
    public final static int WARN  = 2;
    public final static int DEBUG = 3;

    private static void setLoggingLevel(Logger logger, int level) { 
        switch (level) { 
            case INFO:
            logger.setLevel(Level.INFO);
            break;
            
            case ERROR:
            logger.setLevel(Level.ERROR);
            break;

            case DEBUG:
            logger.setLevel(Level.DEBUG);
            break;

            case WARN:
            logger.setLevel(Level.WARN);
            break;
        }
    }
    
    public static void setLoggingLevel(String cname, int level) { 
        setLoggingLevel(Logger.getLogger(cname), level);
    }
    
    public static void setLoggingLevel(int level) { 
        setLoggingLevel(Logger.getRootLogger(), level);
    }
}
