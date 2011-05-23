package gov.lbl.glamm.server.requesthandlers.impl;

import gov.lbl.glamm.client.model.Measurement;
import gov.lbl.glamm.server.dao.ExperimentDAO;
import gov.lbl.glamm.server.dao.impl.ExperimentDAOImpl;
import gov.lbl.glamm.server.requesthandlers.RequestHandler;
import gov.lbl.glamm.server.responsehandlers.ResponseHandler;
import gov.lbl.glamm.server.session.SessionManager;
import gov.lbl.glamm.shared.GlammConstants;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadExperiment implements RequestHandler {
	
	private final String DEFAULT_FILE_NAME = "experiment.tab";

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String experimentId = request.getParameter(GlammConstants.PARAM_EXPERIMENT);
		String sampleId		= request.getParameter(GlammConstants.PARAM_SAMPLE);
		String taxonomyId	= request.getParameter(GlammConstants.PARAM_TAXONOMY_ID);
		String expSource	= request.getParameter(GlammConstants.PARAM_EXP_SOURCE);

		
		if(experimentId != null && !experimentId.isEmpty() &&
				sampleId != null && !sampleId.isEmpty() &&
				taxonomyId != null && !taxonomyId.isEmpty() &&
				expSource != null && !expSource.isEmpty()) {
			
			String content = "";
			
			SessionManager	sm 		= SessionManager.getSessionManager(request, false);
			ExperimentDAO 	expDao 	= new ExperimentDAOImpl(sm);
			HashMap<String, HashSet<Measurement>> id2Measurements = expDao.getMeasurements(experimentId, sampleId, taxonomyId, expSource);
			
			for(HashSet<Measurement> measurementSet : id2Measurements.values()) {
				for(Measurement measurement : measurementSet) {
					content += measurement.getTargetId() + "\t" + measurement.getValue() + "\n";
				}
			}
			
			if(!content.isEmpty())
				ResponseHandler.asPlainTextAttachment(response, content, HttpServletResponse.SC_OK, DEFAULT_FILE_NAME);
		}

	}

}
