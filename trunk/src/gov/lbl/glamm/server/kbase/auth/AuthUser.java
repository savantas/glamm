package gov.lbl.glamm.server.kbase.auth;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthUser {
	private String userName;
	private boolean emailValidated;
	private String userId;
	private String email;
	private List<String> groups;
	private String fullName;
	private String kbaseSession;
	private String errorMessage = null;
	private AuthToken token;
	
	public AuthUser() {
		
	}

	@JsonProperty("username")
	public String getUserName() {
		return userName;
	}
	@JsonProperty("username")
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@JsonProperty("email_validated")
	public boolean isEmailValidated() {
		return emailValidated;
	}
	@JsonProperty("email_validated")
	public void setEmailValidated(boolean emailValidated) {
		this.emailValidated = emailValidated;
	}

	@JsonProperty("user_id")
	public String getUserId() {
		return userId;
	}
	@JsonProperty("user_id")
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@JsonProperty("email")
	public String getEmail() {
		return email;
	}
	@JsonProperty("email")
	public void setEmail(String email) {
		this.email = email;
	}

	@JsonProperty("token")
	public String getTokenString() {
		return token.toString();
	}
	@JsonProperty("token")
	public void setToken(String tokenStr) throws IOException {
		this.token = new AuthToken(tokenStr);
	}
	
	public AuthToken getToken() {
		return token;
	}
	
	public void setToken(AuthToken token) {
		this.token = token;
	}

	@JsonProperty("groups")
	public List<String> getGroups() {
		return groups;
	}
	@JsonProperty("groups")
	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	@JsonProperty("fullname")
	public String getFullName() {
		return fullName;
	}
	@JsonProperty("fullname")
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@JsonProperty("kbase_sessionid")
	public String getKbaseSession() {
		return kbaseSession;
	}
	@JsonProperty("kbase_sessionid")
	public void setKbaseSession(String kbaseSession) {
		this.kbaseSession = kbaseSession;
	}
	
	@JsonProperty("error_msg")
	public String getErrorMessage() {
		return errorMessage;
	}
	@JsonProperty("error_msg")
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("user name: " + userName + "\n");
		buf.append("user id: " + userId + "\n");
		buf.append("full name: " + fullName + "\n");
		buf.append("email validated: " + emailValidated + "\n");
		buf.append("email: " + email + "\n");
		buf.append("groups: " + groups + "\n");
		buf.append("session id: " + kbaseSession + "\n");
		buf.append("token: " + token.toString() + "\n");
		return buf.toString();
	}
}