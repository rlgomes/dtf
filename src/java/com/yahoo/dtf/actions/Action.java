package com.yahoo.dtf.actions;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yahoo.dtf.DTFConstants;
import com.yahoo.dtf.actions.protocol.SerializationException;
import com.yahoo.dtf.actions.reference.RefWrapper;
import com.yahoo.dtf.comm.Comm;
import com.yahoo.dtf.components.Components;
import com.yahoo.dtf.config.Config;
import com.yahoo.dtf.config.DTFStream;
import com.yahoo.dtf.config.transform.TransformerFactory;
import com.yahoo.dtf.debug.Trace;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.InterruptionException;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.exception.QueryException;
import com.yahoo.dtf.exception.RecorderException;
import com.yahoo.dtf.exception.ResultsException;
import com.yahoo.dtf.functions.Functions;
import com.yahoo.dtf.logger.DTFLogger;
import com.yahoo.dtf.logger.RemoteLogger;
import com.yahoo.dtf.query.Cursor;
import com.yahoo.dtf.recorder.Recorder;
import com.yahoo.dtf.recorder.RecorderBase;
import com.yahoo.dtf.references.References;
import com.yahoo.dtf.results.Results;
import com.yahoo.dtf.results.ResultsBase;
import com.yahoo.dtf.state.ActionState;
import com.yahoo.dtf.state.DTFState;
import com.yahoo.dtf.storage.StorageFactory;
import com.yahoo.dtf.streaming.DTFInputStream;
import com.yahoo.dtf.streaming.StringInputStream;
import com.yahoo.dtf.util.ByteArrayUtil;
import com.yahoo.dtf.util.StringUtil;
import com.yahoo.dtf.xml.DTFXSDHandler;

abstract public class Action implements Externalizable {

    private static DTFLogger _logger = DTFLogger.getLogger(Action.class);
  
    private ArrayList<Action> _list = null;
    
    private int line = -1;
    private int column = -1;
    private String filename = null;
   
    public Action() { 
        _list = new ArrayList<Action>(); 
    }
    
    public int getLine() { return line; }
    public void setLine(int line) { this.line = line; }
    
    public int getColumn() { return column; }
    public void setColumn(int column) { this.column = column; }
   
    public void setFilename(String filename) { this.filename = filename; }
    public String getFilename() { return filename; }
    
    public void addAction(Action action) { 
        _list.add(action); 
    }
    
    public void addActions(List actions) {
        for (int i = 0; i < actions.size(); i++)
            addAction((Action) actions.get(i));
    }

    public boolean hasChildren() { return _list.size() != 0; }
    public ArrayList<Action> children() {
        return new FinalArrayList<Action>(_list);
    }
    
    public void clearChildren() { 
        _list.clear();
    }

    protected Action getAction(int index) { 
       Action action = (Action)_list.get(index); 
       
       if (action instanceof RefWrapper) { 
           try {
            action = ((RefWrapper)action).lookupReference();
           } catch (ParseException e) {
               throw new RuntimeException("This shouldn't happen.",e);
           }
       }
       
       return action;
    }
    
    /**
     * DTF's internal instance of function.
     * 
     * @param type
     * @return boolean
     */
    public boolean anInstanceOf(Class type) { 
        return type.isInstance(this);
    }
   
    /**
     * Put together an array list of the actions of the specified class type that
     * are direct children of this tag.
     *  
     * @param <T>
     * @param T
     * @return
     */
    public <T> ArrayList<T> findActions(Class T) {
        ArrayList<T> result = new ArrayList<T>();
        for (int i = 0; i < _list.size(); i++) {
            if (((Action)_list.get(i)).anInstanceOf(T))
                /*
                 * XXX: whenever I have sometime I should use generics on the 
                 *      remaining calls to make everything work smoothly.
                 */
                result.add((T)getAction(i));
        }
        return result;
    }
   
    /**
     * Put together an array list of the actions that are descedents of this one
     * tag, this will include every action recursively down the action tree.
     * 
     * @param <T>
     * @param T
     * @return
     */
    public <T> ArrayList<T> findAllActions(Class T) {
        ArrayList<T> result = new ArrayList<T>();
        for (int i = 0; i < _list.size(); i++) {
            T t = (T)getAction(i);
            if (((Action)t).anInstanceOf(T)) {
                result.add(t);
            }
            ArrayList<T> sub = ((Action)t).findAllActions(T);
            result.addAll(sub);
        }
        return result;
    }

    /**
     * Find the first action of the specified class type.
     * 
     * @param classType
     * @return
     */
    public Action findFirstAction(Class classType) { 
        for (int i = 0; i < _list.size(); i++) {
            if (((Action)_list.get(i)).anInstanceOf(classType))
                return getAction(i);
        }
        return null;
    }
   
    /**
     * Execute the underlying children of this tag and make sure to keep the 
     * execution state in check as to which tag is being executed.
     * 
     * @throws DTFException
     */
    public void executeChildren() throws DTFException {
        Action current = getState().getAction();
        try {
            for (int i = 0; i < _list.size(); i++) {
                Action action = getAction(i);
                getState().setAction((Action) action);
                if ( Trace.isEnabled() ) 
                    Trace.trace(action);
                action.execute();
            }
        } catch (DTFException e) { 
            throw e;
        } catch (Throwable t) { 
            throw new DTFException("Uncaught Exception",t);
        } finally { 
            getState().setAction(current);
        }
    }
    
    public void executeSelf() throws DTFException {
        getState().setAction(this);
        if ( Trace.isEnabled() ) 
            Trace.trace(this);
        this.execute();
    }
   
    /*
     * Internally used by ActionResult.
     */
    protected void executeChildrenWithoutStateChange() throws DTFException {
        for (int i = 0; i < _list.size(); i++) {
            Action action = getAction(i);
            if ( Trace.isEnabled() ) 
                Trace.trace(action);
            action.execute();
        }
    }
    
    /**
     * Execute the underlying children of this tag that are of the type 
     * specified and make sure to keep the execution state in check as to which 
     * tag is being executed.
     * 
     * @throws DTFException
     */
    public <T extends Action> void executeChildren(Class<T> classType) throws DTFException {
        Action current = getState().getAction();
        try {
            for (int i = 0; i < _list.size(); i++) {
                if (((Action)_list.get(i)).anInstanceOf(classType)) {
                    Action action = getAction(i);
                    getState().setAction(action);
                    if ( Trace.isEnabled() ) 
                        Trace.trace(action);
                    action.execute();
                }
            }
        } catch (DTFException e) { 
            throw e;
        } catch (Throwable t) { 
            throw new DTFException("Uncaught Exception",t);
        } finally { 
            getState().setAction(current);
        }
    }

    /**
     * 
     * @param <T>
     * @param children
     * @throws DTFException
     */
    public <T extends Action> void executeChildren(ArrayList<Action> children) throws DTFException {
        Action current = getState().getAction();
        try {
            for (int i = 0; i < children.size(); i++) {
                Action action = children.get(i);
                getState().setAction(action);
                if ( Trace.isEnabled() ) 
                    Trace.trace(action);
                action.execute();
            }
        } catch (DTFException e) { 
            throw e;
        } catch (Throwable t) { 
            throw new DTFException("Uncaught Exception",t);
        } finally { 
            getState().setAction(current);
        }
    }

    /**
     * Simple utility method to be used in your own thread implementations or 
     * if you create flow control tags that have a loop like behaviour where 
     * they iterate a certain number of times. You can easily call this method
     * on each loop or any point at which it is safe to interrupt and terminate
     * the current thread execution.
     * 
     * This method will generate a DTF specific InterruptionException which is 
     * handled nicely by DTF assuming that the test was currently interrupted.
     *
     * @throws InterruptionException
     * 
     * @dtf.feature Thread Interruption
     * @dtf.feature.group Tag Development
     * 
     * @dtf.feature.desc
     * <p>
     * Thread interruption is a necessary evil in the DTF framework that must be
     * considered by all developers of any tags. You have to think about the 
     * issue of having a long running tag that can't be interrupted in Java 
     * unless you allow it to be. You can call the utility method on Action called
     * checkInterruption() and it will automatically validate if the currently 
     * running thread was interrupted and if so will throw an DTF specific 
     * exception InterruptionException that will be handled by the framework 
     * accordingly. So now in your code you can easily call this method where 
     * you feel you could interrupt your execution without leaving your code in 
     * a state that you can't continue to issue actions from.
     * </p>
     * <p>
     * All of the flow control tags such as for, parallelloop, etc within DTF 
     * are already interruptable and will correctly clean up their underlying
     * spawned threads. Now that your threads are interruptable you only need 
     * to register them with the {@dtf.link Thread Management} layer so that you
     * can have the DTF framework interrupt your threads when necessary. As an 
     * example you can see here the code for a the for tag that checks for 
     * interruption during its normal execution:
     * </p>
     * <pre>
     *  public class For extends Loop {
     *      public For() { }
     *      
     *      public void execute() throws DTFException {
     *          Range range = RangeFactory.getRange(getRange());
     *          try { 
     *              while (range.hasMoreElements()) {
     *                  getConfig().setProperty(getProperty(), range.nextElement());
     *                  executeChildren();
     *                  checkInterruption();
     *              }
     *          } catch (InterruptionException e) { 
     *              if ( getLogger().isDebugEnabled() )
     *                  getLogger().debug("execution interrupted.");
     *          } catch (BreakException e) { 
     *              // break point
     *              if ( getLogger().isDebugEnabled() )
     *                  getLogger().debug("break point hit",e);
     *          }
     *          getConfig().remove(getProperty());
     *     }
     * }
     * </pre>
     * 
     */
    public static void checkInterruption() throws InterruptionException { 
        if ( Thread.currentThread().isInterrupted() ) {
            throw new InterruptionException(Thread.currentThread().getName() + 
                                            " execution interrupted.");
        }
    }
  
    public static DTFState getState() { return ActionState.getInstance().getState(); }
    public static Config getConfig() { return getState().getConfig(); }
    public static Comm getComm() { return getState().getComm(); }
    public static StorageFactory getStorageFactory() { return getState().getStorage(); }
    public static Components getComponents() { return getState().getComponents(); }
    public static Recorder getRecorder() { return getState().getRecorder(); }
    public static Results getResults() { return getState().getResults(); }
    public static Functions getFunctions() { return getState().getFunctions(); } 
    public static References getReferences() { return getState().getReferences(); }

    public static void pushRecorder(RecorderBase recorder, String event) throws RecorderException { 
        Recorder rec = new Recorder(recorder,event);
        rec.start();
        rec.setParent(getState().getRecorder());
        getState().setRecorder(rec);
    }
    
    public static void popRecorder() throws RecorderException {
        Recorder rec = getState().getRecorder(); 
        rec.stop();
        getState().setRecorder(rec.getParent());
    }

    public static void pushResults(ResultsBase results) throws ResultsException { 
        Results res = new Results(results);
        res.start();
        res.setParent(getState().getResults());
        getState().setResults(res);
    }
    
    public static void popResults() throws ResultsException {
        Results res = getState().getResults();
        res.stop();
        getState().setResults(res.getParent());
    }
    
    public static void addCursor(String name, Cursor cursor) throws QueryException  {
        Cursor old = getState().getCursors().getCursor(name);
        
        if ( old != null ) 
            old.close();
            
        getState().getCursors().addCursor(name, cursor);
    }
    
    public static Cursor retCursor(String name) { 
        return getState().getCursors().getCursor(name);
    }
    
    public static String getLocalID() { 
        return (String) getGlobalContext(DTFConstants.DTF_NODE_NAME); 
    }
    
    public String getXMLLocation() { 
        DTFState state = getState();
        
        if (state != null) { 
            if (getLine() != -1) {
                String filename = getFilename(); 
                return (filename != null ? " at " + getFilename() + ":" + getLine() + ":" + getColumn() : "");
            }
        } 
    
        return "";
    }
    
    public void setXMLLocation(Action action) { 
        setFilename(action.getFilename());
        setLine(action.getLine());
        setColumn(action.getColumn());
    }
    
    public static void registerContext(String key, Object value) {
        getState().registerContext(key, value);
    }
    
    public static Object getContext(String key) {
        return getState().getContext(key);
    }
    
    public static void unRegisterContext(String key) { 
        getState().unRegisterContext(key);
    }
   
    public static void registerGlobalContext(String key, Object value) {
        getState().registerGlobalContext(key, value);
    }
    
    public static Object getGlobalContext(String key) {
        return getState().getGlobalContext(key);
    }

    public static void unRegisterGlobalContext(String key) { 
        getState().unRegisterGlobalContext(key);
    }
    
    protected int toInt(String property, String value) throws ParseException {        
    	try { 
            return new Integer(replaceProperties(value)).intValue();
        } catch (NumberFormatException e) { 
            throw new ParseException("Value of property [" + property + 
                                     "] is not an int, got [" + value + "]",e);
        }
    }

    protected int toInt(String property, String value, int defaultValue) 
              throws ParseException {
        try { 
            value = replaceProperties(value);
            
            if (value == null)
                return defaultValue;
            
            return new Integer(value).intValue();
        } catch (NumberFormatException e) { 
            throw new ParseException("Value of property [" + property + 
                                     "] is not an int, got [" + value + "]",e);
        }
    }

    protected double toDouble(String property, 
                              String value, 
                              double defaultValue) throws ParseException {
        try { 
            value = replaceProperties(value);
            
            if (value == null) 
                return defaultValue;
            
            return new Double(value).doubleValue();
        } catch (NumberFormatException e) { 
            throw new ParseException("Value of property [" + property + 
                                     "] is not a double, got [" + value + "]",e);
        }
    }
    
    protected double toDouble(String property, String value) throws ParseException {
        try { 
            return new Double(replaceProperties(value)).doubleValue();
        } catch (NumberFormatException e) { 
            throw new ParseException("Value of property [" + property + 
                                     "] is not a double, got [" + value + "]",e);
        }
    }
    
    protected boolean toBoolean(String property, String value) throws ParseException {
        try { 
            value = replaceProperties(value);
            return Boolean.valueOf(value).booleanValue();
        } catch (NumberFormatException e) { 
            throw new ParseException("Value of property [" + property + 
                                     "] is not a boolean, got [" + value + 
                                     "]",e);
        }
    }

    protected long toLong(String property, 
                          String value, 
                          long defaultValue) throws ParseException {
        try {  
            value = replaceProperties(value);
            
            if (value == null) 
                return defaultValue;
            
            return new Long(value).longValue();
        } catch (NumberFormatException e) { 
            throw new ParseException("Value of property [" + property + 
                                     "] is not an long, got [" + value + "]",e);
        }
    }
    

    protected long toLong(String property, 
                          String value ) throws ParseException {
        try { 
            if (value == null) 
                throw new ParseException("Value of property [" + property +
                                         "] is not an long, got [null]");

            value = replaceProperties(value);
            
            return new Long(value).longValue();
        } catch (NumberFormatException e) { 
                throw new ParseException("Value of property [" + property +
                                         "] is not an long, got [" + value + 
                                         "]",e);
        }
    }

    /* 
     * All characters but $,{ and } because those would mean sub property that
     * needs resolving. The other thing to avoid is just any character in the 
     * property name which we only allow ASCII characters and the usual 
     * Punctuation characters.
     */
    private static Pattern pattern = Pattern.
                     compile("\\$\\{([\\s\\w\\d\\p{Punct}&&[^\\$\\{\\}]]+)\\}");
    
    public static String replaceProperties(String string) throws ParseException {
        return replaceProperties(string, false);
    }
    
    /**
     * INTERNAL USE ONLY
     * 
     * This function is used by special tags that require the ability to do a 
     * lazy evaluation of properties and be able to resolve the properties at 
     * the last possible moment. For an example of how this is used have a look 
     * at the Scanf tag.
     * 
     * @param string
     * @param relaxed
     * @return
     * @throws ParseException
     */
    
    /*
     * For a property to be considered as recursively resolving then we must 
     * find it being matched at the same point it was matched before and not 
     * anywhere else along in the string. That is what this class is used for,
     * every key that is matched within a string is kept track of so that we 
     * can easily look it up and make sure it hasn't already been solved at this
     * exact position.
     */
    private static class MatchKey {
        public int index = 0;
        public String key = null;
        
        public MatchKey(int index, String key ) {
            this.index = index;
            this.key = key;
        }
        
        @Override
        public boolean equals(Object obj) {
            if ( obj instanceof MatchKey ) { 
                MatchKey other = (MatchKey) obj;
                return other.index == index && other.key.equals(key);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return index + key.hashCode();
        }
    }
    
    protected static String replaceProperties(String string, boolean relaxed)
              throws ParseException {
        
        if (string == null) 
            return null;

        /*
         * Not in replace mode means we're about to send the current action to 
         * a component so we don't do any property resolution on the runner side.
         */
        if (!getState().replace())
            return string;
            
        Config config = getConfig();
        boolean hasMatch = true;
        ArrayList<MatchKey> replacements = new ArrayList<MatchKey>();
        
        while ( hasMatch ) { 
            hasMatch = false;
            Matcher match = pattern.matcher(string);
            
            while ( match.find() ) { 
                String group = match.group();
                
                String key = group.substring(2, group.length()-1);
                String value = null;

                if ( replacements.contains(new MatchKey(match.start(),key)) ) {
                    throw new ParseException("Recursive resolution detected [" 
                                             + string + "]");
                }

                int tindex = key.indexOf(':');
                int findex = key.indexOf('(');
               
                /*
                 * Another silly workaround till we make a more robust property
                 * resolver as mentioned in the comment below.
                 */
                if ( tindex != -1 && findex != -1 ) {
                    if ( tindex < findex ) { 
                        findex = -1;
                    } else { 
                        tindex = -1;
                    }
                }
           
                /*
                 * XXX: This could use some cleaning up in the near future. 
                 *      probably move all of this into a PropertyResolver class
                 *      of some sort we're we can do a better job at organizing
                 *      properties/dynamic properties/transformers
                 */
                if ( tindex != -1 ) { 
                    String expr = key.substring(tindex+1);
                    String expr_data = expr.substring(expr.indexOf(":")+1);
                    String auxkey = "${" + key.substring(0,tindex) + "}";
                    String aux = replaceProperties(auxkey,relaxed);
                    
                    if ( !aux.equals(auxkey) ) {
	                    value = TransformerFactory.
	                                 getTransformer(expr).apply(aux, expr_data);
                    }
                } else if ( findex != -1) {
                    int fendex = key.indexOf(')');
                    if ( fendex != -1 ) { 
                        String args = key.substring(findex+1,fendex);
                        key = key.substring(0,findex);
                        value = config.callDynamicProperty(key, args);
                    } else {
                        throw new ParseException("Property missing ')' in [" + 
                                                 key + "]"); 
                    }
                } else {
                    value = config.getProperty(key);
                }
                
                if (value != null) {
                    string = StringUtil.replace(string, group, value);
                    hasMatch = true;
                } else {
                    if ( !relaxed ) { 
	                    throw new ParseException("Property [" + key + 
	                                             "] not found.");
                    } 
                }
                
                replacements.add(new MatchKey(match.start(),key));
            }
        }

        if ( Trace.isEnabled() )  {
            String original = string.substring(0,(string.length() > 1024 ? 1024 : string.length()));
            Trace.tracePropertyResolution(original, string);
        }
        
        return string;
    }

    public static DTFInputStream replacePropertiesAsInputStream(String string)
                  throws ParseException {
        return replacePropertiesAsInputStream(string,
                                              Charset.defaultCharset().displayName());
    }

    public static DTFInputStream replacePropertiesAsInputStream(String string,
                                                                String encoding) 
           throws ParseException {
        
        if (string == null)  
            return DTFStream.getStringAsStream("");

        Config config = getConfig();
        boolean hasMatch = true;

        ArrayList<String> replacements = new ArrayList<String>();
        while (hasMatch) { 
            hasMatch = false;
            Matcher match = pattern.matcher(string);
            
            if (match.find()) { 
                String group = match.group();

                if ( replacements.contains(group) )
                    throw new ParseException("Recursive resolution detected [" 
                                             + string + "]");
                
                String key = group.substring(2, group.length()-1);
                String value = null;

                replacements.add(key);
                int findex = key.indexOf('(');
                
                if ( findex != -1) {
                    int fendex = key.indexOf(')');
                    if ( fendex != -1 ) { 
                        String args = key.substring(findex+1,fendex);
                        key = key.substring(0,findex);
                        if ( config.isStream(key) ) { 
                            DTFStream stream = config.getPropertyAsStream(key);
                            return stream.getValueAsStream(args);
                        }
                    } else {
                        throw new ParseException("Property missing ')' in [" + 
                                                 key + "]"); 
                    }
                } else {
                    value = config.getProperty(key);
                }
                
                if (value != null) {
                    if ( group.length() == string.length() ) 
                        string = value;
                    else 
                        string = StringUtil.replace(string, group, value);
                    hasMatch = true;
                } else {
                    throw new ParseException("Property [" + key + 
                                             "] not found.");
                }

            }
        }
        
        return DTFStream.getStringAsStream(replaceProperties(string));
    }
    
    public String getClassName() { return Action.getClassName(this.getClass()); }
    
    public static String getClassName(Class aClass) { 
        String classname = aClass.getName();
        return classname.substring(classname.lastIndexOf(".")+1,
                                   classname.length());
    }
    
    private Field lookup(Class cl, String name) {
        try {
            Field f = cl.getField(name);
            f.setAccessible(true);
            return f;
        } catch (Exception e) {
            try {
                Field f = cl.getDeclaredField(name);
                f.setAccessible(true);
                return f;
            } catch (SecurityException e1) {
                if ( cl.getSuperclass() != null ) { 
                    return lookup(cl.getSuperclass(),name);
                }
            } catch (NoSuchFieldException e1k) {
                if ( cl.getSuperclass() != null ) { 
                    return lookup(cl.getSuperclass(),name);
                }
            }
        }
        
        return null;
    }
 
    protected Hashtable<String,Object> getAttribs(Class actionClass) { 
        ArrayList<String> attribs = null;
        Hashtable<String, Object> result = new Hashtable<String, Object>();
       
        if (!actionClass.getPackage().getName().contains("actions")) 
           return result;
        
        try {
            attribs = DTFXSDHandler.getInstance().
                                       getAttributes(getClassName(actionClass));
        } catch (DTFException e) {
            throw new RuntimeException(e);
        }
      
        if (attribs == null) 
            return result;

        Class myclass = this.getClass();
        Class[] args = new Class[0];
            
        for (int i = 0; i < attribs.size(); i++) { 
            String key = attribs.get(i);

            try {
                Field field = lookup(myclass, key);
                if ( field != null ) { 
                    field.setAccessible(true);
                    Object obj = field.get(this);
                    if (obj != null)
                        result.put(key, obj);
                } else { 
                    _logger.warn("Error getting attribute: " + key + " on class " + actionClass);
                }
            } catch (SecurityException e) {
                _logger.warn("Error getting attribute: " + key +
                             " on class " + actionClass,e);
            } catch (IllegalArgumentException e) {
                _logger.warn("Error getting attribute: " + key +
                             " on class " + actionClass,e);
            } catch (IllegalAccessException e) {
                _logger.warn("Error getting attribute: " + key +
                             " on class " + actionClass,e);
            }
        }
        
        return result;
    }
    
    private String TO_STRING_SEPERATOR = ",";
    public String toString() {
        StringBuffer result = new StringBuffer();
        Class actionClass = this.getClass();
        Hashtable attribs = getAttribs(actionClass);
        Enumeration enumeration = attribs.keys();

        result.append(getClassName(actionClass) + " {");
        boolean oneappend = false;
        while (enumeration.hasMoreElements()) {
            String key = (String)enumeration.nextElement();
            String value = attribs.get(key).toString();
           
            if ( value.length() > 128 ) 
                value = value.substring(0,128) + "...";
            
            result.append(key + "=" + value + TO_STRING_SEPERATOR);
            oneappend = true;
        }
        
        if (oneappend) 
            result.replace(result.length()-1, result.length(), "");
        
        result.append("}");
        
        return result.toString();
    }
  
    private static final String URI_RE = "[a-zA-Z]+:/{1,3}[^/^:]+/?.*";
    private static Pattern URI_PATTERN = Pattern.compile(URI_RE);
    
    public static URI parseURI(String uri) throws ParseException { 
        
        if (uri == null)
            return null;
      
        uri = replaceProperties(uri);
       
        if (!URI_PATTERN.matcher(uri).matches()) {
            throw new ParseException("Bad URI [" + uri + 
                                     "], must match " + URI_RE);
        }
        
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            throw new ParseException("Bad URI syntax.",e);
        }
    }
   
    public static DTFLogger getLogger() { 
        return _logger;
    } 
   
    /*
     * Private for now because I think I need to solve this slightly differently
     */
    private static RemoteLogger getRemoteLogger() { 
        return RemoteLogger.getInstance();
    }
    
    /**
     * execute the behaviour of this action. 
     *
     */
    abstract public void execute() throws DTFException;

    private static HashMap<String, ArrayList<Field>> _fields = 
                                        new HashMap<String, ArrayList<Field>>();
    
    public static ArrayList<Field> getAllFields(Class cl) { 
        Class x = cl;
        ArrayList<Field> fields = null;
        synchronized(_fields) { 
	        fields = _fields.get(cl.getName());
	        if (fields == null) { 
		        fields = new ArrayList<Field>();
		        while ( cl != null ) { 
		            Field[] aux = cl.getDeclaredFields();
		            for (int i = 0; i < aux.length; i++) { 
		                Field field = aux[i];
		                if (!Modifier.isStatic(field.getModifiers())) { 
		                    aux[i].setAccessible(true);
		                    fields.add(aux[i]);
		                }
		            }
		            cl = cl.getSuperclass();
		        }
		        
		        _fields.put(x.getName(), fields);
	        }
        }
        
        return fields;
    }
   
    private Hashtable<String,String> getProperties() throws DTFException { 
        Hashtable<String, String> result = new Hashtable<String, String>();
        Class cl = getClass();
        
        ArrayList<Field> fields = getAllFields(cl);
        Field f = null;
        
        boolean replace = getState().replace();
        
        for (int i = 0; i < fields.size(); i++) { 
            try {
                f = fields.get(i);
                Object obj = f.get(this);
                if ( !(obj instanceof List) ) {
	                String string  = null;
                    if (obj instanceof byte[]) { 
                        byte[] bytes = (byte[]) obj;
                        string = ByteArrayUtil.byteArrayToHexString(bytes,bytes.length);
                    } else {
                        string  = (obj == null ? null : obj.toString());
                    }
             
                    /*
                     * Replace is not enabled when sending actions within a 
                     * <component> tag but it is enabled when sending using 
                     * other tags such as <share_set> which then guarantees
                     * the necessary properties are replaced on the sender side.
                     */
                    if ( replace ) { 
                        try {
                            string = replaceProperties(string);
                        } catch (ParseException e) {
                            throw new DTFException("Unable to replace properties [" + e.getMessage() + "]");
                        }
                    }
                    
	                if (string != null) {
	                    result.put("set" + f.getName(), string);
	                } 
                }
            } catch (SecurityException e) {
                throw new DTFException("Error retrieving [" + f.getName() + "], " + e.getMessage());
            } catch (IllegalArgumentException e) {
                throw new DTFException("Error retrieving [" + f.getName() + "], " + e.getMessage());
            } catch (IllegalAccessException e) {
                throw new DTFException("Error retrieving [" + f.getName() + "], " + e.getMessage());
            }
        }
      
        return result;
    }
    
    public void writeExternal(ObjectOutput out) throws IOException {
        Hashtable<String, String> attributes = null;
        
        try { 
            attributes = getProperties();
        } catch (DTFException e) { 
            /*
             * If an error occurs here we have to be able to report it but 
             * still complete the description of the previous class because 
             * its already halfway on the pipe.
             */
            out.writeInt(SKIP_FLAG); 
            // SKIP_FLAG will tell the reader to skip this action allowing the
            // exception that is next to execute correctly and return the issue.
            SerializationException se = new SerializationException();
            se.setMessage(e.getMessage());
            se.setLine(getLine());
            se.setColumn(getColumn());
            se.setFilename(getFilename());
            out.writeObject(se);
            return;
        }
	     
        Iterator<Entry<String,String>> entries = attributes.entrySet().iterator();
        out.writeInt(attributes.size());
        while (entries.hasNext()) {
	        Entry<String,String> entry = entries.next();
            out.writeUTF(entry.getKey());
            String value = entry.getValue();
            out.writeObject(value.getBytes("UTF-8"));
        }

        ArrayList<Action> children = children();
        out.writeInt(children.size());
        for (int i = 0; i < children.size(); i++) {
            out.writeObject(children.get(i));
        } 
    }
  
    private final static int SKIP_FLAG = -1;
    private boolean skip = false;
    
    private static HashMap<Class, Method[]> methodLookup = 
                                                 new HashMap<Class, Method[]>();
                                                 
    private Method[] lookupMethods(Class cl) { 
        Method[] methods = methodLookup.get(cl);
        
        synchronized( methodLookup ) {
	        if ( methods == null ) { 
	            methods = cl.getMethods();
	            methodLookup.put(cl,methods);
	        }
        }
        
        return methods;
    }
    
    public void readExternal(ObjectInput in) 
           throws IOException, ClassNotFoundException {
        
        Method[] methods = lookupMethods(this.getClass());
        int props = in.readInt();
        
        if ( props == SKIP_FLAG ) {
            skip = true;
            return;
        }
        
        for (int i = 0; i < props; i++) { 
            String methodname = ((String) in.readUTF());
            
            byte[] buffer = (byte[])in.readObject();
            String value = new String(buffer,0,buffer.length,"UTF-8");
           
            for (int m = 0; m < methods.length; m++) {
            	if (methods[m].getName().equalsIgnoreCase(methodname)) { 
                    Method setMethod = methods[m];
                    Object[] args = new Object[1];
                    try {
                        Class classType = setMethod.getParameterTypes()[0];
                        if (classType.equals(String.class)) {
                            args[0] = value;
                        } else if (classType.equals(Integer.TYPE)) {
                            args[0] = new Integer(value);
                        } else if (classType.equals(Boolean.TYPE)) {
                            args[0] = Boolean.valueOf(value);
                        } else  if (classType.equals(Long.TYPE)) {
                            args[0] = new Long(value);
                        } else if (classType.equals(Double.TYPE)) {
                            args[0] = new Double(value);
                        } else if (classType.equals(Float.TYPE)) {
                            args[0] = new Float(value);
                        } else if (classType.equals(Short.TYPE)) {
                            args[0] = new Short(value);
                        } 
                        setMethod.invoke(this, args);
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException("Unable to serialize object. " + methodname,e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Unable to serialize object. " + methodname,e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException("Unable to serialize object. " + methodname,e);
                    }
                }
            }
        }
       
        int children = in.readInt();
        _list = new ArrayList<Action>(children);
        for (int i = 0; i < children; i++) {  
            Action action = (Action)in.readObject();
            if ( !action.skip ) addAction(action);
        }
    }
   
    /**
     * This method will duplicate this action but all of the properties within 
     * it will be resolved. It is used by the share points and is necessary 
     * because when you set a group of actions on a share point we need to 
     * keep a copy of those actions has all necessary properties nicely resolved
     * so a subsequent get doesn't have to do anything but fetch the result.
     * 
     * @return
     * @throws DTFException
     */
    public Action duplicate() throws DTFException {
        Action action;
        try {
            action = (Action) this.getClass().newInstance();
            Method[] methods = getClass().getMethods();
            Hashtable<String,String> props = getProperties();

            Iterator<Entry<String,String>> entries = props.entrySet().iterator();
            while (entries.hasNext()) {
                Entry<String,String> entry = entries.next();
                String methodname = entry.getKey();
                String value = entry.getValue();
               
                for (int m = 0; m < methods.length; m++) {
                    if (methods[m].getName().equalsIgnoreCase(methodname)) { 
                        Method setMethod = methods[m];
                        Object[] args = new Object[1];
                        try {
                            Class classType = setMethod.getParameterTypes()[0];
                            if (classType.equals(String.class)) {
                                args[0] = value;
                            } else if (classType.equals(Integer.TYPE)) {
                                args[0] = new Integer(value);
                            } else if (classType.equals(Boolean.TYPE)) {
                                args[0] = Boolean.valueOf(value);
                            } else  if (classType.equals(Long.TYPE)) {
                                args[0] = new Long(value);
                            } else if (classType.equals(Double.TYPE)) {
                                args[0] = new Double(value);
                            } else if (classType.equals(Float.TYPE)) {
                                args[0] = new Float(value);
                            } else if (classType.equals(Short.TYPE)) {
                                args[0] = new Short(value);
                            } 
                            setMethod.invoke(action, args);
                        } catch (IllegalArgumentException e) {
                            throw new RuntimeException("Unable to serialize object. " + methodname,e);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Unable to serialize object. " + methodname,e);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException("Unable to serialize object. " + methodname,e);
                        }
                    }
                }
            }
            
            ArrayList<Action> children = children();
            for (int i = 0; i < children.size(); i++)
                action.addAction(children.get(i).duplicate());
            
        } catch (InstantiationException e) {
            throw new DTFException("Error during cloning.",e);
        } catch (IllegalAccessException e) {
            throw new DTFException("Error during cloning.",e);
        } catch (DTFException e) {
            throw new DTFException("Error during cloning.",e);
        }
        
        return action;
    }
}
