package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.GeneDAO;
import gov.lbl.glamm.server.dao.ReactionDAO;
import gov.lbl.glamm.server.dao.impl.GeneDAOImpl;
import gov.lbl.glamm.server.dao.impl.ReactionGlammDAOImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GetRxnsForOrganism {
	
	public static List<Reaction> getRxnsForOrganism(final GlammSession sm, final String taxonomyId, final Set<String> dbNames) {
		
		GeneDAO geneDao = new GeneDAOImpl(sm);
		ReactionDAO rxnDao = new ReactionGlammDAOImpl();
		List<Reaction> rxns = null;
		
		List<Gene> genes = geneDao.getGenesForOrganism(taxonomyId);
	
		if(genes != null && !genes.isEmpty()) {
			Set<String> ecNums = new HashSet<String>();
			for(Gene gene : genes) {
				Set<String> ecNumsForGene = gene.getEcNums();
				if(ecNumsForGene != null)
					ecNums.addAll(ecNumsForGene);
			}
			rxns = rxnDao.getReactionsForEcNums(ecNums, dbNames);
		}
		
		return rxns;
	}

}
