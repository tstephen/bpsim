/*
 * This work by WfMC is licensed under a Creative Commons Attribution 3.0 
 * Unported License.
 */
package org.bpsim.validator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * 
 * @author tim.stephenson@knowprocess.com
 * 
 */
public class Validator {

	public List<ValidationError> validate(String bpmnFile) {
		InputStream is = null ; 
		InputStream xsdStream = null;
		ReportingErrorHandler errHandler = new ReportingErrorHandler();
		try {
			is = Validator.class
					.getResourceAsStream(bpmnFile);
			xsdStream = Validator.class.getResourceAsStream("/bpsim-1.0.xsd");
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(true);

			SchemaFactory schemaFactory = SchemaFactory
					.newInstance("http://www.w3.org/2001/XMLSchema");

			factory.setSchema(schemaFactory
					.newSchema(new Source[] { new StreamSource(
							new InputStreamReader(xsdStream)) }));

			SAXParser parser = factory.newSAXParser();

			XMLReader reader = parser.getXMLReader();

			reader.setErrorHandler(errHandler);
			reader.parse(new InputSource(new InputStreamReader(is)));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return errHandler.getErrors();
	}
}
