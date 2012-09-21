package gov.lbl.glamm.shared;

import gov.lbl.glamm.server.externalservice.ExternalDataServiceManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class defines information needed to invoke an external web service that contains OverlayGroupData information. 
 * These are intended to be instantiated by a GroupDataServiceManager.
 * 
 * @see ExternalDataServiceManager
 * @author wjriehl
 *
 */
@SuppressWarnings("serial")
public class ExternalDataService implements Serializable {
	
	private String serviceName;
	private String serviceAbbrev;
	private String serviceUrl;
	private String serviceParser;
	private Set<ExternalServiceParameter> parameters;
	private Map<String, ExternalServiceParameter> name2Parameter;
	private List<String> allParameterNames;
	private List<String> userParameterNames;
	
	@SuppressWarnings("unused")
	private ExternalDataService() { }
	
	public ExternalDataService(String name, String abbrev, String url, String parser, Set<ExternalServiceParameter> parameters) {
		serviceName = name;
		serviceAbbrev = abbrev;
		serviceUrl = url;
		serviceParser = parser;
		this.parameters = parameters;
		
		name2Parameter = new HashMap<String, ExternalServiceParameter>();
		allParameterNames = new ArrayList<String>();
		userParameterNames = new ArrayList<String>();
		for (ExternalServiceParameter p : this.parameters) {
			name2Parameter.put(p.getHumanReadableName(), p);
			name2Parameter.put(p.getExternalUrlName(), p);
			name2Parameter.put(p.getStateUrlName(), p);

			allParameterNames.add(p.getHumanReadableName());
			if (p.getParameterType() != ExternalServiceParameter.ParameterType.HIDDEN)
				userParameterNames.add(p.getHumanReadableName());
		}
	}
	
	public String getServiceName() {
		return serviceName;
	}
	
	public String getUrl() {
		return serviceUrl;
	}
	
	public String getParser() {
		return serviceParser;
	}
	
	public String getAbbreviation() {
		return serviceAbbrev;
	}
	
	public Set<ExternalServiceParameter> getParameters() {
		return parameters;
	}
	
	public void setParameterValue(String name, String value) {
		if (name2Parameter.containsKey(name))
			name2Parameter.get(name).setValue(value);
	}

	public List<String> getAllParameterNames() {
		return allParameterNames;
	}
	
	public List<String> getUserParameterNames() {
		return userParameterNames;
	}
	
	public boolean hasParameter(String name) {
		return name2Parameter.containsKey(name);
	}
}
