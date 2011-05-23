package gov.lbl.glamm.server.requesthandlers.impl;

import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.ReactionFunData;
import gov.lbl.glamm.server.dao.OrganismDAO;
import gov.lbl.glamm.server.dao.impl.OrganismDAOImpl;
import gov.lbl.glamm.server.requesthandlers.RequestHandler;
import gov.lbl.glamm.server.responsehandlers.ResponseHandler;
import gov.lbl.glamm.server.session.SessionManager;
import gov.lbl.glamm.shared.GlammConstants;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PopulateOrganisms implements RequestHandler {
	
	public static ArrayList<Organism> populateOrganisms(SessionManager sm, String dataType) {
	
		OrganismDAO dao = new OrganismDAOImpl(sm);
		ArrayList<Organism> organisms = dao.getAllOrganismsWithDataForType(dataType);

		return organisms;
	}

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		ReactionFunData rfd = new ReactionFunData();
		SessionManager sm = SessionManager.getSessionManager(request, false);
		String dataType = request.getParameter(GlammConstants.PARAM_EXP_DATA_TYPE);
		
		ArrayList<Organism> organisms = populateOrganisms(sm, dataType);

		if(organisms != null) {
			for(Organism organism : organisms) {
				rfd.addPrimitive(organism);
			}
		}

		ResponseHandler.asXStreamXml(response, rfd, HttpServletResponse.SC_OK);

	}

}
