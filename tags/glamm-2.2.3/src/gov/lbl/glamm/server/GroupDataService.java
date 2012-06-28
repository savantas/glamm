package gov.lbl.glamm.server;

import gov.lbl.glamm.client.model.OverlayDataGroup;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class defines information needed to invoke an external web service that contains OverlayGroupData information. 
 * These are intended to be instantiated by a GroupDataServiceManager.
 * 
 * @see GroupDataServiceManager
 * @author wjriehl
 *
 */
public class GroupDataService {
	
	private String serviceName;
	private String serviceUrl;
	private Map<String, String> parameterNames;
	private Map<String, String> name2ParameterValue;
	
	public GroupDataService(String name, String url, Map<String, String> parameters) {
		serviceName = name;
		serviceUrl = url;
		parameterNames = parameters;
		
		name2ParameterValue = new HashMap<String, String>();
		for (String paramName : parameterNames.keySet()) {
			name2ParameterValue.put(paramName, "");
		}
	}
	
	public String getName() {
		return serviceName;
	}
	
	public String getUrl() {
		return serviceUrl;
	}
	
	/**
	 * Gets the Map containing all parameter names for this service. The keys are intended to be human-readable parameters
	 * for use in the widget views, and the values are more machine-readable for use in urls.
	 * 
	 * E.g. "MO Taxonomy ID" : "taxId"
	 * @return
	 */
	public Map<String, String> getParameterNames() {
		return parameterNames;
	}
	
	/**
	 * Sets the value for a parameter.
	 * @param name the name of the parameter
	 * @param value the value associated with the parameter
	 */
	public void setParameter(String name, String value) {
		if (name2ParameterValue.containsKey(name))
			name2ParameterValue.put(name, value);
	}
	
	/**
	 * Gets the URL-encoded parameter name from a more readable parameter name.
	 * @see getParameterNames()
	 * @param name
	 * @return
	 */
	public String getUrlParamName(String name) {
		if (parameterNames.containsKey(name))
			return parameterNames.get(name);
		return null;
	}

	/**
	 * //TODO fix stub
	 * @return
	 * @throws IOException
	 */
	public Set<OverlayDataGroup> performServiceCall() throws IOException {
		return new HashSet<OverlayDataGroup>();
	}
}
