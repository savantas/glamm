package gov.lbl.glamm.server.dao;

import java.util.Map;
import java.util.Set;

import gov.lbl.glamm.shared.ExternalDataService;
import gov.lbl.glamm.shared.model.OverlayDataGroup;

/**
 * Interface for a data access object that deals with Group Data.
 * @author wjriehl
 *
 */
public interface GroupDataDAO {

	/**
	 * Gets group data from a text input identifier
	 * TODO not used in its current form...
	 * @param text 
	 * @return
	 */
	public Set<OverlayDataGroup> getGroupData(String text);

	/**
	 * Gets Group Data from the service with the given name and set of parameters.
	 * @see ExternalDataService
	 * @param serviceName the name of the service to invoke. This should match up to one of the loaded GroupDataServices 
	 * @param parameters the parameters to involve with this service lookup.
	 * @return a Set of OverlayDataGroups fetched from the given service.
	 */
	//public Set<OverlayDataGroup> getGroupDataFromService(String serviceName, Map<String, String> parameters);
	
	public Set<OverlayDataGroup> getGroupDataFromService(ExternalDataService service);
}
