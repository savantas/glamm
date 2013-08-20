package gov.lbl.glamm.shared;

/**
 * <p>
 * FieldVerifier validates that the name the user enters is valid.
 * </p>
 * <p>
 * This class is in the <code>shared</code> packing because we use it in both
 * the client code and on the server. On the client, we verify that the name is
 * valid before sending an RPC request so the user doesn't have to wait for a
 * network round trip to get feedback. On the server, we verify that the name is
 * correct to ensure that the input is correct regardless of where the RPC
 * originates.
 * </p>
 * <p>
 * When creating a class that is used on both the client and the server, be sure
 * that all code is translatable and does not use native JavaScript. Code that
 * is note translatable (such as code that interacts with a database or the file
 * system) cannot be compiled into client side JavaScript. Code that uses native
 * JavaScript (such as Widgets) cannot be run on the server.
 * </p>
 */
public class FieldVerifier {

	public static boolean isLocalUri ( String uri, String domain ) {
		if ( uri == null || uri.trim().equals("") ) {
			return false;
		}

		uri = uri.trim();
		// Check domain if full url
		if ( uri.substring(0, 5).equalsIgnoreCase("http:")
				|| uri.substring(0, 6).equalsIgnoreCase("https:") ) {
			String[] uriTokens = uri.split("/");
			if ( uriTokens.length >= 3 ) {
				if ( uriTokens[2].equals(domain) ) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}

		// Disallow paths that don't start from root because the JS path might be unexpected
		if ( uri.charAt(0) == '/' ) {
			return true;
		}

		return false;
	}
}
