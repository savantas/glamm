package gov.lbl.glamm.server.dao;

import gov.lbl.glamm.shared.model.User;

/**
 * Data access object interface for users.
 * @author jtbates
 *
 */
public interface UserDAO {
	/**
	 * Gets the password hash for a user's email address.
	 * @param email The email address.
	 * @return The password hash.
	 */
	public String 		getPasswordHashForEmail(final String email);
	
	/**
	 * Gets the user object for a user id.
	 * @param userId The user id.
	 * @return The user.
	 */
	public User 	getUserForUserId(final String userId);
	
	/**
	 * Gets the user object for an email address.
	 * @param email The email address.
	 * @return The user.
	 */
	public User 	getUserForEmail(final String email);
}
