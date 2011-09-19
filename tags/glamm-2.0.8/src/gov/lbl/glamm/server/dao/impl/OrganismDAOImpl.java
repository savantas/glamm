package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.OrganismDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class OrganismDAOImpl implements OrganismDAO {
	
	private OrganismMetaMolDAOImpl	metaMolDao	= null;
	private OrganismMolDAOImpl 		molDao 		= null;
	private OrganismSessionDAOImpl 	sessionDao 	= null;
	
	public OrganismDAOImpl(GlammSession sm) {
		metaMolDao 	= new OrganismMetaMolDAOImpl(sm);
		molDao 		= new OrganismMolDAOImpl(sm);
		sessionDao 	= new OrganismSessionDAOImpl(sm);
	}

	@Override
	public List<Organism> getAllOrganisms() {
		
		List<Organism> sessionOrganisms	= sessionDao.getAllOrganisms();
		List<Organism> molOrganisms 	= molDao.getAllOrganisms();
		List<Organism> metaMolOrganisms = metaMolDao.getAllOrganisms();
		
		if(sessionOrganisms == null &&
				molOrganisms == null &&
				metaMolOrganisms == null)
			return null;
		
		List<Organism> organisms = new ArrayList<Organism>();
		
		if(sessionOrganisms != null)
			organisms.addAll(sessionOrganisms);
		if(molOrganisms != null)
			organisms.addAll(molOrganisms);
		if(metaMolOrganisms != null)
			organisms.addAll(metaMolOrganisms);
		
		
		return organisms;
	}

	@Override
	public List<Organism> getAllOrganismsWithDataForType(Sample.DataType dataType) {
		
		List<Organism> sessionOrganisms	= sessionDao.getAllOrganismsWithDataForType(dataType);
		List<Organism> molOrganisms 	= molDao.getAllOrganismsWithDataForType(dataType);
		List<Organism> metaMolOrganisms = metaMolDao.getAllOrganismsWithDataForType(dataType);
			
		if(sessionOrganisms == null &&
				molOrganisms == null &&
				metaMolOrganisms == null)
			return null;
		
		List<Organism> organisms = new ArrayList<Organism>();
		
		if(sessionOrganisms != null)
			organisms.addAll(sessionOrganisms);
		if(molOrganisms != null)
			organisms.addAll(molOrganisms);
		if(metaMolOrganisms != null)
			organisms.addAll(metaMolOrganisms);
		
		
		return organisms;
	}
	
	@Override
	public Organism getOrganismForTaxonomyId(final String taxonomyId) {
		Organism organism = sessionDao.getOrganismForTaxonomyId(taxonomyId);
		
		if(organism == null)
			organism = molDao.getOrganismForTaxonomyId(taxonomyId);
		
		if(organism == null)
			organism = metaMolDao.getOrganismForTaxonomyId(taxonomyId);
		
		return organism;
	}

	@Override
	public Map<String, Set<Organism>> getTransgenicCandidatesForEcNums(Set<String> ecNums) {
		
		Map<String, Set<Organism>> sessionResults	= sessionDao.getTransgenicCandidatesForEcNums(ecNums);
		Map<String, Set<Organism>> molResults 		= molDao.getTransgenicCandidatesForEcNums(ecNums);
		Map<String, Set<Organism>> metaMolResults 	= metaMolDao.getTransgenicCandidatesForEcNums(ecNums);
		
		if(sessionResults == null &&
				molResults == null &&
				metaMolResults == null)
			return null;
		
		Map<String, Set<Organism>> results = new HashMap<String, Set<Organism>>();
		
		if(sessionResults != null)
			merge(results, sessionResults);
		if(molResults != null)
			merge(results, molResults);
		if(metaMolResults != null)
			merge(results, metaMolResults);
		
		return results;
	}
	
	private void merge(Map<String, Set<Organism>> dst, Map<String, Set<Organism>> src) {
		for(Entry<String, Set<Organism>> srcEntry : src.entrySet()) {
			Set<Organism> dstOrganisms = dst.get(srcEntry.getKey());
			if(dstOrganisms == null) {
				dstOrganisms = new HashSet<Organism>();
				dst.put(srcEntry.getKey(), dstOrganisms);
			}
			dstOrganisms.addAll(srcEntry.getValue());
		}
	}

}
