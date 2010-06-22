package com.yahoo.dtf.results;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;

import com.yahoo.dtf.results.Result;
import com.yahoo.dtf.results.Results;
import com.yahoo.dtf.results.ResultsBase;
import com.yahoo.dtf.DTFConstants;
import com.yahoo.dtf.DTFProperties;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.config.Config;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.exception.ResultsException;
import com.yahoo.dtf.exception.StorageException;

public class Results {
    private ResultsBase _results = null;
    private Results _parent = null;
    
    private Properties _properties = null;

    private boolean _testsuiteRecorded = false;
    
    private Appender _appender = null;
    private String _logfile = null;
    
    public Results(ResultsBase results) throws ResultsException { 
        _results = results; 
        _properties = new Properties();

        Config config = Action.getConfig();
        if ( results.saveLogs() ) { 
            URI uri = results.getURI();

            try { 
                String format = 
                           config.getProperty(DTFProperties.DTF_LOGGING_FORMAT);
                Layout layout = new PatternLayout(format);
                
                String output = "script-" +  
                            Action.getConfig().getProperty(DTFConstants.SCRIPT_ID) +
                            ".out";
                URI loguri =  new URI(uri.getScheme(), 
                                      uri.getHost(),
                                      File.separatorChar + output,
                                      null);
                
                _logfile = Action.getStorageFactory().getPath(loguri);
                _appender = new FileAppender(layout,_logfile,false);
                
                Action.getLogger().addAppender(_appender);
            } catch (URISyntaxException e) {
                throw new ResultsException("Unable to add appender to logger.",e);
            } catch (StorageException e) {
                throw new ResultsException("Unable to add appender to logger.",e);
            } catch (IOException e) {
                throw new ResultsException("Unable to add appender to logger.",e);
            } catch (ParseException e) {
                throw new ResultsException("Unable to add appender to logger.",e);
            }
            
            recordProperty(DTFProperties.DTF_TESTCASE_LOG, _logfile);
        }
    }
    
    public void recordProperty(String key, String value) { 
        _properties.setProperty(key, value);
    }
    
    public boolean isTestSuiteRecorded() { return _testsuiteRecorded; }
    public void setTestSuiteRecorded(boolean value) { _testsuiteRecorded = value; }
    
    public void recordResult(Result result) throws ResultsException {
        
        if (_results == null) 
            return;
        
        if (result.isTestSuite()) _testsuiteRecorded = true;
          
        result.setProperties(_properties);
        _properties = new Properties();
        
        synchronized(_results) { 
            _results.recordResult(result);
        }
            
        if (_parent != null) 
            _parent.recordResult(result);
        
    }
    
    public void setParent(Results results) { _parent = results; } 
    public Results getParent() { return _parent; } 
    
    public void start() throws ResultsException { _results.start(); } 
    public void stop() throws ResultsException { 
        if (_appender != null) { 
            Action.getLogger().removeAppender(_appender);
            _appender.close();
        }
        
        _results.stop(); 
    } 
}
