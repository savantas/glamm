package gov.lbl.glamm.client.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@SuppressWarnings("serial")
public class User implements Serializable {

	private static final String MOL_ACL_GROUPID_PUBLIC	= "1";

	private String auth;
	private String userId;
	private Set<String> groupIds;
	private String email;
	
	private static transient final User guestUser = new User(null, null, null);
	

	@SuppressWarnings("unused")
	private User() {}

	public static User guestUser() {
		return guestUser;
	}

	public boolean isGuestUser() {
		return (userId == null && email == null);
	}

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

	public String getAuth() {
		return auth;
	}

	public String getUserId() {
		return userId;
	}

	public Set<String> getGroupIds() {
		// return a defensive copy
		return new HashSet<String>(groupIds);
	}

	public String getEmail() {
		return email;
	}
	
	public void setAuth(final String auth) {
		this.auth = auth;
	}
}
