package com.yahoo.dtf.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.yahoo.dtf.exception.DTFException;
import com.yahoo.dtf.exception.ParseException;

public class XSDHandler {
    
    public static long ATTRIB_REQUIRED = 0;
    public static long ATTRIB_OPTIONAL = 1;
    
    private Document _document = null;

    public XSDHandler(InputStream dtdIs) throws DTFException {
        try {
            _document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(dtdIs);
        } catch (IOException e) {
            throw new ParseException("error loading xsd file.", e);
        } catch (SAXException e) {
            throw new ParseException("error loading xsd file.", e);
        } catch (ParserConfigurationException e) {
            throw new ParseException("error loading xsd file.", e);
        }
    }
    
    private ArrayList<String> findAllAttributes(Node element) {
        ArrayList<String> result = new ArrayList<String>();
        Node aux = element.getFirstChild();
            
        while ( aux != null ) {
                
            if (aux.getNodeName().equals("xs:attribute")) { 
                NamedNodeMap attributes = aux.getAttributes(); 
                Node name = attributes.getNamedItem("name");
                if ( name != null ) {
                    result.add(name.getNodeValue());
                }
            } else if (aux.getNodeName().equals("xs:attributeGroup")) { 
                NamedNodeMap attributes = aux.getAttributes(); 
                Node name = attributes.getNamedItem("ref");
                if ( name != null ) {
                    Node group = findAttributeGroup(name.getNodeValue());
                    if ( group != null ) { 
                        result.addAll(findAllAttributes(group));
                    }
                }
            } else {
                result.addAll(findAllAttributes(aux));
            }
                
            aux = aux.getNextSibling();
        }
       
        return result;
    }
   
    public synchronized String generateChildrenString(String elementName) {
        elementName = elementName.toLowerCase();
        Node element = findElement(elementName);
        
        if (element != null) 
            return findAllDirectChildren(element," ",-1,false);
       
        return null;
    }
   
    private int SEQ = 0;
    private int CHO = 1;
   
    private boolean findAnyPreviousSibling(Node aux) { 
        while ( aux != null ) { 
            Node prev = aux.getPreviousSibling();
            
            if ( prev == null )
                break;
            
            if ( prev.getNodeType() == Node.ELEMENT_NODE )
                return true;
            
            aux = prev;
        }
        return false;
    }
    
    private String findAllDirectChildren(Node element,
                                         String sep,
                                         int prev,
                                         boolean flag) {
        StringBuffer result = new StringBuffer();
        Node aux = element.getFirstChild();
        
        while ( aux != null ) {
            if (aux.getNodeName().equals("xs:element")) { 
                NamedNodeMap attributes = aux.getAttributes(); 
                Node name = attributes.getNamedItem("name");
               
                String prefix = sep;
               
                /*
                 * If there is no previous element then we might as well not 
                 * put the prefix separator because it will just look wrong.
                 */
                if ( !findAnyPreviousSibling(aux) && !flag )
                    prefix = "";
                
                if ( name != null ) {
                    result.append(prefix + name.getNodeValue());
                } else { 
                    Node ref = attributes.getNamedItem("ref");
                    result.append(prefix + ref.getNodeValue());
                }
            } else if (aux.getNodeName().equals("xs:group")) { 
                NamedNodeMap attributes = aux.getAttributes(); 
                Node ref = attributes.getNamedItem("ref");

                String minOccurs = null;
                String maxOccurs = null;
                
                Node aux2 = attributes.getNamedItem("minOccurs");
                if ( aux2 != null ) minOccurs = aux2.getNodeValue();

                aux2 = attributes.getNamedItem("maxOccurs");
                if ( aux2 != null ) maxOccurs = aux2.getNodeValue();
                
                if ( maxOccurs != null && maxOccurs.equals("unbounded") )
                    maxOccurs = "*";
              
                String suffix = "";
                if ( minOccurs != null && maxOccurs != null ) { 
                    suffix = "{" + minOccurs + "," + maxOccurs + "} ";
                }
                
                if ( ref != null ) {
                    Node group = findGroup(ref.getNodeValue());
                    if ( group != null ) { 
                        result.append(findAllDirectChildren(group, sep, prev, true) + suffix);
                    }
                } else { 
                    result.append(findAllDirectChildren(aux, sep, prev, true) + suffix);
                }
            } else if (aux.getNodeName().equals("xs:sequence")) { 
                NamedNodeMap attributes = aux.getAttributes(); 
              
                String minOccurs = "1";
                String maxOccurs = "1";
                
                Node aux2 = attributes.getNamedItem("minOccurs");
                if ( aux2 != null ) minOccurs = aux2.getNodeValue();

                aux2 = attributes.getNamedItem("maxOccurs");
                if ( aux2 != null ) maxOccurs = aux2.getNodeValue();
                
                if ( maxOccurs.equals("unbounded") )
                    maxOccurs = "*";
             
                if ( prev == SEQ ) {
                    result.append(findAllDirectChildren(aux," . ",CHO,false));
                } else { 
	                if ( minOccurs.equals("1") && maxOccurs.equals("1") ) { 
		                result.append(findAllDirectChildren(aux, " . ", SEQ, false));
	                } else { 
		                result.append("(" + findAllDirectChildren(aux," . ", SEQ,false) + "){" + 
		                              minOccurs + "," + maxOccurs + "} ");
	                }
                }
            } else if (aux.getNodeName().equals("xs:choice")) { 
                NamedNodeMap attributes = aux.getAttributes(); 
              
                String minOccurs = "1";
                String maxOccurs = "1";
                
                Node aux2 = attributes.getNamedItem("minOccurs");
                if ( aux2 != null ) minOccurs = aux2.getNodeValue();

                aux2 = attributes.getNamedItem("maxOccurs");
                if ( aux2 != null ) maxOccurs = aux2.getNodeValue();

                if ( maxOccurs.equals("unbounded") )
                    maxOccurs = "*";
                
                if ( prev == CHO ) {
                    result.append(findAllDirectChildren(aux," | ",CHO,false));
                } else { 
	                if ( minOccurs.equals("1") && maxOccurs.equals("1") ) { 
		                result.append("(" + findAllDirectChildren(aux, " | ", CHO,false) + ") ");
	                } else { 
		                result.append("(" + findAllDirectChildren(aux, " | ", CHO,false) + "){" + 
		                              minOccurs + "," + maxOccurs + "} ");
	                }
                }
            } else { 
                result.append(findAllDirectChildren(aux,sep,prev,false));
            }
                
            aux = aux.getNextSibling();
        }
     
        return result.toString();
    }
    
    private ArrayList<String> findRequiredAttributes(Node element) {
        ArrayList<String> result = new ArrayList<String>();
        Node aux = element.getFirstChild();
            
        while ( aux != null ) {
            if (aux.getNodeName().equals("xs:attribute")) { 
                NamedNodeMap attributes = aux.getAttributes(); 
                Node name = attributes.getNamedItem("name");

                if ( name != null ) { 
	                Node attrib = attributes.getNamedItem("use");
	                                
	                if (attrib != null && attrib.getNodeValue().equals("required"))
	                    result.add(name.getNodeValue());
                }
            } else if (aux.getNodeName().equals("xs:attributeGroup")) { 
                NamedNodeMap attributes = aux.getAttributes(); 
                Node name = attributes.getNamedItem("ref");
                if ( name != null ) {
                    Node group = findAttributeGroup(name.getNodeValue());
                    if ( group != null ) { 
                        result.addAll(findRequiredAttributes(group));
                    }
                }
            } else {
                result.addAll(findRequiredAttributes(aux));
            }
                
            aux = aux.getNextSibling();
        }
       
        return result;
    }
    
    public synchronized ArrayList<String> getAttributes(String elementName) {
        ArrayList<String> result = null; 
       
        elementName = elementName.toLowerCase();
        Node element = findElement(elementName);
        
        if (element != null) { 
            result = findAllAttributes(element);
        }
       
        if (result != null && result.size() == 0) { 
            return null;
        }
        
        return result;
    }

    public synchronized ArrayList<String> getRequiredAttributes(String elementName) {
        ArrayList<String> result = null; 
       
        elementName = elementName.toLowerCase();
        Node element = findElement(elementName);
        
        if (element != null) { 
            result = findRequiredAttributes(element);
        }
       
        if (result != null && result.size() == 0) { 
            return null;
        }
        
        return result;
    }
    
    private Node findGroup(String elementName) {
        NodeList list = _document.getElementsByTagName("xs:group");
        for (int i = 0; i < list.getLength(); i++) { 
            NamedNodeMap attributes = list.item(i).getAttributes();
            if (attributes != null) { 
                Node attribute = attributes.getNamedItem("name");
                if (attribute != null && 
                    attribute.getNodeValue().equals(elementName)) { 
                    return list.item(i);
                }
            }
        }
        return null;
    }
    
    private Node find(String name, String elementName) {
        NodeList list = _document.getElementsByTagName(name);
        for (int i = 0; i < list.getLength(); i++) { 
            NamedNodeMap attributes = list.item(i).getAttributes();
            if (attributes != null) { 
                Node attribute = attributes.getNamedItem("name");
                if (attribute != null && 
                    attribute.getNodeValue().equals(elementName)) { 
                    return list.item(i);
                }
            }
        }
        return null;
    }
    
    private Node findElement(String elementName) { 
        // have to make sure to get the top level elements, not any other 
        // elements by the same name at an inner level of the XSD tree.
        NodeList list = _document.getElementsByTagName("xs:schema").item(0).
                        getChildNodes();
        
        for (int i = 0; i < list.getLength(); i++) { 
            Node aux = list.item(i);
            if ( aux.getNodeName().equals("xs:element")) { 
	            NamedNodeMap attributes = aux.getAttributes();
	            if (attributes != null) { 
	                Node attribute = attributes.getNamedItem("name");
	                if (attribute != null && 
	                    attribute.getNodeValue().equals(elementName)) { 
	                    return list.item(i);
	                }
	            }
            }
        }
        return null;
    }
    
    private Node findAttributeGroup(String name) { 
        NodeList list = _document.getElementsByTagName("xs:attributeGroup");
        for (int i = 0; i < list.getLength(); i++) { 
            NamedNodeMap attributes = list.item(i).getAttributes();
            if (attributes != null) { 
                Node attribute = attributes.getNamedItem("name");
                if (attribute != null && 
                    attribute.getNodeValue().equals(name)) { 
                    return list.item(i);
                }
            }
        }
        return null;
    }
}