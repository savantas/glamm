package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.client.model.Experiment;
import gov.lbl.glamm.client.model.Measurement;
import gov.lbl.glamm.client.model.Sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public interface ExperimentDAO {
	public ArrayList<Sample.DataType> getAvailableExperimentTypes();
	public Experiment getExperiment(String experimentId, String sampleId, String taxonomyId, String source);
	public ArrayList<Experiment> getAllExperiments(String taxonomyId);
	public ArrayList<Sample> getAllSamples(String taxonomyId);
	public HashMap<String,HashSet<Measurement>> getMeasurements(String experimentId, String sampleId, String taxonomyId, String source);
}
