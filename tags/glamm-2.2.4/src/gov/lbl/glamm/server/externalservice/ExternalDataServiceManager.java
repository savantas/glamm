package gov.lbl.glamm.server.externalservice;

import gov.lbl.glamm.shared.ExternalDataService;
import gov.lbl.glamm.shared.ExternalServiceParameter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Manager for external (i.e. non-GLAMM or MO database, but served through another web service like RegPrecise,
 * and eventually various KBase services) services. This will load in a services XML file that should have the
 * following format:
 * 
 * <DataServices>
 *     <Service name="service name" url="http://service_url.com/Service">
 *         <Parameter name="User-friendly param name" extName="machineReadableName" />
 *         <Parameter name="Another parameter" extName="anotherParam" />
 *     </Service>
 * </DataServices>
 *
 * Currently, it expects to append all parameters and values in a URL-encoded format. EG. from above:
 *     http://service_url.com/Service?machineReadableName=XXX&anotherParam=YYY
 *
 * TODO:
 *     - Some kind of more explicitly RESTful tags should be included later, as necessary.
 *     - Even better, it should include a simple paradigm for how the url should be constructed.
 * 
 * @author wjriehl
 *
 */
public class ExternalDataServiceManager {
	
	private static final String TAG_SERVICE			  = "Service";
	private static final String ATTR_SERVICE_NAME	  = "name";			// Human-readable name of the service
	private static final String ATTR_SERVICE_URL	  = "url";			// URL of the external service
	private static final String ATTR_SERVICE_PARSER	  = "parser";		// class name of the parser for retrieved data
	private static final String ATTR_SERVICE_ABBREV	  = "abbreviation";
	
	private static final String TAG_PARAMETER		  = "Parameter";
	private static final String ATTR_PARAM_NAME 	  = "displayName";  // Human-readable name of a parameter
	private static final String ATTR_PARAM_URL_NAME   = "extUrlName";	// External URL id of the parameter
	private static final String ATTR_PARAM_STATE_NAME = "stateUrlName"; // GLAMM state URl id of the parameter.
	private static final String ATTR_PARAM_VISIBILITY = "visibility";
	private static final String ATTR_PARAM_DEFAULT	  = "default";
	private static final String ATTR_PARAM_TYPE		  = "type";
	
	private static Map<String, ExternalDataService> name2DataService;
	private static List<ExternalDataService> dataServices;

	static {
		name2DataService = new HashMap<String, ExternalDataService>();
		dataServices = new ArrayList<ExternalDataService>();
	}
	
	/**
	 * Initializes the list of services from the XML file at the given URI.
	 * @param uri the location of the services file
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static void init(final String uri) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(uri);
		
		// Get all the <Service> tags
		NodeList nodeList = doc.getElementsByTagName(TAG_SERVICE);
		for(int i = 0; i < nodeList.getLength(); i++) {
			Element serviceElem = (Element) nodeList.item(i);
			
			// Extract the service name and URL
			String serviceName = serviceElem.getAttribute(ATTR_SERVICE_NAME);
			String url = serviceElem.getAttribute(ATTR_SERVICE_URL);
			String parser = serviceElem.getAttribute(ATTR_SERVICE_PARSER);
			String abbrev = serviceElem.getAttribute(ATTR_SERVICE_ABBREV);
			
			// Get all the parameter identifiers
			List<ExternalServiceParameter> parameters = new ArrayList<ExternalServiceParameter>();
			NodeList paramList = serviceElem.getElementsByTagName(TAG_PARAMETER);
			for (int j = 0; j < paramList.getLength(); j++) {
				Element paramElem = (Element) paramList.item(j);
				
				@SuppressWarnings("unused")
				String visible = paramElem.getAttribute(ATTR_PARAM_VISIBILITY);
				@SuppressWarnings("unused")
				String type = paramElem.getAttribute(ATTR_PARAM_TYPE);
				
				ExternalServiceParameter param = new ExternalServiceParameter();
				param.setHumanReadableName(paramElem.getAttribute(ATTR_PARAM_NAME));
				param.setExternalUrlName(paramElem.getAttribute(ATTR_PARAM_URL_NAME));
				param.setStateUrlName(paramElem.getAttribute(ATTR_PARAM_STATE_NAME));
				param.setVisibilityFromString(paramElem.getAttribute(ATTR_PARAM_VISIBILITY));
				param.setTypeFromString(paramElem.getAttribute(ATTR_PARAM_TYPE));
				
				String value = paramElem.getAttribute(ATTR_PARAM_DEFAULT);
				if (value != null)
					param.setValue(value);

				parameters.add(param);
			}
			
			ExternalDataService service = new ExternalDataService(serviceName, abbrev, url, parser, parameters);
			name2DataService.put(serviceName, service);
			name2DataService.put(abbrev, service);
			dataServices.add(service);
		}
	}
	
	/**
	 * Returns a service with the given name, if such a service is loaded.
	 * @param name the service name
	 * @return the OverlayDataService if it exists, null otherwise
	 */
	public static ExternalDataService getServiceFromName(String name) {
		if (name2DataService.containsKey(name))
			return name2DataService.get(name);
		else
			return null;
		
		//TODO make it return an empty service on null?
		// Or maybe throw some kind of exception to be caught and handled?
	}
	
	/**
	 * Returns information about all loaded services as a Map where keys are service names, and
	 * values are the lists of user-readable parameter names.
	 * @return
	 */
	public static List<ExternalDataService> getAvailableServiceInformation() {
		return dataServices;
	}
}