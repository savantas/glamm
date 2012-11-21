package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.MetabolicModelDAO;
import gov.lbl.glamm.server.dao.impl.MetabolicModelDAOImpl;
import gov.lbl.glamm.shared.model.MetabolicModel;

import java.util.List;

public class PopulateMetabolicModels {
	public static List<MetabolicModel> populateMetabolicModels(GlammSession sm) {
		MetabolicModelDAO dao = new MetabolicModelDAOImpl(sm);
		List<MetabolicModel> models = dao.getAllMetabolicModels();
		
		return models;
	}
}