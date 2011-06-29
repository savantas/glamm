package gov.lbl.glamm.server;


import gov.lbl.glamm.shared.RequestParameters;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Factory class for request handlers
 * @author jtbates
 *
 */
public class RequestHandlerFactory {
	
	private final String ATTR_ACTION						= "action";
	private final String ATTR_REQUEST_HANDLER_CLASS_NAME 	= "class";

	private final String TAG_REQUEST_HANDLER				= "RequestHandler";
	
	/**
	 * Private class storing request handler configuration parameters
	 * @author jtbates
	 *
	 */
	private class RequestHandlerElement {
		
		public String	action						= "";
		public String	requestHandlerClassName 	= "";
		
		public RequestHandlerElement(Element rhParamsElement) {
			action					= rhParamsElement.getAttribute(ATTR_ACTION);
			requestHandlerClassName = rhParamsElement.getAttribute(ATTR_REQUEST_HANDLER_CLASS_NAME);
		}
	}

	private final Map<String, RequestHandlerElement> action2RequestHandlerElements = new HashMap<String, RequestHandlerElement>();

	/**
	 * Constructor 
	 * @param xmlFileName	the name of the file defining the action-request handler mapping 
	 */
	public RequestHandlerFactory(String xmlFileName) {
		parseFile(xmlFileName);
	}

	/**
	 * Factory method - creates a RequestHandler that corresponds with the action specified
	 * @param request	The request 
	 * @return			A RequestHandler object
	 */
	public RequestHandler createRequestHandler(HttpServletRequest request) {
		
		RequestHandler rh 			= null;
		RequestHandlerElement rhp 	= null;
		String action 				= request.getParameter(RequestParameters.ACTION.toString());
		
		if(action != null)
			rhp = action2RequestHandlerElements.get(action);
		
		if(rhp != null) {
			try {
				Class<?> rhClass = Class.forName(rhp.requestHandlerClassName);
				rh = (RequestHandler) rhClass.newInstance();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return rh;
	}
	
	/**
	 * Parses the xml file defining the action-request handler mapping
	 * @param uri
	 */
	private void parseFile(String requestHandlersUri) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(requestHandlersUri);
			
			NodeList nodeList = doc.getElementsByTagName(TAG_REQUEST_HANDLER);
			for(int i = 0; i < nodeList.getLength(); i++) {
				Element element = (Element) nodeList.item(i);
				RequestHandlerElement rhp = new RequestHandlerElement(element);
				action2RequestHandlerElements.put(rhp.action, rhp);
			}

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
