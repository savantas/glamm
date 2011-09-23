package gov.lbl.glamm.server.actions.requesthandlers;

import gov.lbl.glamm.client.model.User;
import gov.lbl.glamm.client.presenter.LoginPresenter;
import gov.lbl.glamm.server.FormRequestHandler;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.RequestHandler;
import gov.lbl.glamm.server.ResponseHandler;
import gov.lbl.glamm.server.dao.UserDAO;
import gov.lbl.glamm.server.dao.impl.UserDAOImpl;
import gov.lbl.glamm.server.util.GlammUtils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Login implements RequestHandler {

	private static final String LOGIN_ERROR_MSG = "Login incorrect - please try again.";

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		final GlammSession sm = GlammSession.getGlammSession(request);
		final FormRequestHandler fh = new FormRequestHandler(request, null);

		final String email = fh.getFormField(LoginPresenter.View.FIELD_LOGIN_USERNAME);
		final String password = fh.getFormField(LoginPresenter.View.FIELD_LOGIN_PASSWORD);

		if(email == null || email.isEmpty()) {
			ResponseHandler.asHtml(response, LOGIN_ERROR_MSG, HttpServletResponse.SC_OK);
			return;
		}

		final UserDAO userDao = new UserDAOImpl(sm); 
		final String pwHash = userDao.getPasswordHashForEmail(email);
		
		if(pwHash == null || pwHash.isEmpty() || !pwHash.equals(GlammUtils.genBase64MD5String(password))) {
			ResponseHandler.asHtml(response, LOGIN_ERROR_MSG, HttpServletResponse.SC_OK);
			return;
		}

		final User user	= userDao.getUserForEmail(email);
		
		final String userId 	= user.getUserId();
		final String remoteAddr	= request.getRemoteAddr();
		final String auth 		= GlammUtils.genBase64MD5String(userId + "|" + remoteAddr);

		user.setAuth(auth);
		sm.setUser(user);
		
		ResponseHandler.asHtml(response, "", HttpServletResponse.SC_OK);
	}

}
