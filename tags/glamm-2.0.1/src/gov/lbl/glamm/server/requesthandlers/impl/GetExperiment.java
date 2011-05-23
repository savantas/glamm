package gov.lbl.glamm.server.requesthandlers.impl;

import gov.lbl.glamm.client.model.Experiment;
import gov.lbl.glamm.client.model.GlammPrimitive;
import gov.lbl.glamm.client.model.GlammPrimitive.Synonym;
import gov.lbl.glamm.client.model.Measurement;
import gov.lbl.glamm.client.model.ReactionFunData;
import gov.lbl.glamm.server.dao.ExperimentDAO;
import gov.lbl.glamm.server.dao.GeneDAO;
import gov.lbl.glamm.server.dao.impl.ExperimentDAOImpl;
import gov.lbl.glamm.server.dao.impl.GeneDAOImpl;
import gov.lbl.glamm.server.requesthandlers.RequestHandler;
import gov.lbl.glamm.server.responsehandlers.ResponseHandler;
import gov.lbl.glamm.server.session.SessionManager;
import gov.lbl.glamm.shared.GlammConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetExperiment implements RequestHandler {

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
		if(expSource.equals(GlammConstants.EXP_SRC_MOL_UARRAY)) {
			GeneDAO geneDao = new GeneDAOImpl(sm);
			primitives = geneDao.getGenesForVimssIds(taxonomyId, ids);
		}
		else if(expSource.equals(GlammConstants.EXP_SRC_SESSION)) {
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

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String experimentId = request.getParameter(GlammConstants.PARAM_EXPERIMENT);
		String sampleId		= request.getParameter(GlammConstants.PARAM_SAMPLE);
		String taxonomyId	= request.getParameter(GlammConstants.PARAM_TAXONOMY_ID);
		String expSource	= request.getParameter(GlammConstants.PARAM_EXP_SOURCE);

		ReactionFunData content = new ReactionFunData();

		SessionManager	sm 		= SessionManager.getSessionManager(request, false);
		ExperimentDAO 	expDao 	= new ExperimentDAOImpl(sm);
		Experiment experiment = expDao.getExperiment(experimentId, sampleId, taxonomyId, expSource);

		if(experiment != null) {
			ArrayList<? extends GlammPrimitive> primitives = getMeasurementsForExperiment(sm, experimentId, sampleId, taxonomyId, expSource);

			// add experiment to content
			if(primitives != null) {
				content.addPrimitive(experiment);
				for(GlammPrimitive primitive : primitives)
					content.addPrimitive(primitive);
			}
		}
		
		ResponseHandler.asXStreamXml(response, content, HttpServletResponse.SC_OK);
	}
}
