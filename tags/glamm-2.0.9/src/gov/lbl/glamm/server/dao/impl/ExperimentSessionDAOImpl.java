package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Experiment;
import gov.lbl.glamm.client.model.Measurement;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.ExperimentDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExperimentSessionDAOImpl implements ExperimentDAO {

	private GlammSession sm = null;
	
	public ExperimentSessionDAOImpl(GlammSession sm) {
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
	public List<Experiment> getAllExperimentsForTaxonomyId(String taxonomyId) {
		if(sm != null)
			return sm.getExperimentsForTaxonomyId(taxonomyId);
		return null;
	}
	
	@Override
	public List<Sample> getAllSamplesForTaxonomyId(String taxonomyId) {
		if(sm == null) 
			return null;
			
		List<Experiment> experiments = getAllExperimentsForTaxonomyId(taxonomyId);
			
		if(experiments == null)
			return null;
			
		List<Sample> samples = new ArrayList<Sample>();
			
		for(Experiment experiment : experiments)
				samples.addAll(experiment.getSamples());
		
		return samples;
	}
	
	@Override
	public Experiment getExperiment(String experimentId) {
		if(sm != null)
			return sm.getExperimentForId(experimentId);
		return null;
	}

	@Override
	public Map<String, Set<Measurement>> getMeasurements(String experimentId,
			String sampleId) {
		if(sm != null)
			return sm.getMeasurements(experimentId, sampleId);
		return null;
	}
	
	@Override
	public String getTaxonomyIdForExperimentId(String experimentId) {
		if(sm != null)
			return sm.getTaxonomyIdForExperimentId(experimentId);
		return null;
	}

}
