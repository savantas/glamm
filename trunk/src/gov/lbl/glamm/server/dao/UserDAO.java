package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.client.model.User;

public interface UserDAO {
	public String 		getPasswordHashForEmail(final String email);
	public User 	getUserForUserId(final String userId);
	public User 	getUserForEmail(final String email);
}
