package com.yahoo.dtf.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.yahoo.dtf.DTFProperties;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.config.Config;
import com.yahoo.dtf.exception.DTFException;

public class DTFXSDHandler extends XSDHandler {

    public DTFXSDHandler(InputStream dtdIS) throws DTFException {
        super(dtdIS);
    }

    private static DTFXSDHandler _instance = null;
    
    public synchronized static DTFXSDHandler getInstance() throws DTFException {
        if (_instance == null) { 
            Config config = Action.getConfig();
            String xsdFilename = config.getProperty(DTFProperties.DTF_XSD_FILENAME);
            String home = config.getProperty(DTFProperties.DTF_HOME);
            try {
                _instance = new DTFXSDHandler(
                                new FileInputStream(home + File.separatorChar + 
                                                    xsdFilename));
            } catch (FileNotFoundException e) {
                throw new DTFException("Unable to find dtd [" + xsdFilename + "]",e);
            }
        }
        
        return _instance;
    }
}
