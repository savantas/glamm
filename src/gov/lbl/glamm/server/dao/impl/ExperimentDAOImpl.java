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

/**
 * Implementation of the Experiment DAO interface.  This implementation allows aggregate access to the Microarray DAO implementation
 * (for accessing microarray data from MicrobesOnline) and the session DAO implementation (for accessing objects uploaded by the user during a session.)
 * @author jtbates
 *
 */
public class ExperimentDAOImpl implements ExperimentDAO {

	private GlammSession 				sm 				= null;
	private ExperimentMicroarrayDAOImpl	expUArrayDao 	= null;
	private ExperimentSessionDAOImpl 	expSessionDao 	= null;
	
	/**
	 * Constructor
	 * @param sm The GLAMM session.
	 */
	public ExperimentDAOImpl(GlammSession sm) {
		this.sm = sm;
		expUArrayDao = new ExperimentMicroarrayDAOImpl(sm);
		if(sm != null)
			expSessionDao = new ExperimentSessionDAOImpl(sm);
	}
	
	@Override
	public List<Sample.DataType> getAvailableSampleDataTypes() {
		List<Sample.DataType> types = null;
		
		if(sm != null)
			types = expSessionDao.getAvailableSampleDataTypes();
		
		if(types == null)
			types = expUArrayDao.getAvailableSampleDataTypes();
		else
			types.addAll(expUArrayDao.getAvailableSampleDataTypes());
		
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
