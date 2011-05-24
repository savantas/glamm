package gov.lbl.glamm.server.requesthandlers.impl;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.server.dao.GeneDAO;
import gov.lbl.glamm.server.dao.impl.GeneDAOImpl;
import gov.lbl.glamm.server.session.SessionManager;

import java.util.ArrayList;

public class PopulateLocusSearch {
	
	public static ArrayList<Gene> populateLocusSearch(final SessionManager sm, final String taxonomyId) {
		// create new gene DAO
		GeneDAO 	geneDao	= new GeneDAOImpl(sm);

		// get set of all genes for this organism
		return geneDao.getGenesForOrganism(taxonomyId);
	}

}
