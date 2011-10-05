package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Measurement;
import gov.lbl.glamm.client.model.interfaces.HasMeasurements;
import gov.lbl.glamm.client.model.interfaces.HasSynonyms;
import gov.lbl.glamm.client.model.util.Synonym;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.ExperimentDAO;
import gov.lbl.glamm.server.dao.GeneDAO;
import gov.lbl.glamm.server.dao.impl.ExperimentDAOImpl;
import gov.lbl.glamm.server.dao.impl.GeneDAOImpl;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetExperiment  {

	public static List<? extends HasMeasurements> getMeasurementsForExperiment(GlammSession sm, String experimentId, String sampleId) {

		List<? extends HasMeasurements> primitives = null;

		if(experimentId == null || experimentId.isEmpty() ||
				sampleId == null || sampleId.isEmpty()) 
			return null;

		ExperimentDAO 	expDao 	= new ExperimentDAOImpl(sm);

		// get measurements
		Map<String, Set<Measurement>> id2Measurements = expDao.getMeasurements(experimentId, sampleId);
		if(id2Measurements == null)
			return null;

		Set<String> ids = id2Measurements.keySet();

		// get primitives for measurements
		String taxonomyId = expDao.getTaxonomyIdForExperimentId(experimentId);
		if(sm.isSessionExperiment(experimentId)) {
			GeneDAO geneDao = new GeneDAOImpl(sm);
			primitives = geneDao.getGenesForSynonyms(taxonomyId, ids);
		}
		else {
			GeneDAO geneDao = new GeneDAOImpl(sm);
			primitives = geneDao.getGenesForVimssIds(taxonomyId, ids);
		}

		if(primitives == null)
			return null;

		// add experiment to content
		for(HasMeasurements primitive : primitives) {
			Set<Synonym> synonyms = ((HasSynonyms) primitive).getSynonyms();
			for(Synonym synonym : synonyms) {
				Set<Measurement> measurements = id2Measurements.get(synonym.getName());
				if(measurements != null)
					for(Measurement measurement : measurements)
						primitive.addMeasurement(measurement);
			}
		}

		return primitives;
	}

}
