package gov.lbl.glamm.server.requesthandlers.impl;

import gov.lbl.glamm.client.model.MetabolicNetwork;
import gov.lbl.glamm.server.dao.MetabolicNetworkDAO;
import gov.lbl.glamm.server.dao.impl.MetabolicNetworkGlammDAOImpl;
import gov.lbl.glamm.server.requesthandlers.RequestHandler;
import gov.lbl.glamm.server.responsehandlers.ResponseHandler;
import gov.lbl.glamm.shared.GlammConstants;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetMapConnectivity implements RequestHandler {
	
	public static MetabolicNetwork getMapConnectivity(String mapId) {
		MetabolicNetworkDAO mnDao = new MetabolicNetworkGlammDAOImpl();
		return mnDao.getNetworkForMapId(mapId);
	}

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		String mapId		= request.getParameter(GlammConstants.PARAM_MAP_TITLE);
		
		MetabolicNetwork mn = getMapConnectivity(mapId);
		
		if(mn != null)
			ResponseHandler.asXStreamXml(response, mn, HttpServletResponse.SC_OK);

	}

}
