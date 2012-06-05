package gov.lbl.glamm.server;


import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The GlammServlet class extends HttpServlet.  It handles all HTTP requests and responses.
 * @author  John Bates jtbates@lbl.gov
 */

@SuppressWarnings("serial")
public class GlammServlet extends HttpServlet {

	private static final String REQUEST_HANDLERS_XML_FILE_NAME	= "/config/request_handlers.xml";
	private static final String SERVER_CONFIG_XML_FILE_NAME		= "/config/server_config.xml";

	private RequestHandlerFactory rhFactory = null;

	/**
	 * Handles HTTP GET requests by handing them over to doPost
	 * @param	request		the HttpServletRequest
	 * @param 	response	the HttpServletResponse
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)     
	throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * Handles HTTP POST requests
	 * @param	request		the HttpServletRequest
	 * @param 	response	the HttpServletResponse
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)     
	throws ServletException, IOException {
		RequestHandler rh = rhFactory.createRequestHandler(request);
		if(rh != null)
			rh.handleRequest(request, response);
	}

	/**
	 * Sets up database connection pool and request handler factory classes
	 */
	@Override
	public void init()
	throws ServletException  {
		try {
			ServletContext sc = this.getServletContext();
			rhFactory = new RequestHandlerFactory(sc.getResource(REQUEST_HANDLERS_XML_FILE_NAME).toString());
			ConfigurationManager.init(sc.getResource(SERVER_CONFIG_XML_FILE_NAME).toString());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Tears down database connection pool
	 */
	@Override
	public void destroy() {
		GlammDbConnectionPool.destroy();
	}

}
