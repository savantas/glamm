package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Experiment;
import gov.lbl.glamm.client.model.Measurement;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.server.SessionManager;
import gov.lbl.glamm.server.dao.ExperimentDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExperimentSessionDAOImpl implements ExperimentDAO {

	private SessionManager sm = null;
	
	public ExperimentSessionDAOImpl(SessionManager sm) {
		this.sm = sm;
	}
	
	@Override
	public List<Sample.DataType> getAvailableExperimentTypes() {
		if(sm != null && sm.hasExperiments()) {
			List<Sample.DataType> types = new ArrayList<Sample.DataType>();
			types.add(Sample.DataType.SESSION);
			return types;
		}
		return null;
	}
	
	@Override
	public List<Experiment> getAllExperiments(String taxonomyId) {
		if(sm != null)
			return sm.getExperimentsForTaxonomyId(taxonomyId);
		return null;
	}
	
	@Override
	public List<Sample> getAllSamples(String taxonomyId) {
		if(sm == null) 
			return null;
			
		List<Experiment> experiments = getAllExperiments(taxonomyId);
			
		if(experiments == null)
			return null;
			
		List<Sample> samples = new ArrayList<Sample>();
			
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
	public Map<String, Set<Measurement>> getMeasurements(String experimentId,
			String sampleId, String taxonomyId, String source) {
		if(sm != null)
			return sm.getMeasurements(experimentId, sampleId, taxonomyId);
		return null;
	}

}
