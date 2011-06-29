package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.client.model.Experiment;
import gov.lbl.glamm.client.model.Measurement;
import gov.lbl.glamm.client.model.Sample;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ExperimentDAO {
	public List<Sample.DataType> getAvailableExperimentTypes();
	public Experiment getExperiment(String experimentId, String sampleId, String taxonomyId, String source);
	public List<Experiment> getAllExperiments(String taxonomyId);
	public List<Sample> getAllSamples(String taxonomyId);
	public Map<String,Set<Measurement>> getMeasurements(String experimentId, String sampleId, String taxonomyId, String source);
}
