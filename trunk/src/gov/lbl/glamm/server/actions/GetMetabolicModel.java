package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.MetabolicModelDAO;
import gov.lbl.glamm.server.dao.impl.MetabolicModelDAOImpl;
import gov.lbl.glamm.shared.model.MetabolicModel;

/**
 * Service class for retrieving a MetabolicModel.
 * @author wjriehl
 *
 */
public class GetMetabolicModel {

	public static MetabolicModel getMetabolicModel(GlammSession sm, String modelId) {
		MetabolicModelDAO modelDao = new MetabolicModelDAOImpl(sm);
		return modelDao.getMetabolicModel(modelId);
	}
	
}
