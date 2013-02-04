package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.MetabolicModelDAO;
import gov.lbl.glamm.server.dao.impl.MetabolicModelDAOImpl;

import java.util.List;

public class PopulateFbaResults {
	public static List<String> populateFbaResults(GlammSession sm, String modelId) {
		MetabolicModelDAO dao = new MetabolicModelDAOImpl(sm);
		return dao.getFbaResultsForModel(modelId);
	}
}
