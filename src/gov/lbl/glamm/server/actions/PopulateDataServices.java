package gov.lbl.glamm.server.actions;

import java.util.List;
import java.util.Map;

import gov.lbl.glamm.server.GroupDataServiceManager;
import gov.lbl.glamm.server.GlammSession;

/**
 * Service for fetching a set of external data services that GLAMM knows how to talk to.
 * @author wjriehl
 *
 */
public class PopulateDataServices {
	
	/**
	 * Returns Data Service information as a Map of service names to a list of parameter that service uses.
	 * TODO: make a simple class container, or maybe a Client-side model for data services.
	 * @param sm the GlammSession
	 * @return a Map where they key Strings are data service names and the values are lists of parameter name strings
	 */
	public static Map<String, List<String>> populateDataServices(GlammSession sm) {
		return GroupDataServiceManager.getAvailableServiceInformation();
	}

}
