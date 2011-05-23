package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.server.dao.GeneDAO;
import gov.lbl.glamm.server.session.SessionManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class GeneDAOImpl implements GeneDAO {
	
	private SessionManager 		sm			= null;
	private GeneGlammDAOImpl 	glammDao 	= null;
	private GeneSessionDAOImpl 	sessionDao 	= null;

	public GeneDAOImpl(SessionManager sm) {
		this.sm = sm;
		glammDao = new GeneGlammDAOImpl();
		sessionDao = new GeneSessionDAOImpl(sm);
	}
	
	@Override
	public HashSet<String> getEcNumsForOrganism(String taxonomyId) {
		if(sm != null && sm.isSessionOrganism(taxonomyId))
			return sessionDao.getEcNumsForOrganism(taxonomyId);
		return glammDao.getEcNumsForOrganism(taxonomyId);
	}
	
	@Override
	public ArrayList<Gene> getGenesForEcNums(String taxonomyId,
			Collection<String> ecNums) {
		ArrayList<Gene> genes = sessionDao.getGenesForEcNums(taxonomyId, ecNums);
		if(genes == null) 
			genes = glammDao.getGenesForEcNums(taxonomyId, ecNums);
		return genes;
	}
	
	@Override
	public ArrayList<Gene> getGenesForVimssIds(String taxonomyId, Collection<String> extIds) {
		return glammDao.getGenesForVimssIds(taxonomyId, extIds);
	}


	@Override
	public ArrayList<Gene> getGenesForOrganism(String taxonomyId) {
		ArrayList<Gene> genes = sessionDao.getGenesForOrganism(taxonomyId);
		if(genes == null)
			genes = glammDao.getGenesForOrganism(taxonomyId);
		return genes;
	}

	@Override
	public ArrayList<Gene> getGenesForRxnIds(String taxonomyId, String[] rxnIds) {
		if(sm == null || !sm.isSessionOrganism(taxonomyId))
			return glammDao.getGenesForRxnIds(taxonomyId, rxnIds);
		return null;
	}

	@Override
	public ArrayList<Gene> getGenesForSynonyms(String taxonomyId,
			Collection<String> synonyms) {
		// TODO Auto-generated method stub
		if(sm != null && sm.isSessionOrganism(taxonomyId))
			return sessionDao.getGenesForSynonyms(taxonomyId, synonyms);
		return glammDao.getGenesForSynonyms(taxonomyId, synonyms);
	}

}
