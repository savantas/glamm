package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.client.model.Compound;


import java.util.Set;
/**
 * Data access object interface for compounds.
 * @author jtbates
 *
 */
public interface CompoundDAO {
	/**
	 * Gets a compound.
	 * @param id The compound id.
	 * @param dbName The database to which this compound belongs.
	 * @return The compound.
	 */
	public Compound getCompound(String id, String dbName);
	public Set<Compound> getCompounds(Set<String> ids);
	public Set<Compound> getCompoundsForSearch(String mapId);
}
