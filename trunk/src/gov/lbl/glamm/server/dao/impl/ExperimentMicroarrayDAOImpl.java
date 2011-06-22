package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Experiment;
import gov.lbl.glamm.client.model.Measurement;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.server.GlammDbConnectionPool;
import gov.lbl.glamm.server.SessionManager;
import gov.lbl.glamm.server.dao.ExperimentDAO;
import gov.lbl.glamm.shared.GlammUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ExperimentMicroarrayDAOImpl implements ExperimentDAO {

	private final float UARRAY_CLAMP_MIN 	= -2.0f;
	private final float UARRAY_CLAMP_MID 	= 0.0f;
	private final float UARRAY_CLAMP_MAX 	= 2.0f;

	private final String UARRAY_UNITS 				= "mean Log2 Ratio";
	private final String UARRAY_CONFIDENCE_TYPE		= "zScore";
	
	private SessionManager sm = null;


	public ExperimentMicroarrayDAOImpl(SessionManager sm) {
		this.sm = sm;
	}
	
	//********************************************************************************
	
	@Override
	public ArrayList<Sample.DataType> getAvailableExperimentTypes() {

		ArrayList<Sample.DataType> types = null;
		String sql = "";

		if(GlammDbConnectionPool.getDbConfig().isFilterOnAcl())
			sql = "select distinct(e.expType) " +
			"from microarray.Exp e " +
			"join microarray.ExpType et on (et.expType=e.expType) " +
			"join genomics_test.ACL A on (A.resourceId=e.id and A.resourceType='uarray') " +
			"where A.requesterId in (" + (sm != null ? GlammUtils.joinCollection(sm.getMolAclGroupIds()) : "1") + ") and A.requesterType='group' and A.read=1 " +
			"order by et.expType;";
		else
			sql = "select distinct(e.expType) " +
			"from microarray.Exp e " +
			"join microarray.ExpType et on (et.expType=e.expType) " +
			"order by et.expType;";

		try {

			Connection connection = GlammDbConnectionPool.getConnection();
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
	public Experiment getExperiment(String experimentId, String sampleId, String taxonomyId, String source) {

		// sanity check on the source
		if(!source.equals(Experiment.EXP_SRC_MOL_UARRAY))
			return null;

		Experiment experiment = null;
		Sample sample = null;

		String sql = "";

		if(GlammDbConnectionPool.getDbConfig().isFilterOnAcl()) 
			sql = "select E.stress, R.cFactor, R.tFactor, R.factorUnit, R.cTime, R.tTime " +
			"from microarray.Exp E " +
			"join microarray.Chip C on (E.chipId=C.id) " +
			"join genomics_test.ACL A on (A.resourceId=E.id and A.resourceType='uarray') " +
			"join microarray.Replicate R on (E.id=R.expId) " +
			"where A.requesterId in (" + (sm != null ? GlammUtils.joinCollection(sm.getMolAclGroupIds()) : "1") + ") and A.requesterType='group' and A.read=1 " +
			"and R.expId=? and R.setId=? and C.taxonomyId=?;";
		else
			sql = "select E.stress, R.cFactor, R.tFactor, R.factorUnit, R.cTime, R.tTime " +
			"from microarray.Exp E " +
			"join microarray.Chip C on (E.chipId=C.id) " +
			"join microarray.Replicate R on (E.id=R.expId) " +
			"and R.expId=? and R.setId=? and C.taxonomyId=?;";

		try {

			Connection connection = GlammDbConnectionPool.getConnection();
			PreparedStatement ps = connection.prepareStatement(sql);

			ps.setString(1, experimentId);
			ps.setString(2, sampleId);
			ps.setString(3, taxonomyId);

			ResultSet rs = ps.executeQuery();

			if(rs.next()) {

				String stress 		= rs.getString("stress");
				String cFactor 		= rs.getString("cFactor");
				String tFactor 		= rs.getString("tFactor");
				String cTime		= rs.getString("cTime");
				String tTime		= rs.getString("tTime");
				String factorUnit	= rs.getString("factorUnit");


				experiment = new Experiment(experimentId, taxonomyId, Experiment.EXP_SRC_MOL_UARRAY);
				sample = new Sample(experimentId, sampleId, taxonomyId, Experiment.EXP_SRC_MOL_UARRAY);

				sample.setStress(stress);
				sample.setControl(cFactor, cTime);
				sample.setTreatment(tFactor, tTime);
				sample.setFactorUnits(factorUnit);
				sample.setClampValues(UARRAY_CLAMP_MIN, UARRAY_CLAMP_MID, UARRAY_CLAMP_MAX);
				sample.setUnits(UARRAY_UNITS);
				sample.setConfidenceType(UARRAY_CONFIDENCE_TYPE);

				experiment.addSample(sample);
			}

			rs.close();
			connection.close();

		} catch(Exception e) {
			e.printStackTrace();
		}

		return experiment;
	}

	//********************************************************************************

	@Override
	public ArrayList<Experiment> getAllExperiments(String taxonomyId) {

		ArrayList<Experiment> experiments = null;

		String sql = "";

		if(GlammDbConnectionPool.getDbConfig().isFilterOnAcl())
			sql = "select R.expId, R.setId, E.stress, R.cFactor, R.tFactor, R.factorUnit, R.cTime, R.tTime " +
			"from microarray.Exp E " +
			"join microarray.Chip C on (E.chipId=C.id) " +
			"join genomics_test.ACL A on (A.resourceId=E.id and A.resourceType='uarray') " +
			"join microarray.Replicate R on (E.id=R.expId) " +
			"where A.requesterId in (" + (sm != null ? GlammUtils.joinCollection(sm.getMolAclGroupIds()) : "1") + ") and A.requesterType='group' and A.read=1 " +
			"and C.taxonomyId=?;";
		else
			sql = "select R.expId, R.setId, E.stress, R.cFactor, R.tFactor, R.factorUnit, R.cTime, R.tTime " +
			"from microarray.Exp E " +
			"join microarray.Chip C on (E.chipId=C.id) " +
			"join microarray.Replicate R on (E.id=R.expId) " +
			"where C.taxonomyId=?;";

		try {

			Connection connection = GlammDbConnectionPool.getConnection();
			PreparedStatement ps = connection.prepareStatement(sql);

			ps.setString(1, taxonomyId);

			ResultSet rs = ps.executeQuery();

			// as there will be multiple Samples per Experiment, hash the experiments as you get them
			HashMap<String, Experiment> expId2Experiment = new HashMap<String, Experiment>();

			while(rs.next()) {

				String expId 		= rs.getString("expId");
				String setId 		= rs.getString("setId");
				String stress 		= rs.getString("stress");
				String cFactor 		= rs.getString("cFactor");
				String tFactor 		= rs.getString("tFactor");
				String cTime		= rs.getString("cTime");
				String tTime		= rs.getString("tTime");
				String factorUnit	= rs.getString("factorUnit");


				Experiment exp = expId2Experiment.get(expId);
				if(exp == null) {
					exp = new Experiment(expId, taxonomyId, Experiment.EXP_SRC_MOL_UARRAY);
					expId2Experiment.put(expId, exp);
				}

				Sample sample = new Sample(expId, setId, taxonomyId, Experiment.EXP_SRC_MOL_UARRAY);

				sample.setStress(stress);
				sample.setControl(cFactor, cTime);
				sample.setTreatment(tFactor, tTime);
				sample.setFactorUnits(factorUnit);
				sample.setClampValues(UARRAY_CLAMP_MIN, UARRAY_CLAMP_MID, UARRAY_CLAMP_MAX);
				sample.setUnits(UARRAY_UNITS);
				sample.setConfidenceType(UARRAY_CONFIDENCE_TYPE);

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
	public ArrayList<Sample> getAllSamples(String taxonomyId) {

		ArrayList<Sample> samples = null;

		String sql = "";

		if(GlammDbConnectionPool.getDbConfig().isFilterOnAcl())
			sql = "select R.expId, R.setId, E.stress, R.cFactor, R.tFactor, R.factorUnit, R.cTime, R.tTime " +
			"from microarray.Exp E " +
			"join microarray.Chip C on (E.chipId=C.id) " +
			"join genomics_test.ACL A on (A.resourceId=E.id and A.resourceType='uarray') " +
			"join microarray.Replicate R on (E.id=R.expId) " +
			"where A.requesterId in (" + (sm != null ? GlammUtils.joinCollection(sm.getMolAclGroupIds()) : "1") + ") and A.requesterType='group' and A.read=1 " +
			"and C.taxonomyId=?" +
			"group by R.expId, R.setId;";
		else
			sql = "select R.expId, R.setId, E.stress, R.cFactor, R.tFactor, R.factorUnit, R.cTime, R.tTime " +
			"from microarray.Exp E " +
			"join microarray.Chip C on (E.chipId=C.id) " +
			"join microarray.Replicate R on (E.id=R.expId) " +
			"where C.taxonomyId=?" +
			"group by R.expId, R.setId;";

		try {

			Connection connection = GlammDbConnectionPool.getConnection();
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


				Sample sample = new Sample(expId, setId, taxonomyId, Experiment.EXP_SRC_MOL_UARRAY);

				sample.setStress(stress);
				sample.setControl(cFactor, cTime);
				sample.setTreatment(tFactor, tTime);
				sample.setFactorUnits(factorUnit);
				sample.setClampValues(UARRAY_CLAMP_MIN, UARRAY_CLAMP_MID, UARRAY_CLAMP_MAX);
				sample.setUnits(UARRAY_UNITS);
				sample.setConfidenceType(UARRAY_CONFIDENCE_TYPE);

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
	public HashMap<String,HashSet<Measurement>> getMeasurements(String experimentId,
			String sampleId, String taxonomyId, String source) {
		// select expId, setId, meanLogRNorm, zScore, locusId from microarray.MeanLogRatio where expId=12 and setId=1;


		// sanity check on the source
		if(!source.equals(Experiment.EXP_SRC_MOL_UARRAY))
			return null;

		HashMap<String, HashSet<Measurement>> id2Measurement = null;

		String sql = "select meanLogRNorm, zScore, locusId from microarray.MeanLogRatio where expId=? and setId=?;";

		try {

			Connection connection = GlammDbConnectionPool.getConnection();
			PreparedStatement ps = connection.prepareStatement(sql);

			ps.setString(1, experimentId);
			ps.setString(2, sampleId);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {

				float value			= rs.getFloat("meanLogRNorm");
				float confidence 	= rs.getFloat("zScore");
				String targetId		= rs.getString("locusId");

				if(id2Measurement == null)
					id2Measurement = new HashMap<String, HashSet<Measurement>>();

				HashSet<Measurement> measurements = id2Measurement.get(targetId);

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

	//********************************************************************************

	

}
