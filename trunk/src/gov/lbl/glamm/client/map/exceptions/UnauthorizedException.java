package gov.lbl.glamm.client.map.exceptions;

@SuppressWarnings("serial")
public class UnauthorizedException extends GlammException {

	public UnauthorizedException() {
		super();
	}
	
	public UnauthorizedException(Exception e) {
		super(e);
	}
	
	public UnauthorizedException(String s) {
		super(s);
	}
	
}
