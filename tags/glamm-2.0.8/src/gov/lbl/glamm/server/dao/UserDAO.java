package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.client.model.GlammUser;

public interface UserDAO {
	public String 		getPasswordHashForEmail(final String email);
	public GlammUser 	getUserForUserId(final String userId);
	public GlammUser 	getUserForEmail(final String email);
}
