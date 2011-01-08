package com.yahoo.dtf.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.yahoo.dtf.plugin.JarUtil;
import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.init.JarFileFilter;
import com.yahoo.dtf.logger.DTFLogger;

/**
 * Class used by build.xml to add plug-ins to existing DTF framework. This class
 * is responsible for merging XSDs and any other content that is necessary to 
 * make a plug-in part of the DTF framework.
 * 
 * @author Rodney Gomes
 *
 */
public class PluginMerger {
    
    private static DTFLogger _logger = DTFLogger.getLogger(PluginMerger.class);
    
    public static void printUsage() { 
        _logger.info(" PluginMerger ");
        _logger.info("**************");
        _logger.info(" PluginMerger plugin_directory dtf_xsd_location build_location");
    }
    
    private static Node findFirstChild(Node parent, String element) { 
        Node child = parent.getFirstChild();
        while (child != null) { 
            String name = child.getLocalName();
            if (name != null && name.equals(element)) {
                return child;
            }
            child = child.getNextSibling();
        } 
        return null;
    }
   
    public static void main(String[] args) {
        
        if (args.length != 3) { 
            printUsage();
            return;
        }
       
        try { 
            
            String pluginLocation = args[0];
            String dtfFilename = args[1];
            String buildLocation = args[2];
          
            _logger.info("outputting to " + buildLocation + "/dtf.xsd");
            FileOutputStream fos = new FileOutputStream(buildLocation + "/dtf.xsd");
            
            File lib = new File(pluginLocation + File.separatorChar + "lib");
            DOMParser parser = new DOMParser();
        
            FileInputStream fis = new FileInputStream(dtfFilename);
            InputSource is = new InputSource(fis);
            parser.parse(is);
            Document dtfDoc = parser.getDocument();
            Element dtfRoot  = dtfDoc.getDocumentElement();
            
            // Get all jar files in the directories lib directory.
            File[] jars = lib.listFiles(new JarFileFilter());
            _logger.info("Looking for jars in: " + lib.getAbsolutePath());
            
            //* Get the jar file and figure out if there is a XSD to be merged.
            for(int i = 0; i < jars.length; i++) { 
                // Check for the XSDFile property value from the Manifest
                File pluginJar = jars[i].getAbsoluteFile();
                String xsdFilename = 
                        JarUtil.getXSDPropertyValue(pluginJar, 
                                                    XSDConstants.XSD_FILE_PROPERTY); 
                    
                /*
                 * There is an xsd file in this jar file which means this is a DTF 
                 * plugin that needs to be merged with.
                 */
                if (xsdFilename != null) { 
                    _logger.info("Handling " + xsdFilename);
                  
                    InputStream rIS = JarUtil.getXSDInputStream(pluginJar);
                    InputSource ris = new InputSource(rIS);
                    parser.parse(ris);
                    Element pluginRoot = parser.getDocument().getDocumentElement();
                    
                    /*
                     * Merge the groups of the same name and throw an exception if
                     * conflicting elements are found.
                     */
                    Node pNode = pluginRoot.getFirstChild();
                    while (pNode != null) { 
                        /*
                         * Ignore comments or anything other than an xml element
                         * node.
                         */
                        if (isElementNode(pNode)) { 
                            NamedNodeMap pAttribs = pNode.getAttributes();
                            Node pAttrib = pAttribs.getNamedItem("name");
                            String pName = pAttrib.getNodeValue();
                            
                            Node rNode = dtfRoot.getFirstChild();
                            boolean merged = false;
                            do { 
                                // not element node then skip it.
                                if (!isElementNode(rNode))
                                    continue;
                                
                                NamedNodeMap rAttribs = rNode.getAttributes();
                                Node rAttrib = rAttribs.getNamedItem("name");
                                  
                                if (rAttrib == null)  
                                    throw new DTFException("element without 'name' attribute, [" + rNode + "]");
                                        
                                String rName = rAttrib.getNodeValue();
                                String localName = pNode.getLocalName();
                                
                                // didn't find a plugin node with the same name
                                // then skip it.
                                if (!pName.equals(rName))
                                    continue;
                                    
                                if (!localName.equals("group")) {  
                                    String msg = "Unsupported merging " + 
                                                 "of XSD elements [" + 
                                                 localName + "]";
                                    throw new DTFException(msg);
                                }
                                    
                                _logger.info("Merging common [" + pName + "]");
                                /*
                                 * merge the two common group elements that 
                                 * should have 1 choice child with the various
                                 * XML elements to merge.
                                 */
                               
                                /*
                                 * Find choice element from DTF XSD group.
                                 */
                                Node child = null;
                                
                                Node rChoice = findFirstChild(rNode, "choice"); 
                                Node choice = findFirstChild(pNode, "choice"); 
                                
                                if ( choice != null ) { 
                                    child = choice.getFirstChild();
                                   
                                    /*
                                     * XXX: need a cleaner merging algorithm
                                     *      than the current one.
                                     */
                                    
                                    /*      
                                     * If they're both sequences then we should 
                                     * merge those two. 
                                     */
                                    Node seqr = findFirstChild(rChoice,"sequence");
                                    Node seqp = findFirstChild(choice, "sequence");
                                    if (seqp != null && seqr != null) {
                                        child   = seqp.getFirstChild(); 
                                        rChoice = seqr;
                                    }
                                    
                                    while (child != null) { 
                                        Node copy = dtfDoc.importNode(child, true);
                                        rChoice.appendChild(copy);
                                        child = child.getNextSibling();
                                    }
                                    
                                } else { 
                                    throw new DTFException("Group " + pNode + 
                                           " does not have a xs:choice child!");
                                }
                                merged = true;
                                
                            } while ((rNode = rNode.getNextSibling()) != null);
                                
                            if (!merged) { 
                                _logger.info("Adding element [" + 
                                             pNode.getLocalName() + 
                                             "] " + pName);
                                    
                                Node newNode = dtfDoc.importNode(pNode, true);
                                dtfDoc.getFirstChild().appendChild(newNode);
                            }
                        }
                        
                        pNode = pNode.getNextSibling();
                    }
                }
            }
           
            // Write the DOM document to the file
            Transformer xformer = 
                              TransformerFactory.newInstance().newTransformer();
            Source source = new DOMSource(dtfDoc);
            xformer.transform(source, new StreamResult(fos));
            
        } catch (IOException e) { 
            _logger.error("Error parsing xsd files.",e);
            System.exit(-1);
        } catch (SAXException e) {
            _logger.error("Error parsing xsd files.",e);
            System.exit(-1);
        } catch (DTFException e) {
            _logger.error("Error parsing xsd files.",e);
            System.exit(-1);
        } catch (TransformerException e) {
            _logger.error("Error writing out xsd file.",e);
            System.exit(-1);
        }
    }
    
    private static boolean isElementNode(Node node) { 
        return node.getNodeType() == Node.ELEMENT_NODE && 
               node.getAttributes() != null;
    }
    
}
