/*
 * This work by WfMC is licensed under a Creative Commons Attribution 3.0 
 * Unported License.
 */
package org.bpsim.validator;

/**
 * 
 * @author tim.stephenson@knowprocess.com
 * 
 */
public class ValidationError {

	public enum ValidationLevel {
		WARN, ERROR, FATAL; 
	}

	private ValidationLevel level;

	private String message;

	public ValidationError() {

	}

	public ValidationError(ValidationLevel level, String message) {
		super();
		this.level = level;
		this.message = message;
	}

	public ValidationLevel getLevel() {
		return level;
	}

	public void setLevel(ValidationLevel level) {
		this.level = level;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
