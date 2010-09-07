package com.yahoo.dtf.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Stack;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.yahoo.dtf.util.StringUtil;
import com.yahoo.dtf.xml.ActionParser;
import com.yahoo.dtf.actions.Action;
import com.yahoo.dtf.actions.component.Component;
import com.yahoo.dtf.actions.component.Local;
import com.yahoo.dtf.actions.function.Function;
import com.yahoo.dtf.actions.reference.RefWrapper;
import com.yahoo.dtf.actions.reference.Referencable;
import com.yahoo.dtf.actions.util.CDATA;
import com.yahoo.dtf.exception.ParseException;
import com.yahoo.dtf.functions.Functions;
import com.yahoo.dtf.logger.DTFLogger;
import com.yahoo.dtf.references.References;

/**
 * @dtf.feature Special Characters in XML
 * @dtf.feature.group Script Writing 
 * 
 * @dtf.feature.desc 
 * <p>
 * You can't write everything you would in a text file in an XML file. Sometimes 
 * because certain characters such as <,> or – are used in the XML language 
 * itself and other times because the XML specification 
 * (http://www.w3.org/TR/xml/) has been very careful to normalize some strings 
 * and not others. So lets cover some of the basics of using special characters 
 * in XML and in the comments within an XML document.
 * </p>
 * <p>
 * So firstly lets figure out how to have XML written inside of XML that isn't 
 * interpreted by DTF. This is achieved quite easily using the CDATA encoding 
 * like so:
 * </p>
 * 
 * {@dtf.xml
 *  <property name="xmldata">
 *      <![CDATA[
 *          <myxml>
 *              <list>
 *                  <element1/>
 *                  <element2/>
 *                  <element3/>
 *              </list>
 *          </myxml>]]>
 *  </property>}
 *  
 * <p>
 * Anything written between <![CDATA[ and ]]> is preserved exactly as is and can
 * contain any characters you'd like. Another situation that sometimes comes up 
 * is when you want to write special characters into the attribute value of a 
 * given tag. Here is where the XML specification gives us some trouble, because
 * they have defined that attribute values must be normalized. This 
 * normalization basically converts tabs into a single space which can be quite 
 * annoying and unexpected. In order to work around this you can do the 
 * following:
 * </p>
 * 
 * {@dtf.xml
 *  <sequence>
 *      <property name="tab" value="&amp;#09;"/>   
 *      <property name="another.enter" value="&amp;#10;"/>
 *  </sequence>}
 * 
 * <p> 
 * So you basically have to use the XML special characters when dealing with 
 * attribute values. This means that if you wanted to have more complicated 
 * data with different whitespace characters you should probably use an 
 * underlying text node to your tag and not store this data in the XML attribute.
 * </p>
 * <p>
 * The only thing really missing is special characters within comments and the 
 * only characters that you can't use inside of an XML comment are the – 
 * characters because they confuse most parsers when it comes to knowing where 
 * the comment starts and ends. XML comments are contained between <-- and -->.
 * </p>
 */
public class ActionParser implements ContentHandler {
    
    private static DTFLogger _logger = DTFLogger.getLogger(ActionParser.class);

    private Action _root = null;
    private Action _current = null;
    
    private Stack _stack = new Stack();
    private Stack _charStack = new Stack();
  
    private boolean processRefs = true;
    private boolean processFuncs = true;
    
    private String filename = null;
    
    private static ArrayList _pkgs = null;
    static { 
        init();
    }
    
    public ActionParser() { 
        this(true,true);
    }
    
    public void setFilename(String filename) { 
        this.filename = filename;
    }
    
    public ActionParser(boolean processRefs,
                        boolean processFuncs ) { 
        this.processRefs = processRefs;
        this.processFuncs = processFuncs; 
    }
   
    private Locator _locator = null;

    public Object getResult() {
        return _root;
    }
    
    public Locator getLocator() { return _locator; } 
      
    public void characters(char[] ch, int start, int length)
            throws SAXException {
       ((StringBuffer)_charStack.peek()).append(ch,start,length);
    }

    public void endDocument() throws SAXException { }

    public void endElement(String uri, String localName, String name)
            throws SAXException {
        
        if (name.equals("value")) 
            return;
       
        StringBuffer buff = 
              (_charStack.size() != 0 ? (StringBuffer) _charStack.pop() : null);
        Action action = (_stack.size() != 0 ? (Action) _stack.pop() : null);
        
        if (buff != null && action instanceof CDATA) {
            // you don't want to set the CDATA to an empty string that would not
            // be the real state of the CDATA block
            if (buff.length() != 0) 
                ((CDATA) action).setCDATA(buff.toString());
        }
    }

    public void endPrefixMapping(String prefix) throws SAXException { }
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException { }
    public void processingInstruction(String target, String data) throws SAXException { }
    public void setDocumentLocator(Locator locator) { _locator = locator; }
    public void skippedEntity(String name) throws SAXException { }
    public void startDocument() throws SAXException { }
 
    /*
     * Cache available packages once for reference.
     */
    private static void init() { 
        Package pkg = Action.class.getPackage();
       _pkgs = getSubPackages(pkg.getName());
    }
    
    private Action getAction(String name) throws SAXException { 
        // 1st look up class based on given class name.
        Class actionClass = null;
        String actionPkg = Action.class.getPackage().getName();
        
        try {
            actionClass = Class.forName(name);
        } catch (ClassNotFoundException e) {
            // 2nd look up this class under com.yahoo.dtf.actions package
            if (name.indexOf(".") == -1) { 
                try {
                    actionClass = Class.forName(actionPkg + "." + name);
                } catch (ClassNotFoundException e1) { 
                    name = StringUtil.capitalize(name);
                    // 3rd look up this class under com.yahoo.dtf.actions.* package
                    for(int i = 0; i < _pkgs.size(); i++) { 
                        String pkgName = (String)_pkgs.get(i); 
                        try {
                            actionClass = Class.forName(pkgName + "." + name);
                            break;
                        } catch (ClassNotFoundException e3) { }
                    }
                }
            } 
        }
       
        if (actionClass == null) 
            throw new SAXException("Class not found [" + name + "] under " +
                                   actionPkg);
                
        try {
            Object obj = actionClass.newInstance();
            return (Action)obj;
        } catch (InstantiationException e) {
            throw new SAXException("InstantiationException error.",e);
        } catch (IllegalAccessException e) {
            throw new SAXException("IllegalAccessException error.",e);
        }
    }

    private static ArrayList getSubPackages(String packageName) {
        ArrayList packages = new ArrayList();

        String[] cp = System.getProperty("java.class.path").split(
                "" + File.pathSeparatorChar);

        for (int i = 0; i < cp.length; i++) {
            String jarName = cp[i];
            packageName = packageName.replaceAll("\\.", "/");

            if (jarName.endsWith(".jar")) {
                try {
                    FileInputStream fis = new FileInputStream(jarName);
                    JarInputStream jarFile = new JarInputStream(fis);
                    try { 
	                    JarEntry jarEntry;
	
	                    while (jarFile != null) {
	                        jarEntry = jarFile.getNextJarEntry();
	
	                        if (jarEntry == null)
	                            break;
	
	                        if (jarEntry.getName().startsWith(packageName)
	                                && jarEntry.isDirectory()) {
	                            String name = jarEntry.getName().replaceAll("/",
	                                    "\\.");
	                            name = name.substring(0, name.length() - 1);
	                            packages.add(name);
	                        }
	                    }
                    } finally { 
                        jarFile.close();
                    }
                } catch (IOException ignore) { }
            }
        }

        return packages;
    }

    public void startElement(String uri, 
                             String localName, 
                             String name, 
                             Attributes atts) throws SAXException {
        
        if (name.equals("value")) 
            return;
       
        String classname = atts.getValue("class"); 
       
        if (classname == null)
            classname = name;
        
        _charStack.push(new StringBuffer());
        _current = getAction(classname);
       
        _current.setLine(getLocator().getLineNumber());
        _current.setColumn(getLocator().getColumnNumber());
        _current.setFilename(filename);

        for (int i = 0; i < atts.getLength(); i++) { 
            String attrName = atts.getQName(i);
            String attrValue = atts.getValue(i);
          
            // skip class attribute as its part of DTF special attributes
            if ( attrName.equals("class") || attrName.equals("xmlns") )  
                continue;
            
            Method[] method = _current.getClass().getMethods();

            boolean found = false;
            for (int j = 0; j < method.length; j++) {
                // TODO: later i could implement another way of looking up 
                //       other methods based on types ? 
                // First method to be found will have to do...
                if (method[j].getName().equalsIgnoreCase("set" + attrName)) {
                    Method setMethod = method[j];
                    Class classType = setMethod.getParameterTypes()[0];
                    Object[] args = new Object[1];
                    found = true;
                    try {
                        if (classType.equals(String.class)) {
                            args[0] = attrValue;
                        } else if (classType.equals(Integer.TYPE)) {
                            args[0] = new Integer(attrValue);
                        } else if (classType.equals(Boolean.TYPE)) {
                            args[0] = Boolean.valueOf(attrValue);
                        } else  if (classType.equals(Long.TYPE)) {
                            args[0] = new Long(attrValue);
                        } else if (classType.equals(Double.TYPE)) {
                            args[0] = new Double(attrValue);
                        } else if (classType.equals(Float.TYPE)) {
                            args[0] = new Float(attrValue);
                        } else if (classType.equals(Short.TYPE)) {
                            args[0] = new Short(attrValue);
                        } 
                    } catch (NumberFormatException e) {
                        throw new SAXException(
                                "Failed to set attrib: " + attrName
                                        + " with value: " + attrValue
                                        + " of type: " + classType, e);
                    }
                    
                    if (args[0] == null) {
                        throw new SAXException("setMethod for class "
                                + _current.getClass()
                                + " unsupported argument type: "
                                + classType);
                    }
                 
                    if ( setMethod.isAnnotationPresent(Deprecated.class) )
                        _logger.warn("Deprecated attribute [" + attrName + 
                                     "] used at line " + 
                                     _locator.getLineNumber() + ", column " + 
                                     _locator.getColumnNumber() + " of file [" +
                                     filename + "]");

                    try {
                        setMethod.invoke(_current, args);
                        break;
                    } catch (IllegalArgumentException e) {
                        throw new SAXException("Error exeuting setter.", e);
                    } catch (IllegalAccessException e) {
                        throw new SAXException("Error exeuting setter.", e);
                    } catch (InvocationTargetException e) {
                        throw new SAXException("Error exeuting setter.", e);
                    }
                }
            }
            
            if ( !found ) {
                throw new SAXException("Unable to find setter for attribute [" + 
                                       attrName + "] on class [" + 
                                       _current.getClass() +"]" );
            }

            /*
             * We need to make sure that all fields are name the right way. That
             * is that the field name matches the defined attribute name in the 
             * XSD.
             */
            ArrayList<Field> fields = Action.getAllFields(_current.getClass());
            found = false;
            for(int f = 0; f < fields.size(); f++) { 
                if ( fields.get(f).getName().equalsIgnoreCase(attrName) ) {
                    found = true;
                    break;
                }
            }
            
            if ( !found ) { 
                throw new SAXException("Unable to find class attribute [" + 
                                       attrName + "] on class [" + 
                                       _current.getClass() +"] make sure " +
                                       " the class has all the attributes" +
                                       " with the same name as the attributes " +
                                       " of the XML tag defined in the XSD.");
            }
        }
     
        boolean referencable = false;
        // Reference magic!
        if (_current instanceof Referencable && processRefs) {
            Referencable ref = (Referencable)_current;
            References refs = Action.getState().getReferences();
            try {
                if (ref.isReferencable()) { 
                    referencable = true;
                    if  (!refs.hasReference(ref.getId())) {
                        String id = ref.getId();
                        refs.addReference(id, ref);
                    } else 
                        _logger.warn("Not overwriting reference for [" + ref.getId() + "]");
                } else if (ref.isReference()) {
                    _current = new RefWrapper(ref);
                }
            } catch (ParseException e) {
                throw new SAXException("Unable to add reference.",e);
            }
        } else if (_current instanceof Function && processFuncs) { 
            /*
             * Functions are added to the functions lookup mechanism and are 
             * only executed if they're called from a call tag.
             */
            Function function = (Function) _current;
            Functions functions = Action.getState().getFunctions();
            try {
                if (!functions.hasFunction(function.getName()))
                    functions.addFunction(function.getName(), function);
            } catch (ParseException e) {
                throw new SAXException("Error adding function.",e);
            }
           
            try {
                if (function.getExport()) { 
                    /*
                     * Check that this function has no local tags within it.
                     */
                    if (function.findAllActions(Local.class).size() != 0)
                        throw new SAXException("Exportable functions can not contain any local or component tags.");

                    if (function.findAllActions(Component.class).size() != 0)
                        throw new SAXException("Exportable functions can not contain any local or component tags.");
                    
                    Function.getExportableFunctions().add(function);
                } 
            } catch (ParseException e) {
                throw new SAXException("Error parsing function.",e);
            }
        }
        
        if (_root == null) {
            _root = _current;
            _stack.push(_root);
        } else {
            /*
             * Function are not added to the execution tree.
             * 
             */
            if (!(_current instanceof Function || referencable)) { 
                ((Action)_stack.peek()).addAction(_current);
            }
            
            _stack.push(_current);
        }
    }

    public void startPrefixMapping(String prefix, String uri)
            throws SAXException { }
}
