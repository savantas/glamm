package gov.lbl.glamm.server;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface for non-RPC (i.e. file upload and form) requests.
 * @author jtbates
 *
 */
public interface RequestHandler {	
	/**
	 * Handles a request.
	 * @param request The request.
	 * @param response The response.
	 * @throws IOException
	 */
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
