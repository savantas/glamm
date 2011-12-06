package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Compound;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.CompoundDAO;
import gov.lbl.glamm.server.dao.impl.CompoundGlammDAOImpl;

import java.util.Set;

/**
 * Service class for populating compound search boxes.
 * @author jtbates
 *
 */
public class PopulateCompoundSearch {

	/**
	 * Gets the set of compounds visible on the specified pathway map.
	 * @param sm The GLAMM session.
	 * @param mapId The map id.
	 * @return The set of compounds visible on the specified pathway map.
	 */
	public static Set<Compound> populateCompoundSearch(final GlammSession sm, String mapId) {
		CompoundDAO cpdDao = new CompoundGlammDAOImpl(sm);
		return cpdDao.getCompoundsForSearch(mapId);
	}

}
