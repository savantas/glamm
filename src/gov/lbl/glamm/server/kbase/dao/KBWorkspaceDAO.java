package gov.lbl.glamm.server.kbase.dao;

import gov.lbl.glamm.shared.model.kbase.workspace.KBWorkspaceData;
import gov.lbl.glamm.shared.model.kbase.workspace.KBWorkspaceObjectData;

import java.util.List;

public interface KBWorkspaceDAO {

	public List<KBWorkspaceData> getWorkspaceList();
	public List<KBWorkspaceObjectData> getWorkspaceModelList(String workspaceName);
	public List<KBWorkspaceObjectData> getWorkspaceFbaList(String workspaceName);
	
}
