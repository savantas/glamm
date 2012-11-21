package gov.lbl.glamm.server.actions.requesthandlers;

import gov.lbl.glamm.client.map.presenter.LoginPresenter;
import gov.lbl.glamm.server.FormRequestHandler;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.RequestHandler;
import gov.lbl.glamm.server.ResponseHandler;
import gov.lbl.glamm.server.kbase.auth.AuthService;
import gov.lbl.glamm.server.kbase.auth.AuthUser;
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

	private static final String LOGIN_ERROR_MSG = "Login incorrect - please try again.";
	private static final String kbaseLoginURL = "https://kbase.us/services/authorization/Sessions/Login";

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		final GlammSession sm = GlammSession.getGlammSession(request);
		final FormRequestHandler fh = new FormRequestHandler(request, null);

		final String email = fh.getFormField(LoginPresenter.FormField.USERNAME.toString());
		final String password = fh.getFormField(LoginPresenter.FormField.PASSWORD.toString());

		if(email == null || email.isEmpty()) {
			ResponseHandler.asHtml(response, LOGIN_ERROR_MSG, HttpServletResponse.SC_OK);
			return;
		}

		AuthService authService = new AuthService(kbaseLoginURL);
		
		try {
			AuthUser kbUser = authService.login(email, password);
			final User user = new User(kbUser.getUserName(), new HashSet<String>(), kbUser.getEmail());
			user.setAuth(kbUser.getTokenString());
			user.setSessionId(kbUser.getToken().getSignature());
			sm.setUser(user);
		} catch (Exception e) {
			throw new IOException(e.getLocalizedMessage());
		}
		
		ResponseHandler.asHtml(response, "", HttpServletResponse.SC_OK);
	}

}