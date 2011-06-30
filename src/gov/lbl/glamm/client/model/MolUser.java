package gov.lbl.glamm.client.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@SuppressWarnings("serial")
public class MolUser implements Serializable {
	private String userId = null;
	private Set<String> groupIds = null;
	private String email = null;

	@SuppressWarnings("unused")
	private MolUser() {}

	public MolUser(final String userId, final Collection<String> groupIds, final String email) {
		this.userId = userId;
		if(groupIds != null && !groupIds.isEmpty()) {
			this.groupIds = new HashSet<String>();
			this.groupIds.addAll(groupIds);
		}
		this.email = email;
	}

	public String getUserId() {
		return userId;
	}

	public Set<String> getGroupIds() {
		return groupIds;
	}

	public String getEmail() {
		return email;
	}
	
	
}
