package gov.lbl.glamm.server.dao;

import java.util.Map;
import java.util.Set;

import gov.lbl.glamm.shared.model.FluxExperiment;
import gov.lbl.glamm.shared.model.Measurement;
import gov.lbl.glamm.shared.model.MetabolicModel;
import gov.lbl.glamm.shared.model.Organism;
import gov.lbl.glamm.shared.model.Reaction;

/**
 * Data access object interface for Metabolic Models
 * @author wjriehl
 *
 */
public interface MetabolicModelDAO {
	
	/**
	 * Returns a MetabolicModel for the given model id
	 * @param id the MetabolicModel id
	 * @return a MetabolicModel or null
	 */
	public MetabolicModel getMetabolicModel(String id);
	
	/**
	 * Gets the set of ids that correspond to all MetabolicModels built for an organism.
	 * @param organism the Organism for which we want all MetabolicModel ids
	 * @return a Set of id Strings for all MetabolicModels involving the given organism.
	 */
	public Set<String> getIdsForOrganism(Organism organism);
	
	/**
	 * TODO - gonna be obsolete soon.
	 * Gets the set of fluxes and Reactions for the given FluxExperiment
	 * @param exp the FluxExperiment
	 * @return a Map of Reaction to Measurement set for the given FluxExperiment
	 */
	public Map<Reaction, Set<Measurement>> getFluxes(FluxExperiment exp); 
}
