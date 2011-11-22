package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Experiment;
import gov.lbl.glamm.client.model.Measurement;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.ExperimentDAO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExperimentDAOImpl implements ExperimentDAO {

	private GlammSession 				sm 				= null;
	private ExperimentMicroarrayDAOImpl	expUArrayDao 	= null;
	private ExperimentSessionDAOImpl 	expSessionDao 	= null;
	
	public ExperimentDAOImpl(GlammSession sm) {
		this.sm = sm;
		expUArrayDao = new ExperimentMicroarrayDAOImpl(sm);
		if(sm != null)
			expSessionDao = new ExperimentSessionDAOImpl(sm);
	}
	
	@Override
	public List<Sample.DataType> getAvailableExperimentTypes() {
		List<Sample.DataType> types = null;
		
		if(sm != null)
			types = expSessionDao.getAvailableExperimentTypes();
		
		if(types == null)
			types = expUArrayDao.getAvailableExperimentTypes();
		else
			types.addAll(expUArrayDao.getAvailableExperimentTypes());
		
		return types;
	}
	
	@Override
	public Experiment getExperiment(String experimentId) {
		if(sm != null && sm.isSessionExperiment(experimentId))
			return expSessionDao.getExperiment(experimentId);
		return expUArrayDao.getExperiment(experimentId);
	}

	@Override
	public List<Experiment> getAllExperimentsForTaxonomyId(String taxonomyId) {
		
		List<Experiment> experiments = null;
		
		if(sm != null)
			experiments = expSessionDao.getAllExperimentsForTaxonomyId(taxonomyId);
		
		if(experiments == null)
			experiments = expUArrayDao.getAllExperimentsForTaxonomyId(taxonomyId);
		else
			experiments.addAll(expUArrayDao.getAllExperimentsForTaxonomyId(taxonomyId));
		
		return experiments;
	}
	
	@Override
	public List<Sample> getAllSamplesForTaxonomyId(String taxonomyId) {
		
		List<Sample> sessionSamples = null;
		List<Sample> molSamples = expUArrayDao.getAllSamplesForTaxonomyId(taxonomyId);
		
		if(sm != null) 
			sessionSamples = expSessionDao.getAllSamplesForTaxonomyId(taxonomyId);
		
		if(sessionSamples == null && molSamples == null)
			return null;
		
		List<Sample> samples = new ArrayList<Sample>();
		
		if(sessionSamples != null)
			samples.addAll(sessionSamples);
		
		if(molSamples != null)
			samples.addAll(molSamples);
		
		return samples;
	}

	@Override
	public Map<String, Set<Measurement>> getMeasurements(String experimentId,
			String sampleId) {
		if(sm != null && sm.isSessionExperiment(experimentId))
			return expSessionDao.getMeasurements(experimentId, sampleId);
		return expUArrayDao.getMeasurements(experimentId, sampleId);
	}

	@Override
	public String getTaxonomyIdForExperimentId(String experimentId) {
		if(sm != null && sm.isSessionExperiment(experimentId))
			return expSessionDao.getTaxonomyIdForExperimentId(experimentId);
		return expUArrayDao.getTaxonomyIdForExperimentId(experimentId);
	}

	@Override
	public Map<String, Set<Measurement>> getMeasurementsForIds(
			String experimentId, String sampleId, Collection<String> ids) {
		if(sm != null && sm.isSessionExperiment(experimentId))
			return expSessionDao.getMeasurementsForIds(experimentId, sampleId, ids);
		return expUArrayDao.getMeasurementsForIds(experimentId, sampleId, ids);
	}
}
