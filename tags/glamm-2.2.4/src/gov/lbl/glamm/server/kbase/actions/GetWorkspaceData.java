package gov.lbl.glamm.server.kbase.actions;

import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.kbase.dao.KBWorkspaceDAO;
import gov.lbl.glamm.server.kbase.dao.impl.KBWorkspaceDAOImpl;
import gov.lbl.glamm.shared.exceptions.GlammRPCException;
import gov.lbl.glamm.shared.model.kbase.workspace.KBWorkspaceData;
import gov.lbl.glamm.shared.model.kbase.workspace.KBWorkspaceObjectData;

import java.util.List;

public class GetWorkspaceData {

	public static List<KBWorkspaceData> getWorkspaceList(GlammSession sm) {
		KBWorkspaceDAO dao = new KBWorkspaceDAOImpl(sm);
		return dao.getWorkspaceList();
	}
	
	public static List<KBWorkspaceObjectData> getWorkspaceModelList(GlammSession sm, String workspace) {
		KBWorkspaceDAO dao = new KBWorkspaceDAOImpl(sm);
		return dao.getWorkspaceModelList(workspace);
	}
	
	public static List<KBWorkspaceObjectData> getWorkspaceFbaList(GlammSession sm, String workspace) {
		KBWorkspaceDAO dao = new KBWorkspaceDAOImpl(sm);
		return dao.getWorkspaceFbaList(workspace);
	}

	public static Boolean userHasObjectPermissions(GlammSession sm, KBWorkspaceObjectData data) throws GlammRPCException {
		KBWorkspaceDAO dao = new KBWorkspaceDAOImpl(sm);
		return dao.userHasObjectPermissions(data);
	}

	public static List<KBWorkspaceObjectData> getObjectsWithValidPermissions(GlammSession sm, List<KBWorkspaceObjectData> data) throws GlammRPCException {
		KBWorkspaceDAO dao = new KBWorkspaceDAOImpl(sm);
		return dao.getObjectsWithValidPermissions(data);
	}
}
