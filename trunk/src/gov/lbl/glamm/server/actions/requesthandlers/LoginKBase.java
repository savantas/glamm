package gov.lbl.glamm.server.actions.requesthandlers;

import gov.doe.kbase.auth.AuthService;
import gov.doe.kbase.auth.AuthUser;
import gov.lbl.glamm.client.map.presenter.LoginPresenter;
import gov.lbl.glamm.server.ConfigurationManager;
import gov.lbl.glamm.server.FormRequestHandler;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.RequestHandler;
import gov.lbl.glamm.server.ResponseHandler;
import gov.lbl.glamm.shared.model.User;

import java.io.IOException;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Non-RPC request handler for logging in a user into the KBase world.
 * @author wjriehl
 *
 */
public class LoginKBase implements RequestHandler {

	private static final String LOGIN_NO_EMAIL = "Login incorrect - please provide your KBase username";
	private static final String LOGIN_ERROR_MSG = "Login incorrect - please try again.";
	private static final String KBASE_AUTH_URL = ConfigurationManager.getKBaseServiceURL("authentication"); 

	static {
		AuthService.setServiceUrl(KBASE_AUTH_URL);
	}
	
	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		final GlammSession sm = GlammSession.getGlammSession(request);
		final FormRequestHandler fh = new FormRequestHandler(request, null);

		final String email = fh.getFormField(LoginPresenter.FormField.USERNAME.toString());
		final String password = fh.getFormField(LoginPresenter.FormField.PASSWORD.toString());

		if(email == null || email.isEmpty()) {
			ResponseHandler.asHtml(response, LOGIN_NO_EMAIL, HttpServletResponse.SC_OK);
			return;
		}

		try {
			AuthUser kbUser = AuthService.login(email, password);
			final User user = new User(kbUser.getUserId(), new HashSet<String>(), kbUser.getEmail());
			user.setAuth(kbUser.getToken().toString());
			user.setSessionId(kbUser.getToken().getSignature());
			sm.setUser(user);
		} 
		catch (Exception e) {
			ResponseHandler.asHtml(response, LOGIN_ERROR_MSG, HttpServletResponse.SC_OK);
		}
		
		ResponseHandler.asHtml(response, "", HttpServletResponse.SC_OK);
	}

}