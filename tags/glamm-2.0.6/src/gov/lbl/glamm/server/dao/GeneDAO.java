package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.client.model.Gene;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public interface GeneDAO {
	public HashSet<String> getEcNumsForOrganism(String taxonomyId);
	public ArrayList<Gene> getGenesForEcNums(String taxonomyId, Collection<String> ecNums);
	public ArrayList<Gene> getGenesForVimssIds(String taxonomyId, Collection<String> vimssIds);
	public ArrayList<Gene> getGenesForOrganism(String taxonomyId);
	public ArrayList<Gene> getGenesForRxnIds(String taxonomyId, String[] rxnIds);
	public ArrayList<Gene> getGenesForSynonyms(String taxonomyId, Collection<String> synonyms);
}
