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

public class GetRxn2EcMapping implements RequestHandler {

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		// TODO - this parameter will effectively be ignored until we have support for multiple map types
		String mapId		= request.getParameter(GlammConstants.PARAM_KEGG_MAP_ID);
		
		ReactionFunData rfd = new ReactionFunData();
		ReactionDAO rxnDao = new ReactionGlammDAOImpl();

		ArrayList<Reaction> rxns = rxnDao.getRxn2EcMapping(mapId, "LIGAND-RXN");

		if(rxns != null) {
			for(Reaction rxn : rxns) {
				rfd.addPrimitive(rxn);
			}
		}

		ResponseHandler.asXStreamXml(response, rfd, HttpServletResponse.SC_OK);

	}

}
