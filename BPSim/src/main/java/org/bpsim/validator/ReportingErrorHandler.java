/*
 * This work by WfMC is licensed under a Creative Commons Attribution 3.0 
 * Unported License.
 */
package org.bpsim.validator;

import java.util.ArrayList;
import java.util.List;

import org.bpsim.validator.ValidationError.ValidationLevel;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * 
 * @author tim.stephenson@knowprocess.com
 * 
 */
public class ReportingErrorHandler implements ErrorHandler {

	private List<ValidationError> errors = new ArrayList<ValidationError>();

    public void warning(SAXParseException e) {
        System.out.println(e.getMessage());
		errors.add(new ValidationError(ValidationLevel.WARN, e.getMessage()));
    }

    public void error(SAXParseException e) {
        System.out.println(e.getMessage());
		errors.add(new ValidationError(ValidationLevel.ERROR, e.getMessage()));
    }

    public void fatalError(SAXParseException e) {
        System.out.println(e.getMessage());
		errors.add(new ValidationError(ValidationLevel.FATAL, e.getMessage()));
    }

	public List<ValidationError> getErrors() {
		return errors;
	}
}