package gov.lbl.glamm.server.kbase.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthToken {
	private String tokenStr;
	private String userName;
	private String tokenId;
	private String clientId;
	private String expiry;
	private String tokenType;
	private String signingSubject;
	private String signature;
	private String tokenData;
	
	public AuthToken(String token) throws IOException {
		this.tokenStr = token;
		parseToken(token);
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getTokenId() {
		return tokenId;
	}
	
	public String getClientId() {
		return clientId;
	}
	
	public String getExpiry() {
		return expiry;
	}
	
	public String getTokenType() {
		return tokenType;
	}
	
	public String getSigningSubject() {
		return signingSubject;
	}
	
	public String getSignature() {
		return signature;
	}
	
	public String getTokenData() {
		return tokenData;
	}
	
	/**
	 * Parses the KBase OAuth token into all of its fields and values.
	 * e.g. "un" -> username, "tokenid" -> the token UUID, etc.
	 *
	 * List of fields (case sensitive!): 
	 * un == user name
	 * tokenid == token UUID
	 * expiry == expiration time in seconds since time(2) epoch
	 * client_id == user ID
	 * token_type == "Bearer" or other type of token (probably Bearer)
	 * SigningSubject == URL of signing authority
	 * sig == token signature
	 * tokenData == see below
	 * 
	 * This also includes a separate field called "tokenData". This maps to the part of the token string that needs to be validated
	 * against the signature (in the "sig" field).
	 * 
	 * So you might use this as follows.
	 * Map<String, String> parsed = parseToken(token);
	 * hashAndCompare(parsed.get("tokenData"), parsed.get("sig"));
	 * (also including some error checking)
	 * 
	 * @param token
	 * @return
	 * @throws IOException
	 */
	private Map<String, String> parseToken(String token) throws IOException {
		Map<String, String> parsed = new HashMap<String, String>();
		/**
		 * Expect the token to be of the following format:
		 * "key1=value1|key2=value2|key3=value3|..."
		 * So, '|' and '=' are expected to be only present as delimiters.
		 */
		String[] tokenFields = token.split("[|]");
		for (String field : tokenFields) {
			String[] keyValuePair = field.split("[=]");
			
			// Should be exactly 2 elements here. If not == bad news.
			if (keyValuePair.length != 2) {
				throw new IOException("Auth token is in the incorrect format, near '" + field + "'");
			}
			parsed.put(keyValuePair[0], keyValuePair[1]);
		}

		// Everything up to '|sig=' is the token data, so grab that.
		int sigPos = token.indexOf("|sig=");

		// If we can't find that fragment, or it's at the end of the string, throw an error - there's no sig present!
		if (sigPos == -1 || token.length() < sigPos+5) {
			throw new IOException("Auth token is in the incorrect format - might be missing the signature?");
		}

		userName = parsed.get("un");
		tokenId = parsed.get("tokenid");
		expiry = parsed.get("expiry");
		clientId = parsed.get("client_id");
		tokenType = parsed.get("token_type");
		signingSubject = parsed.get("SigningSubject");
		signature = parsed.get("sig");
		tokenData = token.substring(0, sigPos);
		
		
		parsed.put("tokenData", token.substring(0, sigPos));
		
		return parsed;
	}
	
	public String toString() {
		return tokenStr;
	}
}
