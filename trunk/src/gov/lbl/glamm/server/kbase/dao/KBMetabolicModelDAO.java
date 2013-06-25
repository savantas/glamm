package gov.lbl.glamm.server.kbase.dao;

import gov.lbl.glamm.client.map.exceptions.UnauthorizedException;
import gov.lbl.glamm.shared.model.kbase.fba.KBFBAResult;
import gov.lbl.glamm.shared.model.kbase.fba.model.KBMetabolicModel;
import gov.lbl.glamm.shared.model.kbase.workspace.KBWorkspaceObjectData;

public interface KBMetabolicModelDAO {
	
	public KBMetabolicModel getModel(final KBWorkspaceObjectData modelData) throws UnauthorizedException;
	public KBMetabolicModel getModel(final String modelId, final String workspaceId) throws UnauthorizedException;
	public KBFBAResult getFBAResult(String fbaId, String workspaceId) throws UnauthorizedException;
}
