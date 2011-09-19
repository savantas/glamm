package gov.lbl.glamm.server.actions.requesthandlers;

import gov.lbl.glamm.client.model.Measurement;
import gov.lbl.glamm.server.RequestHandler;
import gov.lbl.glamm.server.ResponseHandler;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.ExperimentDAO;
import gov.lbl.glamm.server.dao.impl.ExperimentDAOImpl;
import gov.lbl.glamm.shared.RequestParameters;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadExperiment implements RequestHandler {
	
	private final String DEFAULT_FILE_NAME = "experiment.tab";

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String experimentId = request.getParameter(RequestParameters.EXPERIMENT.toString());
		String sampleId		= request.getParameter(RequestParameters.SAMPLE.toString());

		if(experimentId != null && !experimentId.isEmpty() &&
				sampleId != null && !sampleId.isEmpty()) {
			
			String content = "";
			
			GlammSession	sm 		= GlammSession.getGlammSession(request);
			ExperimentDAO 	expDao 	= new ExperimentDAOImpl(sm);
			Map<String, Set<Measurement>> id2Measurements = expDao.getMeasurements(experimentId, sampleId);
			
			for(Set<Measurement> measurementSet : id2Measurements.values()) {
				for(Measurement measurement : measurementSet) {
					content += measurement.getTargetId() + "\t" + measurement.getValue() + "\n";
				}
			}
			
			if(!content.isEmpty())
				ResponseHandler.asPlainTextAttachment(response, content, HttpServletResponse.SC_OK, DEFAULT_FILE_NAME);
		}

	}

}
