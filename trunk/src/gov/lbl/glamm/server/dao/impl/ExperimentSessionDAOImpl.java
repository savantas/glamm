package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Experiment;
import gov.lbl.glamm.client.model.Measurement;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.server.SessionManager;
import gov.lbl.glamm.server.dao.ExperimentDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ExperimentSessionDAOImpl implements ExperimentDAO {

	private SessionManager sm = null;
	
	public ExperimentSessionDAOImpl(SessionManager sm) {
		this.sm = sm;
	}
	
	@Override
	public ArrayList<Experiment> getAllExperiments(String taxonomyId) {
		if(sm != null)
			return sm.getExperimentsForTaxonomyId(taxonomyId);
		return null;
	}
	
	@Override
	public ArrayList<Sample> getAllSamples(String taxonomyId) {
		if(sm == null) 
			return null;
			
		ArrayList<Experiment> experiments = getAllExperiments(taxonomyId);
			
		if(experiments == null)
			return null;
			
		ArrayList<Sample> samples = new ArrayList<Sample>();
			
		for(Experiment experiment : experiments)
				samples.addAll(experiment.getSamples());
		
		return samples;
	}
	
	@Override
	public Experiment getExperiment(String experimentId, String sampleId, String taxonomyId, String source) {
		if(sm != null)
			return sm.getExperimentForId(experimentId);
		return null;
	}

	@Override
	public HashMap<String,HashSet<Measurement>> getMeasurements(String experimentId,
			String sampleId, String taxonomyId, String source) {
		if(sm != null)
			return sm.getMeasurements(experimentId, sampleId, taxonomyId);
		return null;
	}

}
