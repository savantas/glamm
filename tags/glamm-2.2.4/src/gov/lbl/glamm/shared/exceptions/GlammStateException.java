package gov.lbl.glamm.shared.exceptions;

@SuppressWarnings("serial")
public class GlammStateException extends GlammRPCException {
	public GlammStateException() {
		super();
	}
	
	public GlammStateException(Exception e) {
		super(e);
	}
	
	public GlammStateException(String s) {
		super(s);
	}

}
