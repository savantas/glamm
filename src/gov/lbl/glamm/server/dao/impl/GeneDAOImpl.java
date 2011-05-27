package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.server.SessionManager;
import gov.lbl.glamm.server.dao.GeneDAO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class GeneDAOImpl implements GeneDAO {
	
	private static long MIN_METAGENOME_TAXID = 1000000000000l;
	
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
		if(Long.parseLong(taxonomyId) >= MIN_METAGENOME_TAXID)
			return metaMolDao.getEcNumsForOrganism(taxonomyId);
		return molDao.getEcNumsForOrganism(taxonomyId);
	}
	
	@Override
	public ArrayList<Gene> getGenesForEcNums(String taxonomyId,
			Collection<String> ecNums) {
		if(sm != null && sm.isSessionOrganism(taxonomyId))
			return sessionDao.getGenesForEcNums(taxonomyId, ecNums);
		if(Long.parseLong(taxonomyId) >= MIN_METAGENOME_TAXID)
			return metaMolDao.getGenesForEcNums(taxonomyId, ecNums);
		return molDao.getGenesForEcNums(taxonomyId, ecNums);
	}
	
	@Override
	public ArrayList<Gene> getGenesForVimssIds(String taxonomyId, Collection<String> vimssIds) {
		if(sm != null && sm.isSessionOrganism(taxonomyId))
			return sessionDao.getGenesForVimssIds(taxonomyId, vimssIds);
		if(Long.parseLong(taxonomyId) >= MIN_METAGENOME_TAXID)
			return metaMolDao.getGenesForVimssIds(taxonomyId, vimssIds);
		return molDao.getGenesForVimssIds(taxonomyId, vimssIds);
	}


	@Override
	public ArrayList<Gene> getGenesForOrganism(String taxonomyId) {
		if(sm != null && sm.isSessionOrganism(taxonomyId))
			return sessionDao.getGenesForOrganism(taxonomyId);
		if(Long.parseLong(taxonomyId) >= MIN_METAGENOME_TAXID)
			return metaMolDao.getGenesForOrganism(taxonomyId);
		return molDao.getGenesForOrganism(taxonomyId);
	}

	@Override
	public ArrayList<Gene> getGenesForRxnIds(String taxonomyId, String[] rxnIds) {
		if(sm != null && sm.isSessionOrganism(taxonomyId))
			return sessionDao.getGenesForRxnIds(taxonomyId, rxnIds);
		if(Long.parseLong(taxonomyId) >= MIN_METAGENOME_TAXID)
			return metaMolDao.getGenesForRxnIds(taxonomyId, rxnIds);
		return molDao.getGenesForRxnIds(taxonomyId, rxnIds);
	}

	@Override
	public ArrayList<Gene> getGenesForSynonyms(String taxonomyId,
			Collection<String> synonyms) {
		if(sm != null && sm.isSessionOrganism(taxonomyId))
			return sessionDao.getGenesForSynonyms(taxonomyId, synonyms);
		if(Long.parseLong(taxonomyId) >= MIN_METAGENOME_TAXID)
			return metaMolDao.getGenesForSynonyms(taxonomyId, synonyms);
		return molDao.getGenesForSynonyms(taxonomyId, synonyms);
	}

}
