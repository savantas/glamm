package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.shared.model.FluxExperiment;
import gov.lbl.glamm.shared.model.Measurement;
import gov.lbl.glamm.shared.model.MetabolicModel;
import gov.lbl.glamm.shared.model.Organism;
import gov.lbl.glamm.shared.model.Reaction;
import gov.lbl.glamm.shared.model.Media;
import gov.lbl.glamm.shared.model.kbase.fba.KBFBA;
import gov.lbl.glamm.shared.model.metabolism.deprecated.FbaExperiment;
import gov.lbl.glamm.shared.model.metabolism.deprecated.ModelVisualization;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
	public MetabolicModel getMetabolicModel(final String id);
	
	/**
	 * Gets the set of ids that correspond to all MetabolicModels built for an organism.
	 * @param organism the Organism for which we want all MetabolicModel ids
	 * @return a Set of id Strings for all MetabolicModels involving the given organism.
	 */
	public Set<String> getIdsForOrganism(final Organism organism);
	
	/**
	 * TODO - gonna be obsolete soon.
	 * Gets the set of fluxes and Reactions for the given FluxExperiment
	 * @param exp the FluxExperiment
	 * @return a Map of Reaction to Measurement set for the given FluxExperiment
	 */
	public Map<Reaction, Set<Measurement>> getReactionFluxes(final FluxExperiment exp); 
	
	public Set<Reaction> getReactionFluxes(final KBFBA fba);
	
	public MetabolicModel getMetabolicModelFromService(final String source, String id);
	
	public FbaExperiment getFbaExperimentFromService(final String source);
	
	public ModelVisualization getModelVisualizationFromService(final String source);
	
	public List<MetabolicModel> getAllMetabolicModels();

	public List<String> getFbaResultsForModel(final String modelId);

	public KBFBA getFbaResults(final String fbaId);

	public Media getMedia(final String mediaId, final String biochemistryId);

	Set<Reaction> getReactionFluxesFromKBase(final String fbaId);
}
