package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.client.model.Experiment;
import gov.lbl.glamm.client.model.Measurement;
import gov.lbl.glamm.client.model.Sample;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ExperimentDAO {
	public List<Sample.DataType> getAvailableExperimentTypes();
	public Experiment getExperiment(String experimentId);
	public List<Experiment> getAllExperimentsForTaxonomyId(String taxonomyId);
	public List<Sample> getAllSamplesForTaxonomyId(String taxonomyId);
	public Map<String,Set<Measurement>> getMeasurements(String experimentId, String sampleId);
	public Map<String,Set<Measurement>> getMeasurementsForIds(String experimentId, String sampleId, Collection<String> ids);
	public String getTaxonomyIdForExperimentId(String experimentId);
}
