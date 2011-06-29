package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.server.dao.ReactionDAO;
import gov.lbl.glamm.server.dao.impl.ReactionGlammDAOImpl;

import java.util.List;
import java.util.Set;

public class PopulateReactionSearch {
	
	public static List<Reaction> populateReactionSearch(Set<String> dbNames) {
		ReactionDAO rxnDao = new ReactionGlammDAOImpl();
		return rxnDao.getReactionsForSearch(dbNames);
	}

}
