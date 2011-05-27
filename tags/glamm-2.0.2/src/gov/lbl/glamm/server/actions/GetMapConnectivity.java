package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.MetabolicNetwork;
import gov.lbl.glamm.server.dao.MetabolicNetworkDAO;
import gov.lbl.glamm.server.dao.impl.MetabolicNetworkGlammDAOImpl;

public class GetMapConnectivity {
	
	public static MetabolicNetwork getMapConnectivity(String mapId) {
		MetabolicNetworkDAO mnDao = new MetabolicNetworkGlammDAOImpl();
		return mnDao.getNetworkForMapId(mapId);
	}
}
