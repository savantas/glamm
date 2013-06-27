package gov.lbl.glamm.server.kbase.dao;

import gov.lbl.glamm.shared.exceptions.GlammRPCException;
import gov.lbl.glamm.shared.model.kbase.workspace.KBWorkspaceData;
import gov.lbl.glamm.shared.model.kbase.workspace.KBWorkspaceObjectData;

import java.util.List;

public interface KBWorkspaceDAO {

	public List<KBWorkspaceData> getWorkspaceList();
	public List<KBWorkspaceObjectData> getWorkspaceModelList(final String workspaceName);
	public List<KBWorkspaceObjectData> getWorkspaceFbaList(final String workspaceName);
	public KBWorkspaceObjectData getWorkspaceObjectMetadata(final String id, final String workspaceName, final String objectType);
	public Boolean userHasObjectPermissions(final KBWorkspaceObjectData data) throws GlammRPCException;
	public List<KBWorkspaceObjectData> getObjectsWithValidPermissions(final List<KBWorkspaceObjectData> data) throws GlammRPCException;
	
}
