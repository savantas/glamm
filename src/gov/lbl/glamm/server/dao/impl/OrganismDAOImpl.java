package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.server.SessionManager;
import gov.lbl.glamm.server.dao.OrganismDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class OrganismDAOImpl implements OrganismDAO {
	
	private OrganismMolDAOImpl 		molDao 		= null;
	private OrganismSessionDAOImpl 	sessionDao 	= null;
	
	public OrganismDAOImpl(SessionManager sm) {
		molDao 		= new OrganismMolDAOImpl();
		sessionDao 	= new OrganismSessionDAOImpl(sm);
	}

	@Override
	public ArrayList<Organism> getAllOrganisms() {
		ArrayList<Organism> organisms = sessionDao.getAllOrganisms();
		if(organisms != null)
			organisms.addAll(molDao.getAllOrganisms());
		else
			organisms = molDao.getAllOrganisms();
		return organisms;
	}

	@Override
	public ArrayList<Organism> getAllOrganismsWithDataForType(String dataType) {
		
		final int MAX_RETRIES = 5;
		
		ArrayList<Organism> organisms = sessionDao.getAllOrganismsWithDataForType(dataType);
		ArrayList<Organism> molOrganisms = null;
		
		for(int i = 0; i < MAX_RETRIES; i++) {
			molOrganisms = molDao.getAllOrganismsWithDataForType(dataType);
			if(molOrganisms != null && !molOrganisms.isEmpty()) {
				if(organisms != null)
					organisms.addAll(molOrganisms);
				else
					organisms = molOrganisms;
				break;
			}
		}
		
		
		return organisms;
	}

	@Override
	public HashMap<String, HashSet<Organism>> getTransgenicCandidatesForEcNums(HashSet<String> ecNums) {
		HashMap<String, HashSet<Organism>> sResults = sessionDao.getTransgenicCandidatesForEcNums(ecNums);
		HashMap<String, HashSet<Organism>> mResults = molDao.getTransgenicCandidatesForEcNums(ecNums);
		
		if(sResults == null)
			return mResults;
		else if(mResults == null)
			return sResults;
		
		// merge hashes - put sResults into mResults, since sResults will be smaller
		for(Entry<String, HashSet<Organism>> entry : sResults.entrySet()) {
			HashSet<Organism> organisms = mResults.get(entry.getKey());
			if(organisms == null)
				mResults.put(entry.getKey(), entry.getValue());
			else
				organisms.addAll(entry.getValue());
		}
		
		return mResults;
	}

}
