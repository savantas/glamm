package gov.lbl.glamm.client.map.exceptions;

/**
 * This extends GlammException to deal specifically with passing Server-side exceptions to the invoking Client-side class.
 * Mostly, it'll be used with KBase-specific RPC calls that fail. Thus, this is (mostly) a lightweight wrapper around
 * a generic Exception. It also allows for extension into more specific exception types, for example, UnauthorizedException.
 * @author wjriehl
 *
 */

@SuppressWarnings("serial")
public class GlammRPCException extends GlammException {

	public GlammRPCException() {
		super();
	}
	
	public GlammRPCException(Exception e) {
		super(e);
	}
	
	public GlammRPCException(String s) {
		super(s);
	}
}
