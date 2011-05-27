package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Experiment;
import gov.lbl.glamm.server.SessionManager;
import gov.lbl.glamm.server.dao.ExperimentDAO;
import gov.lbl.glamm.server.dao.impl.ExperimentDAOImpl;

import java.util.ArrayList;

public class PopulateExperiments {
	
	public static ArrayList<Experiment> populateExperiments(SessionManager sm, String taxonomyId) {
	
		if(taxonomyId != null && !taxonomyId.isEmpty()) {
			ExperimentDAO expDao = new ExperimentDAOImpl(sm);
			return expDao.getAllExperiments(taxonomyId);
		}
	
		return null;
	}

}
