package gov.lbl.glamm.server.kbase.auth;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A fairly simple OAuth service for KBase, intended (at least initially) for use with GLAMM.
 * Usage:
 * 
 * AuthService service = new AuthService(serviceURL);
 * AuthUser user = service.login(user, password);
 * if (service.validateToken(user.getToken())) {
 * 		// There's a valid token! Return the valid user, or just the token, and move along.
 * }
 * 
 * Thus, this provides code for a user to log in to KBase, retrieve a valid Auth token, 
 * 
 * @author wjriehl
 */
public class AuthService {
	private String authLoginUrl;
	
	public AuthService(String url) {
		this.authLoginUrl = url;
	}
	
	/**
	 * Logs in a user and returns an AuthUser object, which is more or less a POJO containing basic user attributes,
	 * as well as the generated AuthToken.
	 * 
	 * @param user
	 * @param pw
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public AuthUser login(String userName, String password) throws UnsupportedEncodingException, 
														 		   NoSuchAlgorithmException,
														 		   KeyManagementException,
														 		   IOException {

		String dataStr = "user_id=" + userName + "&password=" + password + "&cookie=1";

		HttpsURLConnection conn = (HttpsURLConnection) new URL(authLoginUrl).openConnection();
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(dataStr.getBytes().length));
		conn.setRequestProperty("Content-Language", "en-US");
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		
		DataOutputStream writer = new DataOutputStream(conn.getOutputStream());
		writer.writeBytes(dataStr);
		writer.flush();
		writer.close();
		
		int responseCode = conn.getResponseCode();
		if (responseCode != 200) {
			conn.disconnect();
			throw new IOException("Login failed! Server responded with code " + responseCode + " " + conn.getResponseMessage());
		}

		/** Encoding the HTTP response into JSON format */
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

		AuthUser user = null;
		user = new ObjectMapper().readValue(br, AuthUser.class);

		br.close();
		conn.disconnect();
		
		// In the end, return the user.
		if (user == null) { // if still null, throw an exception 
			throw new IOException("Unable to construct a user object from login results!");
		}
		return user;
	}

	/**
	 * Works in a few steps.
	 * 1. Check the token format, throw IOException if wrong format
	 * 2. Extract the 'token' part and the signature
	 * 3. Get the public key from the 'SigningSubject' part of the token
	 * 4. Do the sha1-RSA comparison
	 * 5. Return the result.
	 * 
	 * Most of this is taken from KBaseAuthValidateToken.java, written by Shuchu Han, as part of the Persistent Store code.
	 * So, thanks!
	 * @param token
	 * @return
	 */
	public boolean validateToken(AuthToken token) throws IOException, NoSuchAlgorithmException, KeyManagementException, InvalidKeyException, SignatureException {
		
		/** now HTTPS the SigningSubgject of input Token */
		URL validationUrl = new URL(token.getSigningSubject());
		
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { 
			new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() { 
					return new X509Certificate[0]; 
				}
				public void checkClientTrusted(X509Certificate[] certs, String authType) {}
				public void checkServerTrusted(X509Certificate[] certs, String authType) {}
			}
		};
		
		// Ignore differences between given hostname and certificate hostname
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) { return true; }
		};
		
		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
		
		/** make the request to Authentication server */
		HttpsURLConnection conn = (HttpsURLConnection) validationUrl.openConnection(); 
		InputStream in = conn.getInputStream();
		
		/** Encoding the HTTP response into JSON format */
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		ObjectMapper m = new ObjectMapper();
		JsonNode jn = m.readTree(br);
		JsonNode jd = jn.get("pubkey");

		in.close();
		conn.disconnect();
		
		/** now get the public key and do the verify */
		Security.addProvider(new BouncyCastleProvider());
		PEMReader pemReader = new PEMReader(new StringReader(jd.textValue().replace("\\n","\n")));
		RSAPublicKey pubKey = (RSAPublicKey) pemReader.readObject();
		pemReader.close();

		/** http://docs.oracle.com/javase/6/docs/technotes/guides/security/crypto/CryptoSpec.html#KeyFactoryEx */
		Signature s = Signature.getInstance("SHA1withRSA");
		
		s.initVerify(pubKey);

		/** update the data
		 * 
		 *	SHA-1 (and all other hashing algorithms) return binary data. That means that (in Java) they produce a byte[].
		 *  That byte array does not represent any specific characters, which means you can't simply turn it into a String 
		 *  like you did.If you need a String, then you have to format that byte[] in a way that can be represented as 
		 *  a String (otherwise, just keep the byte[] around).Two common ways of representing arbitrary byte[] as printable
		 *  characters are BASE64 or simple hex-Strings (i.e. representing each byte by two hexadecimal digits). 
		 *  It looks like you're trying to produce a hex-String. There's also another pitfall: if you want to get the SHA-1 
		 *  of a Java String, then you need to convert that String to a byte[] first (as the input of SHA-1 is a byte[] as well). 
		 *  If you simply use myString.getBytes() as you showed, then it will use the platform default encoding and as such 
		 *  will be dependent on the environment you run it in (for example it could return different data based on the 
		 *  language/locale setting of your OS).A better solution is to specify the encoding to use for the String-to-byte[] 
		 *  conversion like this: myString.getBytes("UTF-8"). Choosing UTF-8 (or another encoding that can represent every 
		 *  unicode character) is the safest choice here.
		 */
		byte[] sig_data_byte = token.getTokenData().getBytes("UTF-8");
		s.update(sig_data_byte);

		/**
		* The equivalent of Perl 's pack "H*", $vartoconvert in Java is :
		* javax.xml.bind.DatatypeConverter.parseHexBinary(hexadecimalString);.
		* For more information on this, I think it is recommended to read 
		* DatatypeConverter class' reference from JavaDocs. 
		*/
		byte[] sig_byte = javax.xml.bind.DatatypeConverter.parseHexBinary(token.getSignature());
		
		/** verification of signature*/
		boolean result = s.verify(sig_byte);
	
		return result;
	}
}