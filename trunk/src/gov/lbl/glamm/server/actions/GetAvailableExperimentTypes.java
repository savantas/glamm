package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.server.SessionManager;
import gov.lbl.glamm.server.dao.ExperimentDAO;
import gov.lbl.glamm.server.dao.impl.ExperimentDAOImpl;

import java.util.ArrayList;

public class GetAvailableExperimentTypes {
	public static ArrayList<Sample.DataType> getAvailableExperimentTypes(SessionManager sm) {
		ExperimentDAO expDao = new ExperimentDAOImpl(sm);
		return expDao.getAvailableExperimentTypes();
	}
}
