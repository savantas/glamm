package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.MetabolicNetwork;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.MetabolicNetworkDAO;
import gov.lbl.glamm.server.dao.impl.MetabolicNetworkGlammDAOImpl;

public class GetMapConnectivity {
	
	public static MetabolicNetwork getMapConnectivity(final GlammSession sm, final String mapId) {
		MetabolicNetworkDAO mnDao = new MetabolicNetworkGlammDAOImpl(sm);
		return mnDao.getNetworkForMapId(mapId);
	}
}
