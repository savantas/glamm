package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.client.model.Compound;

import java.util.Set;

public interface CompoundDAO {
	
	public Compound getCompound(String id, String dbName);
	public Set<Compound> getCompounds(Set<String> ids);
	public Set<Compound> getCompoundsForSearch(String mapId);
}
