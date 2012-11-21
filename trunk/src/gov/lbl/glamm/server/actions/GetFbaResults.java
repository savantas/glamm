package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.MetabolicModelDAO;
import gov.lbl.glamm.server.dao.impl.MetabolicModelDAOImpl;
import gov.lbl.glamm.shared.model.kbase.fba.FBA;

public class GetFbaResults {
	public static FBA getFbaResults(final GlammSession sm, final String fbaId) {
		MetabolicModelDAO dao = new MetabolicModelDAOImpl(sm);
		return dao.getFbaResults(fbaId);
	}
}