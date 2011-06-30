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
	public Experiment getExperiment(String experimentId, String sampleId, String taxonomyId, String source) {
		Experiment experiment = null;
		
		if(source.equals(Experiment.EXP_SRC_SESSION)) {
			if(sm != null)
				experiment = expSessionDao.getExperiment(experimentId, sampleId, taxonomyId, source);
		}
		else if(source.equals(Experiment.EXP_SRC_MOL_UARRAY))
			experiment = expUArrayDao.getExperiment(experimentId, sampleId, taxonomyId, source);
		
		return experiment;
	}

	@Override
	public List<Experiment> getAllExperiments(String taxonomyId) {
		
		List<Experiment> experiments = null;
		
		if(sm != null)
			experiments = expSessionDao.getAllExperiments(taxonomyId);
		
		if(experiments == null)
			experiments = expUArrayDao.getAllExperiments(taxonomyId);
		else
			experiments.addAll(expUArrayDao.getAllExperiments(taxonomyId));
		
		return experiments;
	}
	
	@Override
	public List<Sample> getAllSamples(String taxonomyId) {
		
		List<Sample> sessionSamples = null;
		List<Sample> molSamples = expUArrayDao.getAllSamples(taxonomyId);
		
		if(sm != null) 
			sessionSamples = expSessionDao.getAllSamples(taxonomyId);
		
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
			String sampleId, String taxonomyId, String source) {

		Map<String, Set<Measurement>> id2Measurement = null;
		
		if(source.equals(Experiment.EXP_SRC_SESSION)) {
			if(sm != null)
				id2Measurement = expSessionDao.getMeasurements(experimentId, sampleId, taxonomyId, source);
		}
		
		else if(source.equals(Experiment.EXP_SRC_MOL_UARRAY))
			id2Measurement= expUArrayDao.getMeasurements(experimentId, sampleId, taxonomyId, source);
		
		return id2Measurement;
	}


}
