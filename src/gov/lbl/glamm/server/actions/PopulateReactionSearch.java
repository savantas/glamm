package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.ReactionDAO;
import gov.lbl.glamm.server.dao.impl.ReactionGlammDAOImpl;

import java.util.Set;

/**
 * Service class for populating reaction search boxes.
 * @author jtbates
 *
 */
public class PopulateReactionSearch {
	
	/**
	 * Gets the set of reactions available on a pathway map.
	 * @param sm The GLAMM session.
	 * @param mapId The id of the pathway map.
	 * @return The set of available reactions.
	 */
	public static Set<Reaction> populateReactionSearch(final GlammSession sm, final String mapId) {
		ReactionDAO rxnDao = new ReactionGlammDAOImpl(sm);
		return rxnDao.getReactionsForSearch(mapId);
	}

}
