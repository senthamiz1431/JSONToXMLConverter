package com.csw.converter.converterfactory;

import com.csw.converter.converterimpl.JSONXMLConverterI;
import com.csw.converter.converterimpl.JSONXMLConverterImpl;

public class ConverterFactory
{
	public JSONXMLConverterI getConverter()
	{
		return new JSONXMLConverterImpl();
	}
	
}