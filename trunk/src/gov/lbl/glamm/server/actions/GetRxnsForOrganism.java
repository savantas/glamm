package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.server.SessionManager;
import gov.lbl.glamm.server.dao.GeneDAO;
import gov.lbl.glamm.server.dao.ReactionDAO;
import gov.lbl.glamm.server.dao.impl.GeneDAOImpl;
import gov.lbl.glamm.server.dao.impl.ReactionGlammDAOImpl;

import java.util.ArrayList;
import java.util.HashSet;

public class GetRxnsForOrganism {
	
	public static ArrayList<Reaction> getRxnsForOrganism(final SessionManager sm, final String taxonomyId, final HashSet<String> dbNames) {
		
		GeneDAO geneDao = new GeneDAOImpl(sm);
		ReactionDAO rxnDao = new ReactionGlammDAOImpl();
		ArrayList<Reaction> rxns = null;
		
		ArrayList<Gene> genes = geneDao.getGenesForOrganism(taxonomyId);
	
		if(genes != null && !genes.isEmpty()) {
			HashSet<String> ecNums = new HashSet<String>();
			for(Gene gene : genes) {
				HashSet<String> ecNumsForGene = gene.getEcNums();
				if(ecNumsForGene != null)
					ecNums.addAll(ecNumsForGene);
			}
			rxns = rxnDao.getReactionsForEcNums(ecNums, dbNames);
		}
		
		return rxns;
	}

}
