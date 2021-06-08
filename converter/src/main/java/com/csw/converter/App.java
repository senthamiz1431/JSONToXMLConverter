package com.csw.converter;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.csw.converter.converterfactory.ConverterFactory;
import com.csw.converter.converterimpl.JSONXMLConverterI;

public class App 
{
	
	private static Logger logger = Logger.getLogger(App.class.getName());
	
    public static void main( String[] args )
    {
    	
    	JSONXMLConverterI jsonToXmlConverter = new ConverterFactory().getConverter();
		try {
			String jsonFilepath = args[0];
			String xmlFilePath = args[1];
			
			if(jsonFilepath != null && new File(jsonFilepath).exists())
			{
				if(xmlFilePath != null && new File(xmlFilePath.substring(0, xmlFilePath.lastIndexOf(File.separator))).exists())
				{
					logger.log(Level.INFO, "JSONToXMLConverter starts \n JSON input : " + jsonFilepath + "\n XML output : " + xmlFilePath);
					
					jsonToXmlConverter.convertJSONtoXML(jsonFilepath, xmlFilePath);
					
					logger.log(Level.INFO, "JSONToXMLConverter ends");
				}
				else
				{
					logger.log(Level.SEVERE, "Invalid xml file path " + xmlFilePath);
				}
			}
			else
			{
				logger.log(Level.SEVERE, "Invalid json file path " + jsonFilepath);
			}
		}
		catch(Exception e) {
			logger.log(Level.SEVERE, "Error in converting JSON to XML : " + e.getMessage());
		}
		
    }
}
