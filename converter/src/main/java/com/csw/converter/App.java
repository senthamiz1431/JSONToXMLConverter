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
			if(args[0] != null && new File(args[0]).exists())
			{
				if(args[1] != null && new File(args[1].substring(0, args[1].lastIndexOf(File.separator))).exists())
				{
					logger.log(Level.INFO, "JSON To XML Conversion starts \n JSON input : " + args[0] + "\n XML output : " + args[1]);
					
					jsonToXmlConverter.convertJSONtoXML(args[0], args[1]);
					
					logger.log(Level.INFO, "JSONXMLConverter ends");
				}
				else
				{
					logger.log(Level.SEVERE, "Invalid xml file path " + args[1]);
				}
			}
			else
			{
				logger.log(Level.SEVERE, "Invalid json file path " + args[0]);
			}
		}
		catch(Exception e) {
			logger.log(Level.SEVERE, "Error in converting JSON to XML : " + e.getMessage());
		}
		
    }
}
