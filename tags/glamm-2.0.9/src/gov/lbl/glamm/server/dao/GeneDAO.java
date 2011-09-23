package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.client.model.Gene;

import java.util.List;
import java.util.Collection;
import java.util.Set;

public interface GeneDAO {
	public Set<String> getEcNumsForOrganism(String taxonomyId);
	public List<Gene> getGenesForEcNums(String taxonomyId, Collection<String> ecNums);
	public List<Gene> getGenesForVimssIds(String taxonomyId, Collection<String> vimssIds);
	public List<Gene> getGenesForOrganism(String taxonomyId);
	public List<Gene> getGenesForRxnIds(String taxonomyId, String[] rxnIds);
	public List<Gene> getGenesForSynonyms(String taxonomyId, Collection<String> synonyms);
}
