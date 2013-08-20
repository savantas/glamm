package gov.lbl.glamm.shared.exceptions;

import java.io.Serializable;

/**
 * A generic Exception class specific to GLAMM.
 * All GLAMM-specific exceptions (mostly exceptions in RPC calls) are extended by this,
 * when it's appropriate to use them. As usual, it's best to use the right exception for
 * the job. You wouldn't use a GlammException when the code tries to parse a number out of
 * null, right?
 * @author wjriehl
 *
 */
@SuppressWarnings("serial")
public class GlammException extends Exception implements Serializable {

	public GlammException() {
		super();
	}
	
	public GlammException(Exception e) {
		super(e);
	}
	
	public GlammException(String s) {
		super(s);
	}
}
