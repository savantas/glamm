package gov.lbl.glamm.server.kbase.actions;

import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.kbase.dao.KBMetabolicModelDAO;
import gov.lbl.glamm.server.kbase.dao.impl.KBMetabolicModelDAOImpl;
import gov.lbl.glamm.shared.model.kbase.fba.KBFBAResult;
import gov.lbl.glamm.shared.model.kbase.fba.model.KBMetabolicModel;
import gov.lbl.glamm.shared.model.kbase.workspace.KBWorkspaceObjectData;

public class GetMetabolicModel {
	public static KBMetabolicModel getMetabolicModel(final GlammSession sm, final String modelId, final String workspaceId) {
		KBMetabolicModelDAO dao = new KBMetabolicModelDAOImpl(sm);
		return dao.getModel(modelId, workspaceId);
	}
	
	public static KBMetabolicModel getMetabolicModel(final GlammSession sm, final KBWorkspaceObjectData modelData) {
		KBMetabolicModelDAO dao = new KBMetabolicModelDAOImpl(sm);
		return dao.getModel(modelData);
	}
	
	public static KBFBAResult getFBAResult(final GlammSession sm, final String fbaId, final String workspaceId) {
		KBMetabolicModelDAO dao = new KBMetabolicModelDAOImpl(sm);
		return dao.getFBAResult(fbaId, workspaceId);
	}
}
