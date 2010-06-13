package com.yahoo.dtf.init;

import java.io.File;

import com.yahoo.dtf.DTFNode;
import com.yahoo.dtf.DTFProperties;
import com.yahoo.dtf.init.InitClass;
import com.yahoo.dtf.init.InitPlugins;
import com.yahoo.dtf.init.JarFileFilter;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.logger.DTFLogger;
import com.yahoo.dtf.plugin.JarUtil;


public class InitPlugins {
   
    private static DTFLogger _logger = DTFLogger.getLogger(InitPlugins.class);
    
    private static String INIT_CLASS_PROPERTY = "Init-Class";
    
    public static void init() throws DTFException { 
        if (!DTFNode.getType().equalsIgnoreCase("dtfc")) { 
            String home = Action.getConfig().getProperty(DTFProperties.DTF_HOME);
            File lib = new File(home + File.separatorChar + "lib");
          
            // Get all jar files in the directories lib directory.
            File[] jars = lib.listFiles(new JarFileFilter());
            
            for(int i = 0; i < jars.length; i++) { 
                // Check for the XSDFile property value from the Manifest
                File pluginJar = jars[i].getAbsoluteFile();
                String initClassname = JarUtil.getXSDPropertyValue(pluginJar, INIT_CLASS_PROPERTY);
                if (initClassname != null) { 
                    try {
                        Class initClass = Class.forName(initClassname);
                       
                        Object object = initClass.newInstance();
                        
                        if (!(object instanceof InitClass))
                            throw new DTFException("Class [" + initClassname + "]."
                                                   + " does not implement " + 
                                                   Action.getClassName(InitClass.class));
    
                        ((InitClass)object).init();
                        _logger.info("Called init for plugin [" + jars[i].getName() + "].");
                    } catch (ClassNotFoundException e) {
                        throw new DTFException("Unable to call plugin init class [" 
                                               + initClassname + "].",e);
                    } catch (InstantiationException e) {
                        throw new DTFException("Unable to call plugin init class [" 
                                               + initClassname + "].",e);
                    } catch (IllegalAccessException e) {
                        throw new DTFException("Unable to call plugin init class [" 
                                               + initClassname + "].",e);
                    }
                }
            }
        }
    }
}
