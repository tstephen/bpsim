/*
 * This work by WfMC is licensed under a Creative Commons Attribution 3.0 
 * Unported License.
 */
package org.bpsim.validator;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author tim.stephenson@knowprocess.com
 * 
 */
public class ImplementersGuideTest {

	private Validator validator;

	@Before
	public void setUp() throws Exception {
		validator = new Validator();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCarRepairProcessBpmn() {
		List<ValidationError> errors = validator
				.validate("/Car Repair Process v0.17.bpmn");
		// ONE warning expected as not currently registering BPMN schemas.
		assertEquals(1, errors.size());
	}

	@Test
	public void testCarRepairProcessXpdl() {
		List<ValidationError> errors = validator
				.validate("/Car Repair Process v0.17.xpdl");
		// ONE warning expected as not currently registering XPDL schemas.
		assertEquals(1, errors.size());
	}

	@Test
	public void testLoanProcessBpmn() {
		List<ValidationError> errors = validator
				.validate("/Loan Process v0.17.bpmn");
		// ONE warning expected as not currently registering BPMN schemas.
		assertEquals(1, errors.size());
	}

	@Test
	public void testLoanProcessXpdl() {
		List<ValidationError> errors = validator
				.validate("/Loan Process v0.17.xpdl");
		// ONE warning expected as not currently registering XPDL schemas.
		assertEquals(1, errors.size());
	}

	@Test
	public void testTechnicalSupportBpmn() {
		List<ValidationError> errors = validator
				.validate("/Technical Support Process v0.17.bpmn");
		// ONE warning expected as not currently registering BPMN schemas.
		assertEquals(1, errors.size());
	}

	@Test
	public void testTechnicalSupportXpdl() {
		List<ValidationError> errors = validator
				.validate("/Technical Support Process v0.17.xpdl");
		// ONE warning expected as not currently registering XPDL schemas.
		assertEquals(1, errors.size());
	}
}
