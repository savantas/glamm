package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Experiment;
import gov.lbl.glamm.client.model.GlammPrimitive;
import gov.lbl.glamm.client.model.GlammPrimitive.Synonym;
import gov.lbl.glamm.client.model.Measurement;
import gov.lbl.glamm.server.SessionManager;
import gov.lbl.glamm.server.dao.ExperimentDAO;
import gov.lbl.glamm.server.dao.GeneDAO;
import gov.lbl.glamm.server.dao.impl.ExperimentDAOImpl;
import gov.lbl.glamm.server.dao.impl.GeneDAOImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class GetExperiment  {

	public static ArrayList<? extends GlammPrimitive> getMeasurementsForExperiment(SessionManager sm, String experimentId, String sampleId, String taxonomyId, String expSource) {

		ArrayList<? extends GlammPrimitive> primitives = null;

		if(experimentId == null || experimentId.isEmpty() ||
				sampleId == null || sampleId.isEmpty() ||
				taxonomyId == null || taxonomyId.isEmpty() ||
				expSource == null || expSource.isEmpty()) 
			return null;

		ExperimentDAO 	expDao 	= new ExperimentDAOImpl(sm);

		// get measurements
		HashMap<String, HashSet<Measurement>> id2Measurements = expDao.getMeasurements(experimentId, sampleId, taxonomyId, expSource);
		if(id2Measurements == null)
			return null;

		Set<String> ids = id2Measurements.keySet();

		// get primitives for measurements - will vary with source
		if(expSource.equals(Experiment.EXP_SRC_MOL_UARRAY)) {
			GeneDAO geneDao = new GeneDAOImpl(sm);
			primitives = geneDao.getGenesForVimssIds(taxonomyId, ids);
		}
		else if(expSource.equals(Experiment.EXP_SRC_SESSION)) {
			GeneDAO geneDao = new GeneDAOImpl(sm);
			primitives = geneDao.getGenesForSynonyms(taxonomyId, ids);
		}

		if(primitives == null)
			return null;

		// add experiment to content
		for(GlammPrimitive primitive : primitives) {
			HashSet<Synonym> synonyms = primitive.getSynonyms();
			for(Synonym synonym : synonyms) {
				HashSet<Measurement> measurements = id2Measurements.get(synonym.getName());
				if(measurements != null)
					for(Measurement measurement : measurements)
						primitive.addMeasurement(measurement);
			}
		}

		return primitives;
	}

}
