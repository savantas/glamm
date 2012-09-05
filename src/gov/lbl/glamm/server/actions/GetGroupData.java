package gov.lbl.glamm.server.actions;

import java.util.Map;
import java.util.Set;

import gov.lbl.glamm.client.model.OverlayDataGroup;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.GroupDataDAO;
import gov.lbl.glamm.server.dao.impl.GroupDataDAOImpl;
import gov.lbl.glamm.shared.ExternalDataService;

/**
 * Service class for retrieving OverlayDataGroups from either the database or an external service.
 * @author wjriehl
 *
 */
public class GetGroupData {
	
	public static Set<OverlayDataGroup> getOverlayData(GlammSession sm, String text) {
		GroupDataDAO dao = new GroupDataDAOImpl(sm);
		return dao.getGroupData(text);
	}

	public static Set<OverlayDataGroup> getOverlayDataFromService(GlammSession sm, ExternalDataService service) {
		GroupDataDAO dao = new GroupDataDAOImpl(sm);
		return dao.getGroupDataFromService(service);
	}
	
//	public static Set<OverlayDataGroup> getOverlayDataFromService(GlammSession sm, String serviceName, Map<String, String> parameters) {
//		GroupDataDAO dao = new GroupDataDAOImpl(sm);
//		return dao.getGroupDataFromService(serviceName, parameters);
//	}

}
