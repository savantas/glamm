package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.util.Synonym;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.GeneDAO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GeneSessionDAOImpl implements GeneDAO {

	GlammSession sm = null;

	//********************************************************************************

	public GeneSessionDAOImpl(GlammSession sm) {
		this.sm = sm;
	}

	//********************************************************************************

	@Override
	public Set<String> getEcNumsForOrganism(String taxonomyId) {
		
		Set<String> ecNums = null;
		
		if(sm != null) {
			List<Gene> genes = sm.getGenesForTaxonomyId(taxonomyId);
			if(genes != null) {
				for(Gene gene : genes) {
					Set<String> ecNumsForGene = gene.getEcNums();
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
	public List<Gene> getGenesForEcNums(String taxonomyId,
			Collection<String> ecNums) {
		List<Gene> genes = null;

		if(sm != null && 
				taxonomyId != null && 
				ecNums != null) {

			List<Gene> genesForTaxonomyId = sm.getGenesForTaxonomyId(taxonomyId);
			if(genesForTaxonomyId != null) {
				for(Gene gene : genesForTaxonomyId) {
					Set<String> ecNumsForGene = gene.getEcNums();
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
	public List<Gene> getGenesForOrganism(String taxonomyId) {
		List<Gene> genes = null;

		if(sm != null && taxonomyId != null) {
			genes = sm.getGenesForTaxonomyId(taxonomyId);
		}

		return genes;
	}

	//********************************************************************************

	@Override
	public List<Gene> getGenesForRxnIds(String taxonomyId, String[] rxnIds) {
		// Session-defined networks do not yet know what a reaction id is.  This may/will change.
		return null;
	}
	
	//********************************************************************************


	@Override
	public List<Gene> getGenesForSynonyms(String taxonomyId, Collection<String> synonyms) {
		List<Gene> genes = null;
		
		if(sm != null && taxonomyId != null && synonyms != null) {
			List<Gene> genesForTaxonomyId = sm.getGenesForTaxonomyId(taxonomyId);
			for(Gene gene : genesForTaxonomyId) {
				Set<Synonym> synonymsForGene = gene.getSynonyms();
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
	public List<Gene> getGenesForVimssIds(String taxonomyId, Collection<String> extIds) {
		return getGenesForSynonyms(taxonomyId, extIds);
	}


	//********************************************************************************
	
	

}
