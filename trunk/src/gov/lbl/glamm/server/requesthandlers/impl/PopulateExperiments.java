package gov.lbl.glamm.server.requesthandlers.impl;

import gov.lbl.glamm.client.model.Experiment;
import gov.lbl.glamm.client.model.ReactionFunData;
import gov.lbl.glamm.server.dao.ExperimentDAO;
import gov.lbl.glamm.server.dao.impl.ExperimentDAOImpl;
import gov.lbl.glamm.server.requesthandlers.RequestHandler;
import gov.lbl.glamm.server.responsehandlers.ResponseHandler;
import gov.lbl.glamm.server.session.SessionManager;
import gov.lbl.glamm.shared.GlammConstants;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PopulateExperiments implements RequestHandler {
	
	public static ArrayList<Experiment> populateExperiments(SessionManager sm, String taxonomyId) {
	
		if(taxonomyId != null && !taxonomyId.isEmpty()) {
			ExperimentDAO expDao = new ExperimentDAOImpl(sm);
			return expDao.getAllExperiments(taxonomyId);
		}
	
		return null;
	}

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		// create RFD object
		ReactionFunData content = new ReactionFunData();

		// get taxonomyId
		String taxonomyId	= request.getParameter(GlammConstants.PARAM_TAXONOMY_ID);

		if(taxonomyId != null && !taxonomyId.isEmpty()) {
			
			SessionManager sm = SessionManager.getSessionManager(request, false);
		
			ArrayList<Experiment> experiments = populateExperiments(sm, taxonomyId);
			if(experiments != null) {
				for(Experiment exp : experiments) {
					content.addPrimitive(exp);
				}
			}
		}
		
		ResponseHandler.asXStreamXml(response, content, HttpServletResponse.SC_OK);
	}

}
