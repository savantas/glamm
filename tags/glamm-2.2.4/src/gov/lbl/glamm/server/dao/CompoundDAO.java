package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.shared.model.Compound;


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
	
	/**
	 * Gets the set of compounds corresponding to a set of ids.
	 * @param ids The set of ids.
	 * @return The set of compounds.
	 */
	public Set<Compound> getCompounds(Set<String> ids);
	
	/**
	 * Gets the set of compounds for a given map id.
	 * @param mapId The map id.
	 * @return The set of compounds.
	 */
	public Set<Compound> getCompoundsForSearch(String mapId);
}
