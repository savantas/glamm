package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.server.SessionManager;
import gov.lbl.glamm.server.dao.OrganismDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class OrganismSessionDAOImpl implements OrganismDAO {

	private SessionManager sm = null;

	public OrganismSessionDAOImpl(SessionManager sm) {
		this.sm = sm;
	}

	@Override
	public ArrayList<Organism> getAllOrganisms() {
		if(sm != null)
			return sm.getOrganisms();
		return null;
	}

	@Override
	public ArrayList<Organism> getAllOrganismsWithDataForType(Sample.DataType dataType) {
		if(sm == null)
			return null;
		
		if(dataType == Sample.DataType.NONE)
			return sm.getOrganisms();
		
		if(dataType == Sample.DataType.SESSION)
			return sm.getOrganismsWithUserData();
	
		return null;
	}

	@Override
	public HashMap<String, HashSet<Organism>> getTransgenicCandidatesForEcNums(HashSet<String> ecNums) {
		HashMap<String, HashSet<Organism>> ecNum2Organisms = null;
		if(sm != null) {
			ArrayList<Organism> sessionOrganisms = sm.getOrganisms();
			if(sessionOrganisms == null)
				return null;
			ecNum2Organisms = new HashMap<String, HashSet<Organism>>();
			for(Organism organism : sessionOrganisms) {
				ArrayList<Gene> genes = sm.getGenesForOrganism(organism);
				if(genes == null)
					continue;
				for(Gene gene : genes) {
					HashSet<String> ecNumsForOrganism = gene.getEcNums();
					if(ecNumsForOrganism == null)
						continue;
					for(String ecNum : ecNumsForOrganism) {
						if(ecNums.contains(ecNum)) {
							HashSet<Organism> organisms =  ecNum2Organisms.get(ecNum);
							if(organisms == null) {
								organisms = new HashSet<Organism>();
								ecNum2Organisms.put(ecNum, organisms);
							}
							organisms.add(organism);
						}
					}

				}

			}

		}
		return ecNum2Organisms;
	}
	
	@Override
	public Organism getOrganismForTaxonomyId(final String taxonomyId) {
		if(sm == null)
			return null;
		return sm.getOrganismForTaxonomyId(taxonomyId);
	}

}