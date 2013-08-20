package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.shared.model.Organism;
import gov.lbl.glamm.shared.model.Pathway;

/**
 * Data access object interface for pathways.
 * @author jtbates
 *
 */
public interface PathwayDAO {
	/**
	 * Gets a pathway for a given map id.
	 * @param mapId The map id.
	 * @return The pathway.
	 */
	public Pathway getPathway(String mapId);

	public Pathway getPathway(String mapId, Organism organism);
}
