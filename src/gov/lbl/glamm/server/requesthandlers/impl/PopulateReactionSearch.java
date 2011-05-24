package gov.lbl.glamm.server.requesthandlers.impl;

import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.server.dao.ReactionDAO;
import gov.lbl.glamm.server.dao.impl.ReactionGlammDAOImpl;

import java.util.ArrayList;

public class PopulateReactionSearch {
	
	public static ArrayList<Reaction> populateReactionSearch(String extIdName) {
		ReactionDAO rxnDao = new ReactionGlammDAOImpl();
		return rxnDao.getReactionsForSearch(extIdName);
	}

}
