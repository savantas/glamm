package gov.lbl.glamm.server.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.server.dao.GeneDAO;

public class GeneMetaMolDAOImpl implements GeneDAO {

	@Override
	public HashSet<String> getEcNumsForOrganism(String taxonomyId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Gene> getGenesForEcNums(String taxonomyId,
			Collection<String> ecNums) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Gene> getGenesForVimssIds(String taxonomyId,
			Collection<String> extIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Gene> getGenesForOrganism(String taxonomyId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Gene> getGenesForRxnIds(String taxonomyId, String[] rxnIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Gene> getGenesForSynonyms(String taxonomyId,
			Collection<String> synonyms) {
		// TODO Auto-generated method stub
		return null;
	}

}
