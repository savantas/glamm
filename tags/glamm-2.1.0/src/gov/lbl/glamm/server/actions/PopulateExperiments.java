package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.client.model.Experiment;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.ExperimentDAO;
import gov.lbl.glamm.server.dao.impl.ExperimentDAOImpl;

import java.util.List;

public class PopulateExperiments {
	
	public static List<Experiment> populateExperiments(GlammSession sm, String taxonomyId) {
	
		if(taxonomyId != null && !taxonomyId.isEmpty()) {
			ExperimentDAO expDao = new ExperimentDAOImpl(sm);
			return expDao.getAllExperimentsForTaxonomyId(taxonomyId);
		}
	
		return null;
	}

}
