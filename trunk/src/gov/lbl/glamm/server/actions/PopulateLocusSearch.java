package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.GeneDAO;
import gov.lbl.glamm.server.dao.impl.GeneDAOImpl;

import java.util.Set;

public class PopulateLocusSearch {
	
	public static Set<Gene> populateLocusSearch(final GlammSession sm, final String taxonomyId) {
		// create new gene DAO
		GeneDAO 	geneDao	= new GeneDAOImpl(sm);

		// get set of all genes for this organism
		return geneDao.getGenesForOrganism(taxonomyId);
	}

}
