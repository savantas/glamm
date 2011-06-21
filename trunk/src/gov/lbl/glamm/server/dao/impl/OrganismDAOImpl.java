package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.server.SessionManager;
import gov.lbl.glamm.server.dao.OrganismDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class OrganismDAOImpl implements OrganismDAO {
	
	private OrganismMetaMolDAOImpl	metaMolDao	= null;
	private OrganismMolDAOImpl 		molDao 		= null;
	private OrganismSessionDAOImpl 	sessionDao 	= null;
	
	public OrganismDAOImpl(SessionManager sm) {
		metaMolDao 	= new OrganismMetaMolDAOImpl();
		molDao 		= new OrganismMolDAOImpl();
		sessionDao 	= new OrganismSessionDAOImpl(sm);
	}

	@Override
	public ArrayList<Organism> getAllOrganisms() {
		
		ArrayList<Organism> sessionOrganisms	= sessionDao.getAllOrganisms();
		ArrayList<Organism> molOrganisms 		= molDao.getAllOrganisms();
		ArrayList<Organism> metaMolOrganisms 	= metaMolDao.getAllOrganisms();
		
		if(sessionOrganisms == null &&
				molOrganisms == null &&
				metaMolOrganisms == null)
			return null;
		
		ArrayList<Organism> organisms = new ArrayList<Organism>();
		
		if(sessionOrganisms != null)
			organisms.addAll(sessionOrganisms);
		if(molOrganisms != null)
			organisms.addAll(molOrganisms);
		if(metaMolOrganisms != null)
			organisms.addAll(metaMolOrganisms);
		
		
		return organisms;
	}

	@Override
	public ArrayList<Organism> getAllOrganismsWithDataForType(Sample.DataType dataType) {
		
		ArrayList<Organism> sessionOrganisms	= sessionDao.getAllOrganismsWithDataForType(dataType);
		ArrayList<Organism> molOrganisms 		= molDao.getAllOrganismsWithDataForType(dataType);
		ArrayList<Organism> metaMolOrganisms 	= metaMolDao.getAllOrganismsWithDataForType(dataType);
			
		if(sessionOrganisms == null &&
				molOrganisms == null &&
				metaMolOrganisms == null)
			return null;
		
		ArrayList<Organism> organisms = new ArrayList<Organism>();
		
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
	public HashMap<String, HashSet<Organism>> getTransgenicCandidatesForEcNums(HashSet<String> ecNums) {
		
		HashMap<String, HashSet<Organism>> sessionResults	= sessionDao.getTransgenicCandidatesForEcNums(ecNums);
		HashMap<String, HashSet<Organism>> molResults 		= molDao.getTransgenicCandidatesForEcNums(ecNums);
		HashMap<String, HashSet<Organism>> metaMolResults 	= metaMolDao.getTransgenicCandidatesForEcNums(ecNums);
		
		if(sessionResults == null &&
				molResults == null &&
				metaMolResults == null)
			return null;
		
		HashMap<String, HashSet<Organism>> results = new HashMap<String, HashSet<Organism>>();
		
		if(sessionResults != null)
			merge(results, sessionResults);
		if(molResults != null)
			merge(results, molResults);
		if(metaMolResults != null)
			merge(results, metaMolResults);
		
		return results;
	}
	
	private void merge(HashMap<String, HashSet<Organism>> dst, HashMap<String, HashSet<Organism>> src) {
		for(Entry<String, HashSet<Organism>> srcEntry : src.entrySet()) {
			HashSet<Organism> dstOrganisms = dst.get(srcEntry.getKey());
			if(dstOrganisms == null) {
				dstOrganisms = new HashSet<Organism>();
				dst.put(srcEntry.getKey(), dstOrganisms);
			}
			dstOrganisms.addAll(srcEntry.getValue());
		}
	}

}
