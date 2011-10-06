package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Experiment;
import gov.lbl.glamm.client.model.Measurement;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.server.SessionManager;
import gov.lbl.glamm.server.dao.ExperimentDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ExperimentDAOImpl implements ExperimentDAO {

	private SessionManager 				sm 				= null;
	private ExperimentMicroarrayDAOImpl	expUArrayDao 	= null;
	private ExperimentSessionDAOImpl 	expSessionDao 	= null;
	
	public ExperimentDAOImpl(SessionManager sm) {
		this.sm = sm;
		expUArrayDao = new ExperimentMicroarrayDAOImpl();
		if(sm != null)
			expSessionDao = new ExperimentSessionDAOImpl(sm);
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
	public ArrayList<Experiment> getAllExperiments(String taxonomyId) {
		
		ArrayList<Experiment> experiments = null;
		
		if(sm != null)
			experiments = expSessionDao.getAllExperiments(taxonomyId);
		
		if(experiments == null)
			experiments = expUArrayDao.getAllExperiments(taxonomyId);
		else
			experiments.addAll(expUArrayDao.getAllExperiments(taxonomyId));
		
		return experiments;
	}
	
	@Override
	public ArrayList<Sample> getAllSamples(String taxonomyId) {
		
		ArrayList<Sample> samples = null;
		
		if(sm != null) 
			samples = expSessionDao.getAllSamples(taxonomyId);
		
		if(samples == null)
			samples = expUArrayDao.getAllSamples(taxonomyId);
		else
			samples.addAll(expUArrayDao.getAllSamples(taxonomyId));
		
		return samples;
	}

	@Override
	public HashMap<String,HashSet<Measurement>> getMeasurements(String experimentId,
			String sampleId, String taxonomyId, String source) {

		HashMap<String, HashSet<Measurement>> id2Measurement = null;
		
		if(source.equals(Experiment.EXP_SRC_SESSION)) {
			if(sm != null)
				id2Measurement = expSessionDao.getMeasurements(experimentId, sampleId, taxonomyId, source);
		}
		
		else if(source.equals(Experiment.EXP_SRC_MOL_UARRAY))
			id2Measurement= expUArrayDao.getMeasurements(experimentId, sampleId, taxonomyId, source);
		
		return id2Measurement;
	}


}