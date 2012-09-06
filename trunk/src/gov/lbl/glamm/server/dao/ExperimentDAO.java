package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.shared.model.Experiment;
import gov.lbl.glamm.shared.model.Measurement;
import gov.lbl.glamm.shared.model.Sample;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * Data access object interface for experiments.
 * @author jtbates
 *
 */
public interface ExperimentDAO {
	
	/**
	 * Gets the list of available sample data types.
	 * @return The list of data types.
	 */
	public List<Sample.DataType> getAvailableSampleDataTypes();
	
	/**
	 * Gets the experiment for a given experiment id.
	 * @param experimentId The experiment id.
	 * @return The experiment.
	 */
	public Experiment getExperiment(String experimentId);
	
	/**
	 * Gets the list of all experiments for a given taxonomy id.  The list's content depends on the access level of the user.
	 * @param taxonomyId The taxonomy id.
	 * @return The list of experiments.
	 */
	public List<Experiment> getAllExperimentsForTaxonomyId(String taxonomyId);
	
	/**
	 * Gets the list of all samples for a given taxonomy id.  The list's content depends on the access level of the user.
	 * @param taxonomyId The taxonomy id.
	 * @return The list of samples.
	 */
	public List<Sample> getAllSamplesForTaxonomyId(String taxonomyId);
	
	/**
	 * Gets a map of all ids to their measurements for a given experiment and sample id.
	 * @param experimentId The experiment id.
	 * @param sampleId The sample id.
	 * @return The id to measurement map.
	 */
	public Map<String,Set<Measurement>> getMeasurements(String experimentId, String sampleId);
	
	/**
	 * Gets a map of ids to their measurements for  agiven experiment id, sample id, and collection of ids.
	 * @param experimentId The experiment id.
	 * @param sampleId The sample id.
	 * @param ids The collection of ids.
	 * @return The id to measurement map.
	 */
	public Map<String,Set<Measurement>> getMeasurementsForIds(String experimentId, String sampleId, Collection<String> ids);
	
	/**
	 * Gets the taxonomy id for a given experiment id.
	 * @param experimentId The experiment id.
	 * @return The taxonomy id.
	 */
	public String getTaxonomyIdForExperimentId(String experimentId);
}
