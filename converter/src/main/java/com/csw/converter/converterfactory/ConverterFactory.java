package com.csw.converter.converterfactory;

import com.csw.converter.converterimpl.JSONXMLConverterI;
import com.csw.converter.converterimpl.JSONXMLConverterImpl;

public class ConverterFactory
{
	/**
	 * This method returns the converter factory object
	 * @return
	 */
	public JSONXMLConverterI getConverter()
	{
		JSONXMLConverterImpl jsonXmlConvreterObj = new JSONXMLConverterImpl();
		return jsonXmlConvreterObj;
	}
	
}