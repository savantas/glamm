package gov.lbl.glamm.server.requesthandlers.impl;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.ReactionFunData;
import gov.lbl.glamm.server.dao.GeneDAO;
import gov.lbl.glamm.server.dao.impl.GeneDAOImpl;
import gov.lbl.glamm.server.requesthandlers.RequestHandler;
import gov.lbl.glamm.server.responsehandlers.ResponseHandler;
import gov.lbl.glamm.server.session.SessionManager;
import gov.lbl.glamm.shared.GlammConstants;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PopulateLocusSearch implements RequestHandler {
	
	public static ArrayList<Gene> populateLocusSearch(final SessionManager sm, final String taxonomyId) {
		// create new gene DAO
		GeneDAO 	geneDao	= new GeneDAOImpl(sm);

		// get set of all genes for this organism
		return geneDao.getGenesForOrganism(taxonomyId);
	}

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		// create RFD object
		ReactionFunData content = new ReactionFunData();

		// get taxonomyId && dbName
		String taxonomyId	= request.getParameter(GlammConstants.PARAM_TAXONOMY_ID);

		if(taxonomyId != null && !taxonomyId.isEmpty()) {

			// grab the session manager, don't create a new one
			SessionManager sm = SessionManager.getSessionManager(request, false);

			ArrayList<Gene> genes = populateLocusSearch(sm, taxonomyId);

			if(genes != null) {
				for(Gene gene : genes)
					content.addPrimitive(gene);
			}
		}

		ResponseHandler.asXStreamXml(response, content, HttpServletResponse.SC_OK);
	}
}
