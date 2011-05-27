package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.client.model.MetabolicNetwork;

public interface MetabolicNetworkDAO {
	public MetabolicNetwork getNetworkForMapId(String mapId);
}
