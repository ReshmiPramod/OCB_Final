package com.smartbuy.ocb.web.util;



import org.apache.log4j.Logger;

import com.smartbuy.ocb.dto.SkuDto;
import com.smartbuy.ocb.exceptions.OcbException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;


public class XMLTransformer {

	private static final Logger logger  = Logger.getLogger(XMLTransformer.class);
	 
//	private static final XStream xstream = new XStream(){
	private static final XStream xstream = new XStream(new JsonHierarchicalStreamDriver()){
		
		{
			autodetectAnnotations(true);
			processAnnotations(SkuDto.class);
		}
	};
  private static final XStream fromJson = new XStream(new JettisonMappedXmlDriver());
 // {
//	  
//	  {
//			autodetectAnnotations(true);
//			processAnnotations(SkuDto.class);
//			
//		}
//  }; 
  

	/**
	 * toXML - Converts given bean to XML
	 * @param Object
	 * @return String XML value created from given Bean object
	 * @throws XMLParsingException
	 */
	public static String toXML(Object obj) throws OcbException{
		String xml = null;
		try {
	        xml = xstream.toXML(obj);
		} catch (Exception e) {
			logger.error("XMLParsingException :: " +e.getMessage(), e);
			throw new OcbException(e);
		}
		return xml;
	}
	/**
	 * fromXML - converts given XML to bean
	 * @param xml
	 * @return bean object 
	 * @throws XMLParsingException
	 */
	public static Object fromXML(String json) throws OcbException {
		
		Object obj = null;
		try {
	        obj =  fromJson.fromXML(json);
		} catch (Exception e) {
			logger.error("XMLParsingException :: " +e.getMessage(), e);
			throw new OcbException(e);
		}
		return obj;
	}
}
