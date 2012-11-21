package gov.lbl.glamm.shared.model;

import gov.lbl.glamm.server.kbase.auth.AuthToken;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Model class for users.  Users may log in and log out of GLAMM via their MicrobesOnline login credentials.
 * @author jtbates
 *
 */
@SuppressWarnings("serial")
public class User implements Serializable {

	private static final String MOL_ACL_GROUPID_PUBLIC	= "1";

	private String auth;
	private String sessionId;
	private String userId;
	private Set<String> groupIds;
	private String email;
	
	private static transient final User guestUser = new User(null, null, null);
	

	@SuppressWarnings("unused")
	private User() {}

	/**
	 * Convenience method for generating a guest user.
	 * @return A guest user.
	 */
	public static User guestUser() {
		return guestUser;
	}

	/**
	 * Determines if a user is a guest user.
	 * @return A flag indicating whether or not the user is a guest user.
	 */
	public boolean isGuestUser() {
		return (userId == null && email == null);
	}

	/**
	 * Constructor
	 * @param userId The id of the user.
	 * @param groupIds The MicrobesOnline ACL group ids to which this user belongs.
	 * @param email The email address of this user.
	 */
	public User(final String userId, final Set<String> groupIds, final String email) {
		this.auth = null;
		this.userId = userId;
		if(groupIds != null && !groupIds.isEmpty())
			this.groupIds = new HashSet<String>(groupIds);
		else
			this.groupIds = new HashSet<String>();
		this.groupIds.add(MOL_ACL_GROUPID_PUBLIC);  // ensure the public group id is always in the set
		this.email = email;
	}
	
	public String getSessionId() {
		if (sessionId == null)
			return auth;
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	/**
	 * Gets the auth token for the user.
	 * @return The auth token.
	 */
	public String getAuth() {
		return auth;
	}

	/**
	 * Gets the id for this user.
	 * @return The id.
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Gets the set of group ids for this user.
	 * @return The group ids.
	 */
	public Set<String> getGroupIds() {
		// return a defensive copy
		return new HashSet<String>(groupIds);
	}

	/**
	 * Gets the email address of this user.
	 * @return The email address.
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Sets the auth token for this user.
	 * @param auth The auth token.
	 */
	public void setAuth(final String auth) {
		this.auth = auth;
	}
}
