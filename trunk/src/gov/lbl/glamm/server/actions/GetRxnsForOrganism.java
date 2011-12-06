package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.GeneDAO;
import gov.lbl.glamm.server.dao.ReactionDAO;
import gov.lbl.glamm.server.dao.impl.GeneDAOImpl;
import gov.lbl.glamm.server.dao.impl.ReactionGlammDAOImpl;

import java.util.HashSet;
import java.util.Set;

/**
 * Service class for getting all the reactions available to an organism.
 * @author jtbates
 *
 */
public class GetRxnsForOrganism {
	
	/**
	 * Gets the set of all reactions available to an organism.
	 * @param sm The GLAMM session.
	 * @param taxonomyId The taxonomy id of the organism.
	 * @return The set of all reactions available to an organism.
	 */
	public static Set<Reaction> getRxnsForOrganism(final GlammSession sm, final String taxonomyId) {
		
		GeneDAO geneDao = new GeneDAOImpl(sm);
		ReactionDAO rxnDao = new ReactionGlammDAOImpl(sm);
		Set<Reaction> rxns = null;
		
		Set<Gene> genes = geneDao.getGenesForOrganism(taxonomyId);
	
		if(genes != null && !genes.isEmpty()) {
			Set<String> ecNums = new HashSet<String>();
			for(Gene gene : genes) {
				Set<String> ecNumsForGene = gene.getEcNums();
				if(ecNumsForGene != null)
					ecNums.addAll(ecNumsForGene);
			}
			rxns = rxnDao.getReactionsForEcNums(ecNums);
		}
		
		return rxns;
	}

}
