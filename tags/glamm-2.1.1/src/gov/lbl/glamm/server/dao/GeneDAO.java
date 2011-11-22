package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.client.model.Gene;

import java.util.Collection;
import java.util.Set;

public interface GeneDAO {
	public Set<String> getEcNumsForOrganism(String taxonomyId);
	public Set<Gene> getGenesForEcNums(String taxonomyId, Collection<String> ecNums);
	public Set<Gene> getGenesForVimssIds(String taxonomyId, Collection<String> vimssIds);
	public Set<Gene> getGenesForOrganism(String taxonomyId);
	public Set<Gene> getGenesForRxnIds(String taxonomyId, String[] rxnIds);
	public Set<Gene> getGenesForSynonyms(String taxonomyId, Collection<String> synonyms);
}
