package gov.lbl.glamm.server.actions;

import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.MetabolicModelDAO;
import gov.lbl.glamm.server.dao.impl.MetabolicModelDAOImpl;
import gov.lbl.glamm.shared.model.Media;

public class GetMetabolicModelMedia {
	public static Media getMetabolicModelMedia(final GlammSession sm, final String mediaId, final String biochemistryId) {
		MetabolicModelDAO dao = new MetabolicModelDAOImpl(sm);
		return dao.getMedia(mediaId, biochemistryId);
	}
}
