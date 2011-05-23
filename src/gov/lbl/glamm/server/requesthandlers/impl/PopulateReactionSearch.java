package gov.lbl.glamm.server.requesthandlers.impl;

import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.client.model.ReactionFunData;
import gov.lbl.glamm.server.dao.ReactionDAO;
import gov.lbl.glamm.server.dao.impl.ReactionGlammDAOImpl;
import gov.lbl.glamm.server.requesthandlers.RequestHandler;
import gov.lbl.glamm.server.responsehandlers.ResponseHandler;
import gov.lbl.glamm.shared.GlammConstants;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PopulateReactionSearch implements RequestHandler {
	
	public static ArrayList<Reaction> populateReactionSearch(String extIdName) {
		ReactionDAO rxnDao = new ReactionGlammDAOImpl();
		return rxnDao.getReactionsForSearch(extIdName);
	}

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		ReactionFunData rfd = new ReactionFunData();

		String dbName		= request.getParameter(GlammConstants.PARAM_EXTID_NAME);

		if(dbName != null && !dbName.isEmpty()) {

			ArrayList<Reaction> rxns = populateReactionSearch(dbName);

			if(rxns != null) {
				for(Reaction rxn : rxns) {
					rfd.addPrimitive(rxn);
				}
			}
		}

		ResponseHandler.asXStreamXml(response, rfd, HttpServletResponse.SC_OK);

	}

}
