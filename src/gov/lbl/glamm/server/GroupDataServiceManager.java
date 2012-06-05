package gov.lbl.glamm.server;

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
 * Currently, it expects to append all parameters and values in a CGI service call format. EG. from above:
 *     http://service_url.com/Service?machineReadableName=XXX&anotherParam=YYY
 *
 * TODO:
 *     - Some kind of more explicity RESTful tags should be included later, as necessary.
 *     - Even better, it should include a simple paradigm for how the url should be constructed.
 * 
 * @author wjriehl
 *
 */
public class GroupDataServiceManager {
	
	private static final String TAG_SERVICE 		= "Service";
	private static final String ATTR_SERVICE_NAME 	= "name";
	private static final String ATTR_SERVICE_URL 	= "url";
	
	private static final String TAG_PARAMETER 		= "Parameter";
	private static final String ATTR_PARAM_NAME 	= "name";
	private static final String ATTR_PARAM_URL_NAME = "extName";
	
	private static Map<String, GroupDataService> name2DataService;

	static {
		name2DataService = new HashMap<String, GroupDataService>();
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
		
		NodeList nodeList = doc.getElementsByTagName(TAG_SERVICE);
		for(int i = 0; i < nodeList.getLength(); i++) {
			Element serviceElem = (Element) nodeList.item(i);
			
			String serviceName = serviceElem.getAttribute(ATTR_SERVICE_NAME);
			String url = serviceElem.getAttribute(ATTR_SERVICE_URL);
			
			Map<String, String> parameters = new HashMap<String, String>();
			NodeList paramList = serviceElem.getElementsByTagName(TAG_PARAMETER);
			for (int j = 0; j < paramList.getLength(); j++) {
				Element paramElem = (Element) paramList.item(j);
				parameters.put(paramElem.getAttribute(ATTR_PARAM_NAME), paramElem.getAttribute(ATTR_PARAM_URL_NAME));
			}
			
			GroupDataService service = new GroupDataService(serviceName, url, parameters);
			name2DataService.put(serviceName, service);
		}
	}
	
	/**
	 * Returns a service with the given name, if such a service is loaded.
	 * @param name the service name
	 * @return the OverlayDataService if it exists, null otherwise
	 */
	public static GroupDataService getServiceFromName(String name) {
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
	public static Map<String, List<String>> getAvailableServiceInformation() {
		Map<String, List<String>> serviceInfo = new HashMap<String, List<String>>();
		
		for (GroupDataService s : name2DataService.values()) {
			Map<String, String> params = s.getParameterNames();
			List<String> paramList = new ArrayList<String>();
			paramList.addAll(params.keySet());
			serviceInfo.put(s.getName(), paramList);
		}
		
		return serviceInfo;
	}
}