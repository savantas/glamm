package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.GlammPrimitive.Synonym;
import gov.lbl.glamm.server.SessionManager;
import gov.lbl.glamm.server.dao.GeneDAO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class GeneSessionDAOImpl implements GeneDAO {

	SessionManager sm = null;

	//********************************************************************************

	public GeneSessionDAOImpl(SessionManager sm) {
		this.sm = sm;
	}

	//********************************************************************************

	@Override
	public HashSet<String> getEcNumsForOrganism(String taxonomyId) {
		HashSet<String> ecNums = null;
		
		if(sm != null) {
			ArrayList<Gene> genes = sm.getGenesForTaxonomyId(taxonomyId);
			if(genes != null) {
				for(Gene gene : genes) {
					HashSet<String> ecNumsForGene = gene.getEcNums();
					if(ecNumsForGene != null) {
						if(ecNums == null)
							ecNums = new HashSet<String>();
						ecNums.addAll(ecNumsForGene);
					}
				}
			}
		}
		
		return ecNums;
	}
	
	//********************************************************************************

	@Override
	public ArrayList<Gene> getGenesForEcNums(String taxonomyId,
			Collection<String> ecNums) {
		ArrayList<Gene> genes = null;

		if(sm != null && 
				taxonomyId != null && 
				ecNums != null) {

			ArrayList<Gene> genesForTaxonomyId = sm.getGenesForTaxonomyId(taxonomyId);
			if(genesForTaxonomyId != null) {
				for(Gene gene : genesForTaxonomyId) {
					HashSet<String> ecNumsForGene = gene.getEcNums();
					if(ecNumsForGene != null) {
						for(String ecNum : ecNumsForGene) {
							if(ecNums.contains(ecNum)) {
								if(genes == null)
									genes = new ArrayList<Gene>();
								if(!genes.contains(gene))
									genes.add(gene);
							}
						}
					}
				}
			}

		}
		return genes;
	}

	//********************************************************************************

	@Override
	public ArrayList<Gene> getGenesForOrganism(String taxonomyId) {
		ArrayList<Gene> genes = null;

		if(sm != null && taxonomyId != null) {
			genes = sm.getGenesForTaxonomyId(taxonomyId);
		}

		return genes;
	}

	//********************************************************************************

	@Override
	public ArrayList<Gene> getGenesForRxnIds(String taxonomyId, String[] rxnIds) {
		// Session-defined networks do not yet know what a reaction id is.  This may/will change.
		return null;
	}
	
	//********************************************************************************


	@Override
	public ArrayList<Gene> getGenesForSynonyms(String taxonomyId, Collection<String> synonyms) {
		ArrayList<Gene> genes = null;
		
		if(sm != null && taxonomyId != null && synonyms != null) {
			ArrayList<Gene> genesForTaxonomyId = sm.getGenesForTaxonomyId(taxonomyId);
			for(Gene gene : genesForTaxonomyId) {
				HashSet<Synonym> synonymsForGene = gene.getSynonyms();
				for(Synonym synonym : synonymsForGene) {
					if(synonyms.contains(synonym.getName())) {
						if(genes == null)
							genes = new ArrayList<Gene>();
						genes.add(gene);
					}
				}
			}
		}
		
		return genes;
	}

	//********************************************************************************
	
	@Override
	public ArrayList<Gene> getGenesForVimssIds(String taxonomyId, Collection<String> extIds) {
		return getGenesForSynonyms(taxonomyId, extIds);
	}


	//********************************************************************************
	
	

}
