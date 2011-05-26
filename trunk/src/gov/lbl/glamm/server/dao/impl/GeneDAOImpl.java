package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.server.SessionManager;
import gov.lbl.glamm.server.dao.GeneDAO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class GeneDAOImpl implements GeneDAO {
	
	private SessionManager 		sm			= null;
	private GeneMetaMolDAOImpl	metaMolDao	= null;
	private GeneMolDAOImpl 		molDao 		= null;
	private GeneSessionDAOImpl 	sessionDao 	= null;

	public GeneDAOImpl(SessionManager sm) {
		this.sm = sm;
		metaMolDao = new GeneMetaMolDAOImpl();
		molDao = new GeneMolDAOImpl();
		sessionDao = new GeneSessionDAOImpl(sm);
	}
	
	@Override
	public HashSet<String> getEcNumsForOrganism(String taxonomyId) {
		if(sm != null && sm.isSessionOrganism(taxonomyId))
			return sessionDao.getEcNumsForOrganism(taxonomyId);
		HashSet<String> ecNums = molDao.getEcNumsForOrganism(taxonomyId);
		if(ecNums == null)
			ecNums = metaMolDao.getEcNumsForOrganism(taxonomyId);
		return ecNums;
	}
	
	@Override
	public ArrayList<Gene> getGenesForEcNums(String taxonomyId,
			Collection<String> ecNums) {
		ArrayList<Gene> genes = sessionDao.getGenesForEcNums(taxonomyId, ecNums);
		if(genes == null) 
			genes = molDao.getGenesForEcNums(taxonomyId, ecNums);
		if(genes == null)
			genes = metaMolDao.getGenesForEcNums(taxonomyId, ecNums);
		return genes;
	}
	
	@Override
	public ArrayList<Gene> getGenesForVimssIds(String taxonomyId, Collection<String> extIds) {
		ArrayList<Gene> genes = molDao.getGenesForVimssIds(taxonomyId, extIds);
		if(genes == null)
			genes = metaMolDao.getGenesForVimssIds(taxonomyId, extIds);
		return genes;
	}


	@Override
	public ArrayList<Gene> getGenesForOrganism(String taxonomyId) {
		ArrayList<Gene> genes = sessionDao.getGenesForOrganism(taxonomyId);
		if(genes == null)
			genes = molDao.getGenesForOrganism(taxonomyId);
		if(genes == null)
			genes = metaMolDao.getGenesForOrganism(taxonomyId);
		return genes;
	}

	@Override
	public ArrayList<Gene> getGenesForRxnIds(String taxonomyId, String[] rxnIds) {
		if(sm == null || !sm.isSessionOrganism(taxonomyId)) {
			ArrayList<Gene> genes = molDao.getGenesForRxnIds(taxonomyId, rxnIds);
			if(genes == null)
				genes = metaMolDao.getGenesForRxnIds(taxonomyId, rxnIds);
			return genes;
		}
		return null;
	}

	@Override
	public ArrayList<Gene> getGenesForSynonyms(String taxonomyId,
			Collection<String> synonyms) {
		if(sm != null && sm.isSessionOrganism(taxonomyId))
			return sessionDao.getGenesForSynonyms(taxonomyId, synonyms);
		ArrayList<Gene> genes = molDao.getGenesForSynonyms(taxonomyId, synonyms);
		if(genes == null)
			genes = metaMolDao.getGenesForSynonyms(taxonomyId, synonyms);
		return genes;
	}

}
