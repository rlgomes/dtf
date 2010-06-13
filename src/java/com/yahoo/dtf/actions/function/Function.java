package com.yahoo.dtf.actions.function;

import java.util.ArrayList;

import com.yahoo.dtf.actions.flowcontrol.Sequence;
import com.yahoo.dtf.actions.function.Param;
import com.yahoo.dtf.actions.properties.Property;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.config.Config;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.recorder.internal.ConfigRecorder;
import com.yahoo.dtf.state.ActionState;
import com.yahoo.dtf.state.DTFState;

/**
 * @dtf.tag function
 * 
 * @dtf.since 1.0
 * @dtf.author Rodney Gomes
 * 
 * @dtf.tag.desc Define a function that  can be called from anywhere in the 
 *               subsequent XML script. This function can also be imported using 
 *               the import tag defined up ahead.
 *
 * @dtf.tag.example 
 * <function name="func3">
 *      <param name="nomore" type="optional"/>  
 *      <param name="flag" default="false"/>  
 *      
 *      <log>In func3</log>
 * </function>
 * 
 * @dtf.tag.example 
 * <function name="func4" export="true">
 *      <log>XXX</log>
 *      <return>BLAH</return>
 * </function>
 */
public class Function extends Action {

    public final static String EXPORTED_FUNCTIONS_CTX = "dtf.global.functions.ctx";
    
    /**
     * @dtf.attr name
     * @dtf.attr.desc Specifies a unique name for the function in this script 
     *                file.
     */
    private String name = null;
   
    /**
     * @dtf.attr export
     * @dtf.attr.desc specifies if this function is to be exported so it is 
     *                accessible within any component used by this test case.
     *                The functions are inspected to make sure that they do not 
     *                contain any references to the local or component tag. The 
     *                local tag cannot be exported because it is a functionality 
     *                of the DTFX and would allow for certain operations such as 
     *                lockcomponent, loadproperties, etc. to be executed on the 
     *                component side.
     */
    private String export = null;

    public void execute() throws DTFException {
        throw new RuntimeException("Unsupported operation");
    }
    
    private static Object _lock = new Object();
    public static ArrayList<Function> getExportableFunctions() { 
        synchronized(_lock) { 
            ArrayList<Function> functions = 
                 (ArrayList<Function>) getGlobalContext(EXPORTED_FUNCTIONS_CTX);
           
            if ( functions == null ) { 
                functions = new ArrayList<Function>();
                registerGlobalContext(EXPORTED_FUNCTIONS_CTX, functions);
            }
        
            return functions;
        }
    }

    public void executeFunction(ArrayList<Property> args) 
           throws DTFException {
        /*
         * Verify parameters before executing
         */
        ArrayList<Param> params = findActions(Param.class);
        ActionState as = ActionState.getInstance();
        DTFState current = as.getState();
        DTFState state = (DTFState) current.duplicate();

        for (int a = 0; a < args.size(); a++) { 
            Property prop = args.get(a);
            boolean found = false;
            for (int p = 0; p < params.size(); p++) { 
                Param param = params.get(p);
                if (prop.getName().equals(param.getName())) { 
                    found = true;
                    break;
                }
            }
            
            if ( !found ) {
                getLogger().warn("Argument [" + prop.getName() + 
                                 "] not being used by the function [" + 
                                 getName() + "]");
            }
        }
      
        for (int p = 0; p < params.size(); p++) { 
            Param param = (Param)params.get(p);
            
            if (param.isRequired()) { 
                boolean found = false;
                for (int i = 0; i < args.size(); i++) { 
                    Property prop = args.get(i);
                    if (prop.getName().equals(param.getName())) { 
                        state.getConfig().setProperty(param.getName(),
                                                      prop.getValue(),
                                                      true);
                        found = true;
                        break;
                    }
                }
                
                if (!found) { 
                    if (state.getConfig().getProperty(param.getName()) == null)
                            throw new DTFException("Missing required parameter "
                                                   + param);
                } 
            } else if (param.isOptional()) { 
                boolean found = false;
                for (int i = 0; i < args.size(); i++) { 
                    Property prop = args.get(i);
                    if (prop.getName().equals(param.getName())) { 
                        state.getConfig().setProperty(param.getName(),
                                                      prop.getValue(),
                                                      true);
                        found = true;
                        break;
                    }
                }
                
                if (!found && param.getDefault() != null) { 
                    state.getConfig().setProperty(param.getName(),
                                                  param.getDefault(),
                                                  true);
                }
            } 
        }
        
        Sequence sequence = new Sequence();
        sequence.addActions(children());
        
        as.setState(state);
        try { 
            Config config = current.getConfig();
            // using the config recorder to make sure the events get recorded 
            // into the parent thread!
            pushRecorder(new ConfigRecorder(config),null);
            sequence.execute();
        } finally { 
            popRecorder();
            as.delState();
            as.setState(current);
        }
    }
    
    public String getName() throws ParseException { return replaceProperties(name); }
    public void setName(String name) { this.name = name; }

    public boolean getExport() throws ParseException { return toBoolean("export",export); }
    public void setExport(String export) { this.export = export; }
}
