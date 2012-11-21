package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.server.GlammDbConnectionPool;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.ExperimentDAO;
import gov.lbl.glamm.server.util.GlammUtils;
import gov.lbl.glamm.shared.model.Experiment;
import gov.lbl.glamm.shared.model.Measurement;
import gov.lbl.glamm.shared.model.Sample;
import gov.lbl.glamm.shared.model.Sample.DataType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of the Experiment DAO interface allowing access to the microarray data on MicrobesOnline.
 * @author jtbates
 *
 */
public class ExperimentMicroarrayDAOImpl implements ExperimentDAO {

	private final float UARRAY_CLAMP_MIN 	= -2.0f;
	private final float UARRAY_CLAMP_MID 	= 0.0f;
	private final float UARRAY_CLAMP_MAX 	= 2.0f;

	private final String UARRAY_UNITS 				= "mean Log2 Ratio";
	private final String UARRAY_CONFIDENCE_TYPE		= "zScore";

	private GlammSession sm = null;


	/**
	 * Constructor
	 * @param sm The GLAMM session.
	 */
	public ExperimentMicroarrayDAOImpl(GlammSession sm) {
		this.sm = sm;
	}

	

	@Override
	public List<Sample.DataType> getAvailableSampleDataTypes() {

		List<Sample.DataType> types = null;
		String sql = "select distinct(e.expType) " +
		"from microarray.Exp e " +
		"join microarray.ExpType et on (et.expType=e.expType) " +
		"join genomics_test.ACL A on (A.resourceId=e.id and A.resourceType='uarray') " +
		"where A.requesterId in (" + (sm != null ? GlammUtils.joinCollection(sm.getUser().getGroupIds()) : "1") + ") and A.requesterType='group' and A.read=1 " +
		"order by et.expType;";

		try {

			Connection connection = GlammDbConnectionPool.getConnection(sm);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);

			while(rs.next()) {
				if(types == null)
					types = new ArrayList<Sample.DataType>();
				types.add(Sample.DataType.dataTypeForMolExpType(rs.getString("expType")));
			}

			rs.close();
			connection.close();

		} catch(Exception e) {
			e.printStackTrace();
		}

		return types;

	}

	@Override
	public Experiment getExperiment(String experimentId) {

		Experiment experiment = null;

		String sql = "select E.stress, R.setId, E.expType, R.cFactor, R.tFactor, R.factorUnit, R.cTime, R.tTime " +
		"from microarray.Exp E " +
		"join genomics_test.ACL A on (A.resourceId=E.id and A.resourceType='uarray') " +
		"join microarray.Replicate R on (E.id=R.expId) " +
		"where A.requesterId in (" + (sm != null ? GlammUtils.joinCollection(sm.getUser().getGroupIds()) : "1") + ") and A.requesterType='group' and A.read=1 " +
		"and R.expId=?;";

		try {

			Connection connection = GlammDbConnectionPool.getConnection(sm);
			PreparedStatement ps = connection.prepareStatement(sql);

			ps.setString(1, experimentId);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {

				String stress 		= rs.getString("stress");
				String cFactor 		= rs.getString("cFactor");
				String tFactor 		= rs.getString("tFactor");
				String cTime		= rs.getString("cTime");
				String tTime		= rs.getString("tTime");
				String factorUnit	= rs.getString("factorUnit");
				String sampleId		= rs.getString("setId");
				String expType		= rs.getString("expType");

				if (experiment == null)
					experiment = new Experiment(experimentId);

				Sample sample = new Sample(experimentId, sampleId, Sample.DataType.dataTypeForMolExpType(expType));

				sample.setStress(stress);
				sample.setControl(cFactor, cTime);
				sample.setTreatment(tFactor, tTime);
				sample.setFactorUnits(factorUnit);
				sample.setClampValues(UARRAY_CLAMP_MIN, UARRAY_CLAMP_MID, UARRAY_CLAMP_MAX);
				sample.setUnits(UARRAY_UNITS);
				sample.setConfidenceType(UARRAY_CONFIDENCE_TYPE);
				sample.setTargetType(Sample.TargetType.GENE);

				experiment.addSample(sample);
			}

			rs.close();
			connection.close();

		} catch(Exception e) {
			e.printStackTrace();
		}

		return experiment;
	}

	

	@Override
	public List<Experiment> getAllExperimentsForTaxonomyId(String taxonomyId) {

		List<Experiment> experiments = null;

		String sql = "select R.expId, R.setId, E.stress, E.expType, R.cFactor, R.tFactor, R.factorUnit, R.cTime, R.tTime " +
		"from microarray.Exp E " +
		"join microarray.Chip C on (E.chipId=C.id) " +
		"join genomics_test.ACL A on (A.resourceId=E.id and A.resourceType='uarray') " +
		"join microarray.Replicate R on (E.id=R.expId) " +
		"where A.requesterId in (" + (sm != null ? GlammUtils.joinCollection(sm.getUser().getGroupIds()) : "1") + ") and A.requesterType='group' and A.read=1 " +
		"and C.taxonomyId=?;";

		try {

			Connection connection = GlammDbConnectionPool.getConnection(sm);
			PreparedStatement ps = connection.prepareStatement(sql);

			ps.setString(1, taxonomyId);

			ResultSet rs = ps.executeQuery();

			// as there will be multiple Samples per Experiment, hash the experiments as you get them
			Map<String, Experiment> expId2Experiment = new HashMap<String, Experiment>();

			while(rs.next()) {

				String expId 		= rs.getString("expId");
				String setId 		= rs.getString("setId");
				String stress 		= rs.getString("stress");
				String cFactor 		= rs.getString("cFactor");
				String tFactor 		= rs.getString("tFactor");
				String cTime		= rs.getString("cTime");
				String tTime		= rs.getString("tTime");
				String factorUnit	= rs.getString("factorUnit");
				String expType		= rs.getString("expType");


				Experiment exp = expId2Experiment.get(expId);
				if(exp == null) {
					exp = new Experiment(expId);
					expId2Experiment.put(expId, exp);
				}

				Sample sample = new Sample(expId, setId, DataType.dataTypeForMolExpType(expType));

				sample.setStress(stress);
				sample.setControl(cFactor, cTime);
				sample.setTreatment(tFactor, tTime);
				sample.setFactorUnits(factorUnit);
				sample.setClampValues(UARRAY_CLAMP_MIN, UARRAY_CLAMP_MID, UARRAY_CLAMP_MAX);
				sample.setUnits(UARRAY_UNITS);
				sample.setConfidenceType(UARRAY_CONFIDENCE_TYPE);
				sample.setTargetType(Sample.TargetType.GENE);

				exp.addSample(sample);
			}

			experiments = new ArrayList<Experiment>(expId2Experiment.values());

			rs.close();
			connection.close();

		} catch(Exception e) {
			e.printStackTrace();
		}

		return experiments;
	}

	@Override
	public List<Sample> getAllSamplesForTaxonomyId(String taxonomyId) {

		List<Sample> samples = null;

		String sql = "select E.expType, R.expId, R.setId, E.stress, R.cFactor, R.tFactor, R.factorUnit, R.cTime, R.tTime " +
		"from microarray.Exp E " +
		"join microarray.Chip C on (E.chipId=C.id) " +
		"join genomics_test.ACL A on (A.resourceId=E.id and A.resourceType='uarray') " +
		"join microarray.Replicate R on (E.id=R.expId) " +
		"where A.requesterId in (" + (sm != null ? GlammUtils.joinCollection(sm.getUser().getGroupIds()) : "1") + ") and A.requesterType='group' and A.read=1 " +
		"and C.taxonomyId=?" +
		"group by R.expId, R.setId;";

		try {

			Connection connection = GlammDbConnectionPool.getConnection(sm);
			PreparedStatement ps = connection.prepareStatement(sql);

			ps.setString(1, taxonomyId);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {

				String expId 		= rs.getString("expId");
				String setId 		= rs.getString("setId");
				String stress 		= rs.getString("stress");
				String cFactor 		= rs.getString("cFactor");
				String tFactor 		= rs.getString("tFactor");
				String cTime		= rs.getString("cTime");
				String tTime		= rs.getString("tTime");
				String factorUnit	= rs.getString("factorUnit");
				String expType		= rs.getString("expType");

				Sample sample = new Sample(expId, setId, DataType.dataTypeForMolExpType(expType));

				sample.setStress(stress);
				sample.setControl(cFactor, cTime);
				sample.setTreatment(tFactor, tTime);
				sample.setFactorUnits(factorUnit);
				sample.setClampValues(UARRAY_CLAMP_MIN, UARRAY_CLAMP_MID, UARRAY_CLAMP_MAX);
				sample.setUnits(UARRAY_UNITS);
				sample.setConfidenceType(UARRAY_CONFIDENCE_TYPE);
				sample.setTargetType(Sample.TargetType.GENE);

				if(samples == null)
					samples = new ArrayList<Sample>();

				samples.add(sample);
			}

			rs.close();
			connection.close();

		} catch(Exception e) {
			e.printStackTrace();
		}

		return samples;
	}

	@Override
	public Map<String, Set<Measurement>> getMeasurements(String experimentId, String sampleId) {

		Map<String, Set<Measurement>> id2Measurement = new HashMap<String, Set<Measurement>>();

		if(experimentId == null || sampleId == null)
			return id2Measurement;

		String sql = "select meanLogRNorm, zScore, locusId from microarray.MeanLogRatio where expId=? and setId=?;";

		try {

			Connection connection = GlammDbConnectionPool.getConnection(sm);
			PreparedStatement ps = connection.prepareStatement(sql);

			ps.setString(1, experimentId);
			ps.setString(2, sampleId);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {

				float value			= rs.getFloat("meanLogRNorm");
				float confidence 	= rs.getFloat("zScore");
				String targetId		= rs.getString("locusId");

				Set<Measurement> measurements = id2Measurement.get(targetId);

				if(measurements == null) {
					measurements = new HashSet<Measurement>();
					id2Measurement.put(targetId, measurements);
				}

				measurements.add(new Measurement(experimentId, sampleId, value, confidence, targetId));
			}

			rs.close();
			connection.close();

		} catch(Exception e) {
			e.printStackTrace();
		}

		return id2Measurement;
	}


	@Override
	public Map<String, Set<Measurement>> getMeasurementsForIds(String experimentId, String sampleId, Collection<String> ids) {

		Map<String, Set<Measurement>> id2Measurement = new HashMap<String, Set<Measurement>>();

		if(experimentId == null || sampleId == null || ids == null || ids.isEmpty())
			return id2Measurement;

		String sql = "select meanLogRNorm, zScore, locusId from microarray.MeanLogRatio where expId=? and setId=? and locusId in (" + GlammUtils.joinCollection(ids) + ");";

		try {

			Connection connection = GlammDbConnectionPool.getConnection(sm);
			PreparedStatement ps = connection.prepareStatement(sql);

			ps.setString(1, experimentId);
			ps.setString(2, sampleId);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {

				float value			= rs.getFloat("meanLogRNorm");
				float confidence 	= rs.getFloat("zScore");
				String targetId		= rs.getString("locusId");

				Set<Measurement> measurements = id2Measurement.get(targetId);

				if(measurements == null) {
					measurements = new HashSet<Measurement>();
					id2Measurement.put(targetId, measurements);
				}

				measurements.add(new Measurement(experimentId, sampleId, value, confidence, targetId));
			}

			rs.close();
			connection.close();

		} catch(Exception e) {
			e.printStackTrace();
		}

		return id2Measurement;
	}

	



	@Override
	public String getTaxonomyIdForExperimentId(String experimentId) {
		String taxonomyId = null;

		String sql = "select C.taxonomyId from microarray.Exp E join microarray.Chip C on (C.id=E.chipId) where E.id=?;";

		try {

			Connection connection = GlammDbConnectionPool.getConnection(sm);
			PreparedStatement ps = connection.prepareStatement(sql);

			ps.setString(1, experimentId);

			ResultSet rs = ps.executeQuery();

			if(rs.next()) 
				taxonomyId = rs.getString("taxonomyId");

			rs.close();
			connection.close();

		} catch(Exception e) {
			e.printStackTrace();
		}

		return taxonomyId;
	}


}
