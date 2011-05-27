package gov.lbl.glamm.server.requesthandlers.impl;

import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.server.dao.ReactionDAO;
import gov.lbl.glamm.server.dao.impl.ReactionGlammDAOImpl;

import java.util.ArrayList;
import java.util.HashSet;

public class PopulateReactionSearch {
	
	public static ArrayList<Reaction> populateReactionSearch(HashSet<String> dbNames) {
		ReactionDAO rxnDao = new ReactionGlammDAOImpl();
		return rxnDao.getReactionsForSearch(dbNames);
	}

}
