package gov.lbl.glamm.server.requesthandlers.impl;

import gov.lbl.glamm.client.model.Compound;
import gov.lbl.glamm.client.model.ReactionFunData;
import gov.lbl.glamm.server.dao.CompoundDAO;
import gov.lbl.glamm.server.dao.impl.CompoundGlammDAOImpl;
import gov.lbl.glamm.server.requesthandlers.RequestHandler;
import gov.lbl.glamm.server.responsehandlers.ResponseHandler;
import gov.lbl.glamm.shared.GlammConstants;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PopulateCompoundSearch implements RequestHandler {

	public static ArrayList<Compound> populateCompoundSearch(String extIdName) {
		CompoundDAO cpdDao = new CompoundGlammDAOImpl();
		return cpdDao.getCompoundsForSearch(extIdName);
	}

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		ReactionFunData rfd = new ReactionFunData();

		String dbName		= request.getParameter(GlammConstants.PARAM_EXTID_NAME);

		if(dbName != null && !dbName.isEmpty()) {
			ArrayList<Compound> cpds = populateCompoundSearch(dbName);

			if(cpds != null) {
				for(Compound cpd : cpds) {
					rfd.addPrimitive(cpd);
				}
			}
		}

		ResponseHandler.asXStreamXml(response, rfd, HttpServletResponse.SC_OK);

	}

}
