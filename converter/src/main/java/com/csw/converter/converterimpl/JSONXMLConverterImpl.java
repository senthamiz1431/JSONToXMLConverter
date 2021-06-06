package com.csw.converter.converterimpl;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class converts the JSON input to XML output.
 *
 */
public class JSONXMLConverterImpl implements JSONXMLConverterI
{
	private Logger logger = Logger.getLogger(JSONXMLConverterImpl.class.getName());
	private Properties p=new Properties();

	
	/**
	 * convert JSON to XML
	 * Using DOM Create a new document
	 * Read the constants from the properties file
	 * Read JSON file for processing 
	 * Write document to the xmlFile
	 * Input = JSON file path and output file path
	 */
	public void convertJSONtoXML(String jsonFile, String xmlFile)
	{
		try 
		{
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			
			FileReader reader=new FileReader("constants.properties");  
		    p.load(reader);  
			
		    readFromJsonFile(jsonFile, document);
			
			writeToXmlFile(document, xmlFile);	
		}
		catch(Exception e)
		{
			logger.log(Level.SEVERE, "Error in converting JSON to XML : " + e.getMessage());
		}
	}
	
	/**
	 * This method read the JSON file
	 * @param jsonFile
	 * @param document
	 */
	private void readFromJsonFile(String jsonFile, Document document)
	{
		logger.log(Level.INFO, "readJsonFile starts");
		try
		{
			ObjectMapper Objmapper =new ObjectMapper();
			Map<?, ?> jsonMap = Objmapper.readValue(new File(jsonFile), Map.class);
		    if(jsonMap != null && jsonMap.size() > 0)
		    {
		    	Element root = document.createElement(p.getProperty("OBJECT_ELEMENT"));
		 	    document.appendChild(root);
		 		processObjectElement(jsonMap, document, root);
		    }
		    else
		    {
		    	throw new Exception("Invalid json file" + jsonFile);
		    }
		}
		catch(Exception e)
		{
			logger.log(Level.SEVERE, "Error reading JSON file : " + e.getMessage());
		}
		logger.log(Level.INFO, "readJsonFile ends");
	}
	
	/**
	 * This method process the Map and its child elements.
	 * @param jsonMap
	 * @param document
	 * @param rootElement
	 */
	private void processObjectElement(Map<?, ?> jsonMap, Document document, Element rootElement)
	{
		for(Entry<?, ?> jsonObj : jsonMap.entrySet())
		{	
			if(jsonObj.getValue() instanceof Map)
			{	
				Element mapElement = createObjectElement(jsonObj, document, rootElement, p.getProperty("OBJECT_ELEMENT"));
				processObjectElement((Map<?, ?>) jsonObj.getValue(), document, mapElement);
			}
			else if(jsonObj.getValue() instanceof List)
			{
				Element listElement = createObjectElement(jsonObj, document, rootElement, p.getProperty("ARRAY_ELEMENT"));
				processArrayElements((List<?>) jsonObj.getValue(), document, listElement);
			}
			else if(jsonObj.getValue() instanceof Number)
			{
				createObjectElement(jsonObj, document, rootElement, p.getProperty("NUMBER_ELEMENT"));
			} 
			else if(jsonObj.getValue() instanceof Boolean) 
			{
				createObjectElement(jsonObj, document, rootElement, p.getProperty("BOOLEAN_ELEMENT"));
			} 
			else if(jsonObj.getValue() instanceof String)
			{
				createObjectElement(jsonObj, document, rootElement, p.getProperty("STRING_ELEMENT"));
			}
			else 
			{
				createObjectElement(jsonObj, document, rootElement, p.getProperty("NULL_ELEMENT"));
			}
		}
	}
	
	/**
	 * This method process the JSON array to form the required document
	 * @param jsonArray
	 * @param document
	 * @param rootElement
	 */
	private void processArrayElements(List<?> jsonArray, Document document, Element rootElement)
	{
		for(Object jsonObj : jsonArray)
		{
			if(jsonObj instanceof Map) {
				Element mapElement = createArrayElement(jsonObj, document, rootElement, p.getProperty("OBJECT_ELEMENT"));
				processObjectElement((Map<?, ?>) jsonObj, document, mapElement);
			}
			else if(jsonObj instanceof List)
			{
				Element listElement = createArrayElement(jsonObj, document, rootElement, p.getProperty("ARRAY_ELEMENT"));
				processArrayElements((List<?>) jsonObj, document, listElement);
			} 
			else if(jsonObj instanceof Number) 
			{
				createArrayElement(jsonObj, document, rootElement, p.getProperty("NUMBER_ELEMENT"));
			} 
			else if(jsonObj instanceof Boolean) 
			{
				createArrayElement(jsonObj, document, rootElement, p.getProperty("BOOLEAN_ELEMENT"));
			}
			else if(jsonObj instanceof String)
			{
				createArrayElement(jsonObj, document, rootElement, p.getProperty("STRING_ELEMENT"));
			} 
			else 
			{
				createArrayElement(jsonObj, document, rootElement, p.getProperty("NULL_ELEMENT"));
			}
		}
	}
	
	
	
	/**
	 * This method creates element(tag) for object element and creates attribute for Object element.
	 * @param jsonObj
	 * @param document
	 * @param rootElement
	 * @param strElement
	 * @return
	 */
	private Element createObjectElement(Entry<?, ?> jsonObj, Document document, Element rootElement, String strChildElement)
	{
		Element childElement = document.createElement(strChildElement);
		
		if(jsonObj.getKey() != null)
		{
			Attr attr = document.createAttribute(p.getProperty("ATTR_NAME"));
			attr.setValue(jsonObj.getKey().toString());
			
			childElement.setAttributeNode(attr);
		}
		
		if(jsonObj.getValue() != null && (!strChildElement.equals(p.getProperty("OBJECT_ELEMENT")) 
				&& !strChildElement.equals(p.getProperty("ARRAY_ELEMENT"))))
		{
			childElement.appendChild(document.createTextNode(jsonObj.getValue().toString()));
		}
		
		rootElement.appendChild(childElement);
		
		return childElement;
	}
	

	/**
	 * This method create elements(tag) for array elements. 
	 * @param jsonObj
	 * @param document
	 * @param rootElement
	 * @param strChildElement
	 * @return
	 */
	private Element createArrayElement(Object jsonObj, Document document, Element rootElement, String strChildElement)
	{
		Element childElement = document.createElement(strChildElement);
		
		rootElement.appendChild(childElement);
		
		if(jsonObj != null && !strChildElement.equals(p.getProperty("OBJECT_ELEMENT")) 
				&& !strChildElement.equals(p.getProperty("ARRAY_ELEMENT")))
		{
			childElement.appendChild(document.createTextNode(jsonObj.toString()));
		}
		
		return childElement;
	}
	
	
	/**
	 * This method writes the document to XMLfile
	 * @param document
	 * @param xmlFile
	 */
	private void writeToXmlFile(Document document, String xmlFile)
	{
		logger.log(Level.INFO, "writeXmlFile starts");
		try
		{
			StreamResult streamResult = new StreamResult(new File(xmlFile));
	        
	        TransformerFactory.newInstance().newTransformer()
	        	.transform(new DOMSource(document), streamResult);
		}
		catch(Exception e)
		{
			logger.log(Level.SEVERE, "Error while writing the data to XML" + e.getMessage());
		}
		logger.log(Level.INFO, "writeXmlFile ends");
	}
	
}