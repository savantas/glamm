package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.ExperimentDAO;
import gov.lbl.glamm.server.dao.impl.ExperimentDAOImpl;
import gov.lbl.glamm.shared.model.Organism;
import gov.lbl.glamm.shared.model.Sample;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for populating samples search boxes and lists.
 * @author jtbates
 *
 */
public class PopulateSamples {
	
	/**
	 * Gets the list of samples available for an organism.  The content of the list is dependent on the access level of the user.
	 * @param sm The GLAMM session.
	 * @param organism The organism.
	 * @return The list of available samples.
	 */
	public static List<Sample> populateSamples(GlammSession sm, Organism organism) {
		
		if(organism != null) {
			ExperimentDAO expDao = new ExperimentDAOImpl(sm);
			return expDao.getAllSamplesForTaxonomyId(organism.getTaxonomyId());
		}
	
		return new ArrayList<Sample>();
	}

}
