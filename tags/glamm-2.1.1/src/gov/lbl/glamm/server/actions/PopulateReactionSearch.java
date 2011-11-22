package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.ReactionDAO;
import gov.lbl.glamm.server.dao.impl.ReactionGlammDAOImpl;

import java.util.Set;

public class PopulateReactionSearch {
	
	public static Set<Reaction> populateReactionSearch(final GlammSession sm, final String mapId) {
		ReactionDAO rxnDao = new ReactionGlammDAOImpl(sm);
		return rxnDao.getReactionsForSearch(mapId);
	}

}
