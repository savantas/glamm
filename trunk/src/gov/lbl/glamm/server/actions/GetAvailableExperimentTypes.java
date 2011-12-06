package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.ExperimentDAO;
import gov.lbl.glamm.server.dao.impl.ExperimentDAOImpl;

import java.util.List;

/**
 * Service class for getting the list of available experiment types.
 * @author jtbates
 *
 */
public class GetAvailableExperimentTypes {
	
	/**
	 * Gets the list of experimental data types.  The content of this list is dependent on the access level of the logged in user.
	 * @param sm The GLAMM session.
	 * @return The list of expeirmental data types.
	 */
	public static List<Sample.DataType> getAvailableExperimentTypes(GlammSession sm) {
		ExperimentDAO expDao = new ExperimentDAOImpl(sm);
		return expDao.getAvailableExperimentTypes();
	}
}
