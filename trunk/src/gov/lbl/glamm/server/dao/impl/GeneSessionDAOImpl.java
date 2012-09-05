package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.util.Synonym;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.GeneDAO;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of the Gene DAO for accessing user-uploaded gene data.
 * @author jtbates
 *
 */
public class GeneSessionDAOImpl implements GeneDAO {

	GlammSession sm = null;

	/**
	 * Constructor.
	 * @param sm The GLAMM session.
	 */
	public GeneSessionDAOImpl(GlammSession sm) {
		this.sm = sm;
	}

	@Override
	public Set<String> getEcNumsForOrganism(String taxonomyId) {

		Set<String> ecNums = null;

		if(sm != null) {
			Set<Gene> genes = sm.getGenesForTaxonomyId(taxonomyId);
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

	@Override
	public Set<Gene> getGenesForEcNums(String taxonomyId,
			Collection<String> ecNums) {
		Set<Gene> genes = new HashSet<Gene>();

		if(sm != null && 
				taxonomyId != null && 
				ecNums != null) {

			Set<Gene> genesForTaxonomyId = sm.getGenesForTaxonomyId(taxonomyId);
			if(genesForTaxonomyId != null) {
				for(Gene gene : genesForTaxonomyId) {
					Set<String> ecNumsForGene = gene.getEcNums();
					if(ecNumsForGene != null) {
						for(String ecNum : ecNumsForGene) {
							if(ecNums.contains(ecNum)) {
								genes.add(gene);
							}
						}
					}
				}
			}

		}
		return genes;
	}

	@Override
	public Set<Gene> getGenesForOrganism(String taxonomyId) {
		
		if(sm == null || taxonomyId == null)
			return new HashSet<Gene>();
		return sm.getGenesForTaxonomyId(taxonomyId);
	}

	@Override
	public Set<Gene> getGenesForRxnIds(String taxonomyId, String[] rxnIds) {
		// Session-defined networks do not yet know what a reaction id is.  This may/will change.
		return new HashSet<Gene>();
	}

	@Override
	public Set<Gene> getGenesForSynonyms(String taxonomyId, Collection<String> synonyms) {
		Set<Gene> genes = new HashSet<Gene>();

		if(sm != null && taxonomyId != null && synonyms != null) {
			Set<Gene> genesForTaxonomyId = sm.getGenesForTaxonomyId(taxonomyId);
			for(Gene gene : genesForTaxonomyId) {
				Set<Synonym> synonymsForGene = gene.getSynonyms();
				for(Synonym synonym : synonymsForGene) {
					if(synonyms.contains(synonym.getName())) {
						genes.add(gene);
					}
				}
			}
		}

		return genes;
	}

	@Override
	public Set<Gene> getGenesForVimssIds(String taxonomyId, Collection<String> extIds) {
		return getGenesForSynonyms(taxonomyId, extIds);
	}
	
	@Override
	//TODO - stub.
	public Set<Gene> getGenesForVimssIds(Collection<String> ids) {
		return getGenesForSynonyms("", ids);
	}

}
