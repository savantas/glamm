package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.GeneDAO;
import gov.lbl.glamm.server.dao.impl.GeneDAOImpl;

import java.util.Set;

/**
 * Service class for getting the set of genes for the specified organism.
 * @author jtbates
 *
 */
public class PopulateLocusSearch {
	
	/**
	 * Gets the set of genes for the specified organism.
	 * @param sm The GLAMM session.
	 * @param organism The organism.
	 * @return The set of genes for the specified organism.
	 */
	public static Set<Gene> populateLocusSearch(final GlammSession sm, final Organism organism) {
		// create new gene DAO
		GeneDAO 	geneDao	= new GeneDAOImpl(sm);

		// get set of all genes for this organism
		return geneDao.getGenesForOrganism(organism.getTaxonomyId());
	}

}
