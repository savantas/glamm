package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.OrganismDAO;
import gov.lbl.glamm.shared.model.Gene;
import gov.lbl.glamm.shared.model.Organism;
import gov.lbl.glamm.shared.model.Sample;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of the Organism DAO interface granting access to user-uploaded organisms stored per-session.
 * @author jtbates
 *
 */
public class OrganismSessionDAOImpl implements OrganismDAO {

	private GlammSession sm = null;

	/**
	 * Constructor.
	 * @param sm The GLAMM Session.
	 */
	public OrganismSessionDAOImpl(GlammSession sm) {
		this.sm = sm;
	}

	@Override
	public List<Organism> getAllOrganisms() {
		if(sm != null)
			return sm.getOrganisms();
		return null;
	}

	@Override
	public List<Organism> getAllOrganismsWithDataForType(Sample.DataType dataType) {
		if(sm == null)
			return null;
		
		if(dataType == Sample.DataType.NONE)
			return sm.getOrganisms();
		
		if(dataType == Sample.DataType.SESSION)
			return sm.getOrganismsWithUserData();
	
		return null;
	}

	@Override
	public Map<String, Set<Organism>> getTransgenicCandidatesForEcNums(Set<String> ecNums) {
		
		Map<String, Set<Organism>> ecNum2Organisms = null;
		
		if(sm != null) {
			List<Organism> sessionOrganisms = sm.getOrganisms();
			if(sessionOrganisms == null)
				return null;
			ecNum2Organisms = new HashMap<String, Set<Organism>>();
			for(Organism organism : sessionOrganisms) {
				Set<Gene> genes = sm.getGenesForOrganism(organism);
				if(genes == null)
					continue;
				for(Gene gene : genes) {
					Set<String> ecNumsForOrganism = gene.getEcNums();
					if(ecNumsForOrganism == null)
						continue;
					for(String ecNum : ecNumsForOrganism) {
						if(ecNums.contains(ecNum)) {
							Set<Organism> organisms =  ecNum2Organisms.get(ecNum);
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
