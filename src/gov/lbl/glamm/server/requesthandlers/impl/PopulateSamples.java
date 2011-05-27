package gov.lbl.glamm.server.requesthandlers.impl;

import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.server.SessionManager;
import gov.lbl.glamm.server.dao.ExperimentDAO;
import gov.lbl.glamm.server.dao.impl.ExperimentDAOImpl;

import java.util.ArrayList;

public class PopulateSamples {
	
	public static ArrayList<Sample> populateSamples(SessionManager sm, String taxonomyId) {
		
		if(taxonomyId != null && !taxonomyId.isEmpty()) {
			ExperimentDAO expDao = new ExperimentDAOImpl(sm);
			return expDao.getAllSamples(taxonomyId);
		}
	
		return null;
	}

}