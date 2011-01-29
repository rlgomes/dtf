package com.yahoo.dtf.javadoc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.util.StringUtil;
import com.yahoo.dtf.xml.XSDHandler;

/**
 *            
 * @dtf.feature XML Documentation
 * @dtf.feature.group Tag Development
 * @dtf.feature.desc 
 * 
 * <p>
 * When writing tests you can document those tests using JavaDoc tags, that are 
 * then transformed into this document you are reading right now. Using these 
 * tags is a really simple and you only need to know some HTML if you want to 
 * make your documentation look a bit neater. Here are the list of available 
 * documentation tags that you can use within your tags and also to document any
 * new features built into DTF.
 * </p>
 * <p>
 * For examples on using these tags just have a look at any source file with 
 * '@dtf.feature' or '@dtf.tag' taglets in them and you'll find various examples 
 * on how to use this feature to better the documentation of your code.
 * </p>
 * 
 * <h3>Tag Documentation</h3>
 * <table border="1" width="75%">
 *      <tr><th colspan="2">Class Taglets</th></tr>
 *      <tr><th width="200px">Taglet</th><th>Description</th></tr>
 *      <tr>
 *          <td><b>@dtf.tag tag</b></td>
 *          <td>This tag is required to identify that the following 
 *              documentation is for a tag named tag.
 *          </td>
 *      </tr>
 *      <tr>
 *          <td><b>@dtf.tag.desc desc</b></td>
 *          <td>This is where you can place a detailed description of what this 
 *              tag does and what other resources are useful for reading before 
 *              using this tag. You can place almost any HTML elements in this 
 *              section which allows you to format your documentation nicely 
 *              using tables and lists.
 *          </td>
 *      </tr>
 *      <tr>
 *          <td><b>@dtf.author author</b></td>
 *          <td>Here you can put the author or authors names who are responsible 
 *              for maintaining this tag.</td>
 *      </tr>
 *      <tr>
 *          <td><b>@dtf.since version</b></td>
 *          <td>Just a simple indication of since when this tag has existed in 
 *              terms of versions of DTF.</td>
 *      </tr>
 *      <tr>
 *          <td><b>@dtf.event name</b></td>
 *          <td>This taglet is used to document exactly what events are being 
 *              thrown by the tag being documented. This makes the life of the 
 *              test writer really simple because he/she can consult this 
 *              documentation to easily identify what information is available 
 *              from executing this tag.
 *          </td>
 *      </tr>
 *      <tr>
 *          <td><b>@dtf.event.attr name</b></td>
 *          <td>With this taglet you can identify all the names of attributes 
 *              that an event has so that the test writer knows exactly what 
 *              he/she can get back from executing your tag. This tag depends on
 *              the previously documented event name so be sure to use the 
 *              '@dtf.event' taglet before this one.</td>
 *          </tr>
 *      <tr>
 *          <td><b>@dtf.event.attr.desc desc</b></td>
 *          <td>This taglet is used to describe the previously identified 
 *              attribute using the @dtf.event.attr taglet. So be sure to use 
 *              that taglet previous to this one otherwise logically the
 *              documentation won't make sense.
 *          </td>
 *      </tr>
 *      <tr>
 *          <td><b>@dtf.example example</b></td>
 *          <td>This tag can be used to identify example XML documentation that
 *              shows exact XML samples of how to use the tag being documented.
 *              So the body of this taglet can be any XML you'd like just be 
 *              sure to place a single root element around your XML.
 *          </td>
 *      </tr>
 *  </table>
 *  <br/> 
 *  
 *  <table border="1" width="75%">
 *      <tr><th colspan="2">Attribute Taglets</th></tr>
 *      <tr><th width="200px">Taglet</th><th>Description</th></tr>
 *      <tr>
 *          <td><b>@dtf.attr name</b></td>
 *          <td>This identifies the name of the attribute being documented here.</td>
 *      </tr>
 *      <tr>
 *          <td><b>@dtf.attr.desc desc</b></td>
 *          <td>
 *          This is where you can place the description of the attribute being 
 *          documented.You can place almost any HTML elements in this section 
 *          which allows you to format your documentation nicely using tables 
 *          and lists.
 *          </td>
 *      </tr>
 *  </table>
 *  <br/>
 *  
 *  <table border="1" width="75%">
 *      <tr><th colspan="2">Utility Taglets</th></tr>
 *      <tr><th width="200px">Taglet</th><th>Description</th></tr>
 *      <tr>
 *          <td><b>@dtf.link name</b></td>
 *          <td>
 *          This tag allows you to create a link to another tag/feature just by
 *          having the correct name specified. The name is case insensitive so 
 *          that you don't have to know the exact case spelling of the tag or 
 *          feature you're trying to link to.
 *          </td>
 *      </tr>
 *  </table>
 *  <br/>
 *
 *  <h2>Feature Documentation</h2>
 *  <p>
 *  The feature documentation allows the developer to document features that may 
 *  not be a simple XML tag and instead could be the documentation of how 
 *  certain dynamic properties work or the documentation on how ranges work.
 *  </p>
 *  <p>
 *  The only two things you need to define for a feature is what the name of
 *  the feature is and which feature group it belongs to. The name of the feature
 *  will appear as a link underneat the feature group. There are some already 
 *  existing groups that you can attach your documentation to or you can build
 *  your own group name and that will be added to the list of documented 
 *  features in the generated feature documentation.
 *  </p>
 *  <table border="1" width="75%">
 *      <tr><th colspan="2">Feature Taglets</th></tr>
 *      <tr><th width="200px">Taglet</th><th>Description</th></tr>
 *      <tr>
 *          <td><b>@dtf.feature name</b></td>
 *          <td>The name of the feature being documented.</td>
 *      </tr>
 *      <tr>
 *          <td><b>@dtf.feature.group group</b></td>
 *          <td>
 *          The logical group belongs to within the documentation being 
 *          generated. This is going to be used to display this feature within
 *          the same group as as other features with the same group name.
 *          </td>
 *      </tr>
 *      <tr>
 *          <td><b>@dtf.feature.desc desc</b></td>
 *          <td>
 *          The description of this feature along with any information that 
 *          might be useful for the test writers that may find this feature to 
 *          be useful. You can place almost any HTML elements in this section 
 *          which allows you to format your documentation nicely using tables 
 *          and lists.
 *          </td>
 *      </tr>
 *      <tr>
 *          <td><b>@dtf.example example</b></td>
 *          <td>
 *          This tag can be used to identify example XML documentation that 
 *          shows exact XML samples of how to use the feature being documented.
 *          So the body of this taglet can be any XML you'd like just be sure to
 *          place a single root element around your XML.
 *          </td>
 *     </tr>
 * </table>
 */
public class DTFDoclet {
   
    private static final String TAGS_DIRECOTRY      = "tags";
    private static final String FEATURES_DIRECTORY  = "features";
    
    public static final String DTF_TAG              = "dtf.tag";
    public static final String DTF_AUTHOR           = "dtf.author";
    public static final String DTF_SINCE            = "dtf.since";
    public static final String DTF_TAG_DESC         = "dtf.tag.desc";
    public static final String DTF_TAG_EXAMPLE      = "dtf.tag.example";

    public static final String DTF_ATTR             = "dtf.attr";
    public static final String DTF_ATTR_DESC        = "dtf.attr.desc";

    public static final String DTF_LINK             = "dtf.link";
    public static final String DTF_XML              = "dtf.xml";

    public static final String DTF_EVENT            = "dtf.event";
    public static final String DTF_EVENT_ATTR       = "dtf.event.attr";
    public static final String DTF_EVENT_ATTR_DESC  = "dtf.event.attr.desc";

    public static final String DTF_FEATURE          = "dtf.feature";
    public static final String DTF_FEATURE_GROUP    = "dtf.feature.group";
    public static final String DTF_FEATURE_DESC     = "dtf.feature.desc";
    public static final String DTF_EXAMPLE          = "dtf.example";

    /*
     * Used to basically flag that a specific tag should not be added to the 
     * main page indexed tags. This is useful if you have tags that only apply
     * within certain other tags and shouldn't be seen as root level tags.
     */
    public static final String DTF_SKIP_INDEX       = "dtf.skip.index";
    
    private static class DTFDoc { 
        public String name = null;
        public Tag[] descriptions = null;
        public Tag[] examples = null;
        public String qname = null;
    }
    
    public static boolean start(RootDoc root) throws Exception {
        ClassDoc[] classes = root.classes();
       
        ArrayList<String> packagenames = new ArrayList<String>();
        HashMap<String, ArrayList<ClassDoc>> packages = 
                                       new HashMap<String, ArrayList<ClassDoc>>();
        

        ArrayList<String> fgroupnames = new ArrayList<String>();
        HashMap<String, ArrayList<DTFDoc>> fgroups = 
                                       new HashMap<String, ArrayList<DTFDoc>>();
        
        String[][] options = root.options();
        
        String destination = null;
        String xsd = null;
        
        for(int i = 0; i < options.length; i++) { 
            if (options[i][0].equals("-d")) {
                destination = options[i][1];
            }

            if (options[i][0].equals("-dtfxsd")) {
                xsd = options[i][1];
            }
        }
        
        if (destination == null) {
            throw new IllegalArgumentException("-d option needs to be specified");
        }

        if (xsd == null) {
            throw new IllegalArgumentException("-dtfxsd option needs to be specified");
        }

        String tagdir = destination + File.separatorChar + TAGS_DIRECOTRY;
        File dir = new File(tagdir);
        if ( !dir.exists() && !dir.mkdirs() ) { 
            throw new DTFException("Unable to create directory [" + tagdir + "]");
        }

        String featuredir = destination + File.separatorChar + FEATURES_DIRECTORY;
        dir = new File(featuredir);
        if ( !dir.exists() && !dir.mkdirs() ) { 
            throw new DTFException("Unable to create directory [" + featuredir + "]");
        }
        
        PrintStream psIndex = createFile(destination + File.separatorChar + "index.html");
        psIndex.println("<html>");
        psIndex.println("   <head>");
        psIndex.println("   <link rel='stylesheet' " +
        		                 "href='main.css' " +
        		                 "type='text/css'>");
        psIndex.println("   </head>");
        psIndex.println("   <body id='body'>");
        psIndex.println("       <h1>DTF Documentation</h1>");
       
        ArrayList<String> tnames = new ArrayList<String>();
        ArrayList<String> fnames = new ArrayList<String>();
        /*
         * 1st phase
         * 
         * Filter out non DTF related JavaDocs and also gather the list of 
         * packages that contain different DTF Actions. Create a TOC for the 
         * whole document.
         */
        for (int i = 0; i < classes.length; ++i) {
            ClassDoc classdoc = classes[i];
          
            /*
             * Identify which ones have DTF XML tags
             */
            if (classdoc.tags(DTF_TAG).length != 0) {
                String classname = classdoc.qualifiedName();
                String packagename = classname.substring(0, classname.lastIndexOf("."));
              
                tnames.add(classname.replace(packagename + ".", "").toLowerCase());
                
                if (!packages.containsKey(packagename)) {
                    packages.put(packagename, new ArrayList<ClassDoc>());
                    packagenames.add(packagename);
                }
                
                packages.get(packagename).add(classdoc);
            }
            
            MethodDoc[] methoddocs = classes[i].methods();
            
            for (int m = 0; m < methoddocs.length; m++ ) { 
                if ( methoddocs[m].tags(DTF_FEATURE).length != 0 ) { 
                    String fgroup = methoddocs[m].tags(DTF_FEATURE_GROUP)[0].text();

                    if (!fgroups.containsKey(fgroup)) {
	                    fgroups.put(fgroup, new ArrayList<DTFDoc>());
	                    fgroupnames.add(fgroup);
	                }

                    DTFDoc dtfdoc = new DTFDoc();
                    dtfdoc.name = methoddocs[m].tags(DTF_FEATURE)[0].text();
                    dtfdoc.descriptions = methoddocs[m].tags(DTF_FEATURE_DESC);
                    dtfdoc.examples = methoddocs[m].tags(DTF_EXAMPLE);
                    
                    
                    fnames.add(dtfdoc.name.trim().toLowerCase());
	                
	                fgroups.get(fgroup).add(dtfdoc);
                }
            }
            
            /*
             * Find the documented features through out the code as well
             */
            if (classdoc.tags(DTF_FEATURE).length != 0) {
                String fgroup = classdoc.tags(DTF_FEATURE_GROUP)[0].text();
                
                if (!fgroups.containsKey(fgroup)) {
                    fgroups.put(fgroup, new ArrayList<DTFDoc>());
                    fgroupnames.add(fgroup);
                }

                DTFDoc dtfdoc = new DTFDoc();
                dtfdoc.name = classdoc.tags(DTF_FEATURE)[0].text();
                dtfdoc.descriptions = classdoc.tags(DTF_FEATURE_DESC);
                dtfdoc.examples = classdoc.tags(DTF_EXAMPLE);
                dtfdoc.qname = classdoc.qualifiedName();

                fnames.add(dtfdoc.name.trim().toLowerCase());
                
                fgroups.get(fgroup).add(dtfdoc);
            }
        }
       
        /*
         * Sorting makes it easier to find what you want in the documentation
         */
        Collections.sort(packagenames);
        Collections.sort(fgroupnames);

        Iterator<String> iter = fgroupnames.iterator();

        psIndex.println("<div id='features'>");
        psIndex.println("   <div id='features_title'>Features</div>");
        int counttags = 0;
        while (iter.hasNext()) {
            if ( counttags == 0 ) {
                psIndex.println("<div class='feature_row'>");
            }
            
            psIndex.println("<div class='feature_div'>");
            String feature = (String) iter.next();
            psIndex.println("<div class='feature_name'>" +
            		             feature + "</div>");
            psIndex.println("<div id='collapsible_"+ feature + 
                               "' class='feature_elems'" + 
                                 "style='display: block;'>");
            ArrayList<DTFDoc> features = fgroups.get(feature);
            for (int i = 0; i < features.size(); i++) { 
                DTFDoc dtfdoc = features.get(i);
                psIndex.println("<a class='feature_elem' href='" + FEATURES_DIRECTORY + "/" + 
                              dtfdoc.name.toLowerCase() + ".html'>" + dtfdoc.name + 
                              "</a>");
            }
            psIndex.println("</div>");
            psIndex.println("</div>");
            
            counttags++;
            if ( counttags == 4 ) { 
                psIndex.println("</div>");
                counttags = 0;
            }
        }

        if ( counttags > 0  ) { 
            psIndex.println("</div>");
        }
        psIndex.println("</div>");
        
        /*
         * Link to the root element of the DTF XML as a starting point for the 
         * testcase writing.
         */
        Collections.sort(packagenames);
        iter = packagenames.iterator();
       
        psIndex.println("<div id='tags'>");
        psIndex.println("<div id='tags_title'>Tags</div>");
        
        counttags = 0;
        while (iter.hasNext()) {
            String pname = (String) iter.next();
            ArrayList<ClassDoc> tags = packages.get(pname);
           
            String aux = pname.substring(pname.lastIndexOf(".actions.") + 
                                         "actions.".length()+1);
            aux = aux.replaceAll("\\."," ");
            aux = StringUtil.capitalize(aux);  
            
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < tags.size(); i++) { 
                ClassDoc classdoc = (ClassDoc)tags.get(i);
               
                if ( classdoc.tags(DTF_SKIP_INDEX).length == 0 ) {
	                String tagname = classdoc.name().toLowerCase();
	            
	                buffer.append("<a class='tag_elem' href='" + 
	                              TAGS_DIRECOTRY + "/" + tagname + ".html'>" + 
	                              tagname + "</a> ");
                }
            }
            
            if ( buffer.length() != 0 ) { 
	            if ( counttags == 0 ) {
	                psIndex.println("<div class='tag_row'>");
	            }
            
	            psIndex.println("<div class='tag_div'>");
                psIndex.println("<div class='tag_name' " +
                		             "onclick=\"togglediv('collapsible_" + 
                		             aux + "');\">" + 
                		             aux + "</div>");
	            psIndex.println("<div id='collapsible_" + aux + "' " + 
	                                 "class='tag_elems' " + 
	                                 "style='display: block;'>");
	            psIndex.print(buffer.toString());
	            psIndex.println("</div>");
	            psIndex.println("</div>");
	           
	            counttags++;
	            if ( counttags == 5 ) { 
	                psIndex.println("</div>");
	                counttags = 0;
	            }
            }
        }

        if ( counttags > 0  ) { 
            psIndex.println("</div>");
        }
        psIndex.println("</div>");
       
        /*
         * 1st phase 
         * 
         * Tag documentation generation that will generate a separate HTML file
         * for each of the DTF actions that are documented with DTF javadoc
         * tags.
         */
        iter = packagenames.iterator();
        
        FileInputStream fis;
        try {
            fis = new FileInputStream(xsd);
        } catch (FileNotFoundException e) {
            throw new Exception("Error accessing XSD.",e);
        }
        XSDHandler dtfXSD = new XSDHandler(fis);
        
        while (iter.hasNext()) {
            String pname = (String) iter.next();
            ArrayList tags = (ArrayList)packages.get(pname);
            info("Package " + pname);
            for (int i = 0; i < tags.size(); i++) { 
                ClassDoc classdoc = (ClassDoc)tags.get(i);
                String tagname = classdoc.name().toLowerCase();
                PrintStream ps = createFile(tagdir + File.separatorChar + 
                                            tagname + ".html");
                
                info("Tag " + tagname);
                
                ps.println("<html>");
                ps.println("    <head>");
                ps.println("    <link rel='stylesheet' " +
                		              "href='../main.css' " +
                		              "type='text/css'>");
                ps.println("        <title>" + classdoc.name() + "</title>");
                ps.println("    </head>");
                ps.println("    <body id='body'>");
                ps.println("<div id='nav'>");
                ps.println("<a href='../index.html'>Home</a></div>");
                
                // Hiding the class name in the tooltip of the title :)
                ps.println("<div id='tag_title' title='" + classdoc.qualifiedName() + 
                           "'>" + classdoc.name() + "</div>");

                /*
                 * Description
                 */
                ps.println("<div id='tag_description'>");
                Tag[] descriptions = classdoc.tags(DTF_TAG_DESC);
                if (descriptions.length != 0) { 
                    for (int d = 0; d < descriptions.length; d++) { 
                        ps.print(treatTag(descriptions[d],tnames,fnames));
                    }
                }
                ps.println("</div>");
                
                /*
                 * Child tags
                 */
                String children = dtfXSD.generateChildrenString(tagname);
                if ( children != null && children.trim().length() > 0 ) { 
                    Pattern pattern = Pattern.compile("([a-zA-Z_]*)");
                    Matcher matcher = pattern.matcher(children);
                    StringBuffer result = new StringBuffer();
                    int laststart = 0;
                    while ( matcher.find() ) { 
                        String match = matcher.group();
                        int start = matcher.start();
                        int end = matcher.end();
                        
                        match = match.toLowerCase();

                        if ( match.trim().length() > 0 ) {
                            result.append(children.substring(laststart,start));
                            result.append("<a href='" + match + ".html'>" + match + "</a>");
                            laststart=end;
                        }
                    }
                    result.append(children.substring(laststart));
                    
                    ps.print("<div class='tag_children_label'>Children Tags</div>");
                    ps.print("<div class='tag_children'>");
                    ps.print(result.toString());
                    ps.print("</div>");
                }
                
                /*
                 * Events
                 */
                Tag[] events = classdoc.tags(DTF_EVENT);
                Tag[] eventsAttrs = classdoc.tags(DTF_EVENT_ATTR);
                Tag[] eventsDesc= classdoc.tags(DTF_EVENT_ATTR_DESC);
                
                if (eventsAttrs.length != eventsDesc.length) { 
                    // XXX: throw an exception
                }
               
                if (eventsAttrs.length != 0) {
                    HashMap<String, HashMap<String, String>> eventMap = 
                                  new HashMap<String, HashMap<String,String>>();

                    /*
                     * Construct all the relations between events and their 
                     * attributes.
                     */
                    for (int a = 0; a < events.length; a++) { 
                        String[] eventNames = events[a].text().trim().split(" ");
                       
                        for (int e = 0; e < eventNames.length; e++) { 
                            if (!eventMap.containsKey(eventNames[e])) {
                                eventMap.put(eventNames[e], new HashMap<String,String>());
                            }

                            String attrName = eventsAttrs[a].text();
                            String attrDesc = treatTag(eventsDesc[a],tnames,fnames);
                           
                            ((HashMap)eventMap.get(eventNames[e])).put(attrName, attrDesc);
                        }
                    }
                    
                    ps.print("<div class='tag_events_label'>Events</div>");
                    Iterator<Entry<String,HashMap<String,String>>> eventIter = 
                                                 eventMap.entrySet().iterator();
                    
                    ps.print("<div class='tag_events'>");
                    while ( eventIter.hasNext() ) { 
                        Entry<String,HashMap<String, String>> entry =  
                                                               eventIter.next();
                        
                        HashMap<String, String> attribs = entry.getValue();
                        ps.print("<div class='tag_event'>");
                        ps.print("<div class='tag_event_name'>" + entry.getKey() 
                                 + " Event</div>");
                        
                        Iterator<Entry<String,String>> attribIter = attribs.entrySet().iterator();
                        while ( attribIter.hasNext() ) { 
                            Entry<String,String> attrib = attribIter.next();
                            ps.print("<div class='tag_event_attribute'>");
                            ps.print("<div class='tag_event_attribute_name'>" +
                                     attrib.getKey() + "</div>");
                            ps.print("<div class='tag_event_attribute_value'>" +
                                     attrib.getValue() + "</div>");
                            ps.print("</div>");
                        }
                        ps.print("</div>");
                    }
                    ps.print("</div>");
                }

                /*
                 * Attributes
                 */
                HashMap attrs = new HashMap();
                ClassDoc aux = classdoc;
                while (aux != null) { 
                    FieldDoc[] fieldDocs = aux.fields();
                    for (int f = 0; f < fieldDocs.length; f++) {
                        attrs.put(fieldDocs[f].name(), fieldDocs[f]);
                    }
                    aux = aux.superclass();
                }
               
                ArrayList<String> attributes = dtfXSD.getAttributes(tagname);
                ArrayList<String> required = dtfXSD.getRequiredAttributes(tagname);
                ArrayList<String> optional = dtfXSD.getOptionalAttributes(tagname);
                
                if ( attributes != null ) {
                    boolean someopt = false;
                    StringBuffer opt = new StringBuffer();
                    
                    boolean somereq = false;
                    StringBuffer req = new StringBuffer();

                    opt.append("<div id='optional_attributes_label'>Optional Attributes</div>");
                    opt.append("<div id='optional_attributes'>");
    
                    req.append("<div id='required_attributes_label'>Required Attributes</div>");
                    req.append("<div id='required_attributes'>");
                    
                    Iterator<String> keys = attributes.iterator();
                    
                    while (keys.hasNext()) {
                        String aName = keys.next();
                        StringBuffer which = null;
                        
                        if ( required.contains(aName) ) {
                            which = req;
                            somereq = true;
                        } else if ( optional.contains(aName) ) { 
                            which = opt;
                            someopt = true;
                        }
                      
                        if ( which != null ) { 
                            which.append("<div class='attribute'>");
	                        which.append("<div class='attribute_name'>" + aName + "</div>");
	                        FieldDoc doc = (FieldDoc)attrs.get(aName);
	                        if (doc != null) { 
	                            Tag[] descs = doc.tags(DTF_ATTR_DESC);
	                                    
	                            if (descs.length != 0) {
	                                which.append("<div class='attribute_desc'>" + 
	                                             treatTag(descs[0],tnames,fnames) + 
	                                             "</div>");
	                            }
	                        }
	                        which.append("</div>");
                        }
                    }
    
                    if (somereq) { 
                        ps.print(req + "</div>");
                    }

                    if (someopt) { 
                        ps.print(opt + "</div>");
                    }
                }

                /*
                 * Examples
                 */
                Tag[] examples = classdoc.tags(DTF_TAG_EXAMPLE);
                if (examples.length != 0) { 
                    ps.print("<div class='tag_examples_label'>Usage Examples</div>");
       
                    ps.print("<div class='tag_examples'>");
                    for (int e = 0; e < examples.length; e++) { 
                        Tag example = examples[e];
                        try { 
                            String text = example.text().trim();
                            
                            if (text.length() != 0) {
                                ps.print("<div class='tag_example_container'>");
                                ps.print("<div class='tag_example_title'>Example #" + (e+1) + "</div>");
                                ps.print("<div class='tag_example'>" + treatXML(text) + "</div>");
                                ps.print("</div>");
                            }
                        } catch (Exception exc) { 
                            throw new Exception("Error handling example #" + 
                                                (e+1) + " of tag " + tagname, exc);
                        }
                    }
                    ps.print("</div>");
                }
                
                ps.print("</body></html>");
                ps.close();
            }
        }
        
        /*
         * 2nd phase for Features
         * 
         */
        iter = fgroupnames.iterator();
        while (iter.hasNext()) {
            String pname = (String) iter.next();
            ArrayList<DTFDoc> tags = fgroups.get(pname);
            info(pname);
            for (int i = 0; i < tags.size(); i++) { 
                DTFDoc dtfdoc = tags.get(i);
                
                PrintStream ps = createFile(featuredir + 
                                            File.separatorChar + 
                                            dtfdoc.name.toLowerCase() + 
                                            ".html");
                
                info("Feature [" + dtfdoc.name + "]");
               
                ps.println("<html>");
                ps.println("    <head>");
                ps.println("    <link rel='stylesheet' " +
                                      "href='../main.css' " +
                                      "type='text/css'>");
                ps.println("        <title>" + dtfdoc.name + "</title>");
                ps.println("    </head>");
                ps.println("    <body id='body'>");
                ps.println("<div id='nav'>");
                ps.println("<a href='../index.html'>Home</a></div>");

                // Hiding the class name in the tooltip of the title :)
                ps.println("<div class='feature_title' title='" + dtfdoc.qname 
                           + "'>" + dtfdoc.name + "</div>");
                
                /*
                 * Description
                 */
                ps.print("<div class='feature_description'>");
                Tag[] descriptions = dtfdoc.descriptions;
                if (descriptions.length != 0) { 
                    for (int d = 0; d < descriptions.length; d++) { 
                        ps.print(treatTag(descriptions[d],tnames,fnames));
                    }
                }
                ps.print("</div>");
              
                /*
                 * Examples
                 */
                Tag[] examples = dtfdoc.examples;
                if (examples.length != 0) { 
                    ps.print("<div class='feature_examples_label'>Usage Examples</div>");
       
                    ps.print("<div class='feature_examples'>");
                    for (int e = 0; e < examples.length; e++) { 
                        Tag example = examples[e];
                        try { 
                            String text = example.text().trim();
                            
                            if (text.length() != 0) {
                                ps.print("<div class='feature_example_container'>");
                                ps.print("<div class='feature_example_title'>Example #" + (e+1) + "</div>");
                                ps.print("<div class='feature_example'>" + treatXML(text) + "</div>");
                                ps.print("</div>");
                            }
                        } catch (Exception exc) { 
                            throw new Exception("Error handling example #" + 
                                                (e+1) + " of tag " + dtfdoc.name,
                                                exc);
                        }
                    }
                    ps.print("</div>");
                }
                
                ps.print("</body></html>");
                ps.close();
            }
        }
        
        psIndex.print("</td></tr></table>");
        psIndex.print("</body></html>");
        psIndex.close();
      
        return true;
    }
    
    public static PrintStream createFile(String filename) throws Exception { 
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            throw new Exception("Unable to create destination folders.",e);
        }
        
        return new PrintStream(fos);
    }
    
    public static String treatTag(Tag tag,
                                  ArrayList<String> tnames,
                                  ArrayList<String> fnames) throws Exception { 
        StringBuffer result = new StringBuffer();
        
        Tag[] tags = tag.inlineTags();
       
        for (int i = 0; i < tags.length; i++) { 
            Tag aux = tags[i];
            if (aux.name().equalsIgnoreCase("text")) { 
                result.append(aux.text());
            } else if (aux.name().equals("@"+DTF_LINK)) { 
                String name = aux.text().trim().toLowerCase();
                String loc = null;
                
                if ( tnames.contains(name) ) { 
                    loc = "tags";
                } else if ( fnames.contains(name) ) { 
                    loc = "features";
                } else { 
                    throw new Exception("Unable to find link to [" + 
                                           name + "].");
                }
                
                result.append("<a href='../" + loc + "/" + name + 
                              ".html'>" + aux.text() + "</a>");
            } else if (aux.name().equals("@"+DTF_XML)) { 
                result.append(treatXML(aux.text()));
            }
        }
        
        return result.toString();
    }

    public static String treatXML(String string) throws Exception {
        try { 
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            
            string = string.replaceFirst("\\<\\?xml[^<^>]*\\?\\>","");
            ByteArrayInputStream bais = 
                                    new ByteArrayInputStream(string.getBytes());
            Document doc = docBuilder.parse(bais);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            serialize(doc,baos);
            return "<div class='xml_code'>" + baos.toString() + "</div>";
        } catch (Exception e) { 
            throw new Exception("Error processing XML node.",e);
        }
    }
    
    public static void serialize(Document doc, OutputStream out) throws Exception {
        TransformerFactory tfactory = TransformerFactory.newInstance();
        Transformer serializer;
        try {
            ByteArrayOutputStream aux = new ByteArrayOutputStream();
           
            // pretty print the XML
            FileInputStream fis = new FileInputStream("src/xsl/pretty_printing.xsl");
            Source xsl = new StreamSource(fis);
            serializer = tfactory.newTransformer(xsl);
            serializer.transform(new DOMSource(doc), new StreamResult(aux));
           
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            ByteArrayInputStream bais = new ByteArrayInputStream(aux.toByteArray());
            doc = docBuilder.parse(bais);
           
            // XML to HTML 
            fis = new FileInputStream("src/xsl/xmlverbatim.xsl");
            xsl = new StreamSource(fis);
            serializer = tfactory.newTransformer(xsl);
            serializer.transform(new DOMSource(doc), new StreamResult(out));
            
        } catch (TransformerException e) {
            throw new Exception("Error processing XML node.",e);
        }
    }
    
    public static int optionLength(String option) {
        
        if (option.equals("-d"))
            return 2;
        
        if (option.equals("-dtfxsd")) 
            return 2;
        
        return 0;
    }

    public static boolean validOptions(String options[][],
                                       DocErrorReporter reporter) {
        return true;
    }
    
    public static void info(String message) { 
        System.out.println(message);
    }
    
    public static void error(String message) { 
        System.err.println(message);
    }

    public static void warn(String message) { 
        System.out.println("WARN: " + message);
    }
}
