package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.GeneDAO;

import java.util.Collection;
import java.util.Set;

/**
 * Implementation of the Gene DAO interface aggregating access to genes stored in MicrobesOnline, MetaMicrobesOnline, and user-uploaded genes stored
 * per-session.
 * @author jtbates
 *
 */
public class GeneDAOImpl implements GeneDAO {
	
	private GlammSession 		sm			= null;
	private GeneMetaMolDAOImpl	metaMolDao	= null;
	private GeneMolDAOImpl 		molDao 		= null;
	private GeneSessionDAOImpl 	sessionDao 	= null;

	/**
	 * Constructor
	 * @param sm The GLAMM session.
	 */
	public GeneDAOImpl(GlammSession sm) {
		this.sm = sm;
		metaMolDao = new GeneMetaMolDAOImpl(sm);
		molDao = new GeneMolDAOImpl(sm);
		sessionDao = new GeneSessionDAOImpl(sm);
	}
	
	@Override
	public Set<String> getEcNumsForOrganism(String taxonomyId) {
		if(sm != null && sm.isSessionOrganism(taxonomyId))
			return sessionDao.getEcNumsForOrganism(taxonomyId);
		if(Long.parseLong(taxonomyId) >= Organism.MIN_METAGENOME_TAXID)
			return metaMolDao.getEcNumsForOrganism(taxonomyId);
		return molDao.getEcNumsForOrganism(taxonomyId);
	}
	
	@Override
	public Set<Gene> getGenesForEcNums(String taxonomyId,
			Collection<String> ecNums) {
		if(sm != null && sm.isSessionOrganism(taxonomyId))
			return sessionDao.getGenesForEcNums(taxonomyId, ecNums);
		if(Long.parseLong(taxonomyId) >= Organism.MIN_METAGENOME_TAXID)
			return metaMolDao.getGenesForEcNums(taxonomyId, ecNums);
		return molDao.getGenesForEcNums(taxonomyId, ecNums);
	}
	
	@Override
	public Set<Gene> getGenesForVimssIds(String taxonomyId, Collection<String> vimssIds) {
		if(sm != null && sm.isSessionOrganism(taxonomyId))
			return sessionDao.getGenesForVimssIds(taxonomyId, vimssIds);
		if(Long.parseLong(taxonomyId) >= Organism.MIN_METAGENOME_TAXID)
			return metaMolDao.getGenesForVimssIds(taxonomyId, vimssIds);
		return molDao.getGenesForVimssIds(taxonomyId, vimssIds);
	}


	@Override
	public Set<Gene> getGenesForOrganism(String taxonomyId) {
		if(sm != null && sm.isSessionOrganism(taxonomyId))
			return sessionDao.getGenesForOrganism(taxonomyId);
		if(Long.parseLong(taxonomyId) >= Organism.MIN_METAGENOME_TAXID)
			return metaMolDao.getGenesForOrganism(taxonomyId);
		return molDao.getGenesForOrganism(taxonomyId);
	}

	@Override
	public Set<Gene> getGenesForRxnIds(String taxonomyId, String[] rxnIds) {
		if(sm != null && sm.isSessionOrganism(taxonomyId))
			return sessionDao.getGenesForRxnIds(taxonomyId, rxnIds);
		if(Long.parseLong(taxonomyId) >= Organism.MIN_METAGENOME_TAXID)
			return metaMolDao.getGenesForRxnIds(taxonomyId, rxnIds);
		return molDao.getGenesForRxnIds(taxonomyId, rxnIds);
	}

	@Override
	public Set<Gene> getGenesForSynonyms(String taxonomyId,
			Collection<String> synonyms) {
		if(sm != null && sm.isSessionOrganism(taxonomyId))
			return sessionDao.getGenesForSynonyms(taxonomyId, synonyms);
		if(Long.parseLong(taxonomyId) >= Organism.MIN_METAGENOME_TAXID)
			return metaMolDao.getGenesForSynonyms(taxonomyId, synonyms);
		return molDao.getGenesForSynonyms(taxonomyId, synonyms);
	}
	

}
